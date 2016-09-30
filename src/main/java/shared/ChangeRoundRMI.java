package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChangeRoundRMI extends Remote {

	public void changeRound(String currentUser) throws RemoteException;

}
