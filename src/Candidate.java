import java.util.HashSet;

public class Candidate {
    private int number;
    private String name;
    private String party;
    private HashSet<String> votes = new HashSet<>();

    public Candidate(int number, String name, String party) {
        this.setNumber(number);
        this.setName(name);
        this.setParty(party);
    }

    public void addVote(String voter) {
        votes.add(voter);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public HashSet<String> getVotes() {
        return votes;
    }

    public void setVotes(HashSet<String> votes) {
        this.votes = votes;
    }
}
