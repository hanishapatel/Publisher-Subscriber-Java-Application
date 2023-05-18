package Shared;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ManghaniP3SubscriberRMIinterface  extends Remote{

    void publishing_event(Eventmessage message) throws RemoteException;

    
}
