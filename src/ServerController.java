import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ServerController {
    public ArrayList<Candidate> readCandidatesFile(String pathname) throws IOException {
        var candidates = new ArrayList<Candidate>();
        var path = Paths.get(pathname);

        Files.lines(path).skip(1).forEach(line -> {
            if (line.length() > 0) {
                var candidateString = line.split(";");
                var c = new Candidate(Integer.parseInt(candidateString[0]), candidateString[1], candidateString[2]);

                candidates.add(c);
            }
        });

        return candidates;
    }

    public void readVotesToCandidates(ArrayList<Candidate> candidates) throws IOException {

        try {
            var path = Paths.get("votes.csv");

            Files.lines(path).forEach(line -> {
                var voteString = line.split(";");

                if (voteString.length == 0) {
                    return;
                }

                var candidate = candidates.stream().filter(c -> {
                    return c.getNumber() == Integer.parseInt(voteString[0]);
                }).collect(Collectors.toList()).get(0);

                candidate.getVotes().add(voteString[1]);
            });
        } catch (IOException e) {
            File file = new File("votes.csv");
            file.createNewFile();
        }
    }

    public void initialize() {
        try {
            Scanner sc = new Scanner(System.in);
            String senatorsPathname;

            System.out.println("Digite o caminho do CSV de senadores:");
            senatorsPathname = sc.nextLine();

            var candidates = readCandidatesFile(senatorsPathname);

            readVotesToCandidates(candidates);

            var election = new ElectionServant(candidates);

            Naming.rebind("rmi://localhost/ElectionService", election);

            System.out.println("Servidor em execução");
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
