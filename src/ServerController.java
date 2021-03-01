import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.util.ArrayList;

public class ServerController {
    public ArrayList<Candidate> readCandidatesFile() throws IOException {
        var candidates = new ArrayList<Candidate>();
        var path = Paths.get("/Users/lucas/dev/University/election-java-rmi-lucasalgus/senadores.csv");

        Files.lines(path).skip(1).forEach(line -> {
            if (line.length() > 0) {
                var candidateString = line.split(";");
                var c = new Candidate(Integer.parseInt(candidateString[0]), candidateString[1], candidateString[2]);

                candidates.add(c);
            }
        });

        return candidates;
    }

    public void initialize() {
        try {
            var candidates = readCandidatesFile();
            var election = new ElectionServant(candidates);

            // System.setProperty("java.rmi.server.hostname", "localhost");
            // System.setProperty("java.security.policy", "rmi.policy");
			// System.setProperty("java.rmi.server.useCodebaseOnly", "false");
			// System.setProperty("java.rmi.server.codebase", "/Users/lucas/dev/University/election-java-rmi-lucasalgus/bin/Main.class");
            Naming.rebind("rmi://localhost/ElectionService", election);

            System.out.println("Servidor em execução");
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
