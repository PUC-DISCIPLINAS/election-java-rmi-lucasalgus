import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Election extends Remote {
    void vote(String voter, String candidate) throws RemoteException;
    int result(String candidate) throws RemoteException;
}
