import java.math.BigInteger;
import java.rmi.Naming;
import java.security.MessageDigest;
import java.util.Scanner;

public class ClientController {
    private Scanner scanner;
    private Election election;
    private String name;
    private String token;

    private void log(String message) {
        System.out.print(message);
    }

    private void br() {
        System.out.print("\n");
    }

    private String generateToken(String name) throws Exception {
        var md = MessageDigest.getInstance("MD5");
        md.update(name.getBytes());
        byte[] md5 = md.digest();

        BigInteger numMd5 = new BigInteger(1, md5);
        String hashMd5 = String.format("%022x", numMd5);

        return hashMd5;
    }

    private void vote(String candidate) {
        try {
            election.vote(token, candidate);
            log("Voto registrado com sucesso!");
        } catch (Exception e) {
            log("Erro ao registrar o voto: ");
            log(e.getMessage());
        }
    }

    private void result(String candidate) {
        try {
            var count = election.result(candidate);
            log("Resultados do candidato " + candidate + ": " + count + " votos.");
        } catch (Exception e) {
            log("Erro ao acessar resultados");
            log(e.getMessage());
        }
    }

    private void interpretCommand(String line) {
        try {
            var candidate = line.split(" ")[1];

            if (line.contains("vote")) {
                vote(candidate);
            } else if (line.contains("result")) {
                result(candidate);
            } else {
                throw new Exception();
            }
        } catch(Exception e) {
            log("Comando inválido");
        }
    }

    public void initialize() throws Exception {
        scanner = new Scanner(System.in);
        System.setProperty("java.rmi.server.hostname", "localhost");
        System.setProperty("java.security.policy", "rmi.policy");
        election = (Election) Naming.lookup("rmi://localhost/ElectionService");

        log("Digite o seu nome");
        name = scanner.nextLine();
		token = generateToken(name);

        try {
            token = generateToken(name);
        } catch (Exception e) {
            log("Erro ao gerar token de eleitor");
            return;
        }

        log("Bem vindo ao sistema de eleição.");
        br();
        log("Comandos:");
        log("- `vote <numero>` (Exemplo: vote 10) ");
        log("- `result <numero>` (Exemplo: result 10) ");
        br();

        while (true) {
            var line = scanner.nextLine();
            interpretCommand(line);
        }
    }
}
