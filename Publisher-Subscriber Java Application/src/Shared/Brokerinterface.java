package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Brokerinterface extends Remote {
    

    void publish(Eventmessage message) throws RemoteException;

    void subscribe(Eventtype topic_content, String sub_ip) throws RemoteException;

    void unSubscribe(Eventtype topic_content, String sub_ip) throws RemoteException;

    List<Eventmessage> get_publishersnotify(Eventtype topic_content) throws RemoteException;
}
