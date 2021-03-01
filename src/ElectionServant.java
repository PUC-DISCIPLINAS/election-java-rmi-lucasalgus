import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ElectionServant extends UnicastRemoteObject implements Election {
	private static final long serialVersionUID = 1L;
	ArrayList<Candidate> candidates;

    public ElectionServant(ArrayList<Candidate> candidates) throws RemoteException {
        super();
        this.candidates = candidates;
    }

    public void vote(String voter, String candidate) throws RemoteException {
        var candidateObj = candidates.stream()
                .filter(c -> c.getNumber() == Integer.parseInt(candidate))
                .collect(Collectors.toList()).get(0);

        candidateObj.addVote(voter);
    }

    public int result(String candidate) throws RemoteException {
        var candidateObj = candidates.stream()
                .filter(c -> c.getNumber() == Integer.parseInt(candidate))
                .collect(Collectors.toList()).get(0);

        return candidateObj.getVotes().size();
    }
}
