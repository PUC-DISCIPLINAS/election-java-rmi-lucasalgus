import java.math.BigInteger;
import java.net.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientController {
    private Scanner scanner;
    private Election election;
    private String name;
    private String token;

    private void log(String message) {
        System.out.println(message);
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
        } catch (RemoteException e) {
            var t = new Timer();

             t.scheduleAtFixedRate(new TimerTask(){
                int tries = 0;
                boolean success = false;

                @Override
                public void run(){
                    try {
                        if (success) {
                            log("Voto registrado com sucesso!");
                            t.cancel();
                        }
                        if (tries >= 30) {
                            log("Erro ao registrar o voto.");
                            t.cancel();
                        }
                        log("Tentando... " + tries + "/30");
                        tries++;
                        initServer();
                        election.vote(token, candidate);
                        success = true;
                    } catch (Exception ex) {
                        //
                    }
                }
            },0,1000);
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
        String candidate = "";
        try {
            candidate = line.split(" ")[1];

            if (line.contains("vote")) {
                vote(candidate);
            } else if (line.contains("result")) {
                result(candidate);
            } else {
                throw new Exception();
            }
        }  catch(Exception e) {
            log("Comando inválido");
			log(e.getMessage());
        }
    }

    public void initServer() throws Exception {
        System.setProperty("java.rmi.server.hostname", "localhost");
        System.setProperty("java.security.policy", "rmi.policy");
        election = (Election) Naming.lookup("rmi://localhost/ElectionService");
    }

    public void initialize() throws Exception {
        scanner = new Scanner(System.in);
        initServer();

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

		var line = scanner.nextLine();
		interpretCommand(line);
    }
}
