package Subscriber;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import Broker.ManghaniP3broker;
import Publisher.ManghaniP3Pub;
import Shared.Basic;
import Shared.Brokerinterface;
import Shared.Eventmessage;
import Shared.Eventtype;
import Shared.ManghaniP3SubscriberRMIinterface;
import Shared.Credentials.Logintype;
import Subscriber.ManghaniP3sub;
import Shared.Credentials;
import Shared.choice;
public class ManghaniP3sub extends choice implements ManghaniP3SubscriberRMIinterface {

    private Brokerinterface brokerstubs;
    String Subaddr;
    List<Eventtype> sublist;
    int PortNumber;

    public ManghaniP3sub(Scanner s) {
        super(s);
        sublist = new ArrayList<Eventtype>();
        System.out.print("Please Enter the Subscriber PortNumber =  ");
        PortNumber = Integer.parseInt(s.nextLine());

        String[] addrlist = Basic.connectionPort(PortNumber);
        if (addrlist.length > 1) {
            System.out.print("Please Enter your Bulletin Board IP add:\n");
            this.Subaddr = this.scan.nextLine().trim();
        } else {
            this.Subaddr = addrlist[0];
            System.out.println("Your Sytem IP add is = " + this.Subaddr);
        }
        Registry Brokerregistery;
        try {
            Brokerregistery = LocateRegistry.createRegistry(PortNumber);
            ManghaniP3SubscriberRMIinterface subRMIinterface;
            subRMIinterface = (ManghaniP3SubscriberRMIinterface) UnicastRemoteObject.exportObject(this, 0);
            Brokerregistery.rebind("Subscriberegistry", subRMIinterface);
        } catch (RemoteException e1) {
            System.out.println("Eroor in the Subscriber class........");
        }
        while (this.brokerstubs == null) {
            try {
                System.out.print("Enter Bulletin Board IP add for connection\n");
                String ipaddStr = s.nextLine();
                Registry registry = LocateRegistry.getRegistry(ipaddStr.split(":")[0],
                        Integer.parseInt(ipaddStr.split(":")[1]));
                System.out.print("\nSuccesfully Connected to the Bulletin Board\n\n");
                this.brokerstubs = (Brokerinterface) registry.lookup("BulletinboardRegistry");
            } catch (Exception e) {
                System.err.println(" Invalid details! try again. " + e.toString());
            }
        }

    }

    @Override
    public String toString() {
        String represent = "<" + name + ">\n";
        represent += "Topic: "  + "\n";
        return represent;
    }

    @Override
    public void choices_call() 
    {
        
        while (true) {

            try {
               
                System.out.print("Please select the one:-\n");
                System.out.print("*------------------------------*\n\n");
                System.out.print("Enter 1 for Topic Subscription \n");
                System.out.print("Enter 2 for Topic Unsubscription \n");
                System.out.print("Enter 3 for Display the  Publication Messages here  \n\n");
                System.out.print("Your Selected Menu is : ");
                int selection = Integer.parseInt(this.scan.nextLine());
                switch (selection) {
                    case 1:
                        subscription();
                        break;
                    case 2:
                        unsubscription();
                        break;
                    case 3:
                        FetchingsubscribedPublist();
                        break;
                    default:
                        throw new Exception("Invalid Publisher input");
                }

    
            } catch (Exception e) {
                //System.err.println("Exception on subscriber selection");
            }
        }
    }


    void subscription() {
        Eventtype temp_topic1 = null;
        while (temp_topic1 == null) {
            try {
                System.out.println("Available Topics are as follows:");
                System.out.println("\n");
                int i = 0;
                for (Eventtype temp_topic : Eventtype.values()) {
                    i++;
                 
                    if (this.sublist.contains(temp_topic))
                        continue;
                    System.out.println(" "+i +". "+ temp_topic);
                }
                System.out.print("Enter Topic number to subscribe: ");
                int topic_selected = Integer.parseInt(this.scan.nextLine());
                temp_topic1 = Eventtype.values()[topic_selected-1];
                System.out.print("\nSuccesfully subscribed to topic: " + temp_topic1 +"\n\n");
            } catch (Exception e) {
                System.err.println("Invalid topic choice! try again");
            }
        }
        try {
            this.brokerstubs.subscribe(temp_topic1, this.Subaddr);
            this.sublist.add(temp_topic1);
        } catch (RemoteException e) {
            System.err.println("Error while subscribing to " + temp_topic1);
        }
    }
 @Override
public void publishing_event(Eventmessage Textmessage) throws RemoteException {
        System.out.print(Textmessage);
    }


    private void FetchingsubscribedPublist() {

        Eventtype topicnum = null;
        
        while (topicnum == null) {
            try {
                int l = 0;
                System.out.println("Subscribed Topics are as follows: ");
                for (Eventtype eventtype : this.sublist) {
                    System.out.println("  "+(l++) + ". " + eventtype);
                }
                System.out.print("\n Please enter Topic number to view publication message: ");
                int chhosedtopic = Integer.parseInt(this.scan.nextLine());
                topicnum = this.sublist.get(chhosedtopic);
            } catch (Exception e) {
                System.err.println("\nError on getting all subscribed publishers messages........\n");
            }
        }
        try {
            List<Eventmessage> publisherslist = this.brokerstubs.get_publishersnotify(topicnum);
            System.out.println("\n" );
            publisherslist.forEach((m) -> System.out.print( m));
        } catch (RemoteException e) {
            System.err.println("Getting Publications from message queue - Error:" + topicnum);
            e.printStackTrace();
        }
    }

    void unsubscription() {
        Eventtype topicidnum = null;
        
        while (topicidnum == null) {
            try {
                int i = 0;
                System.out.print("Available Topics are as follows: ");
                for (Eventtype topictype : this.sublist) {
                    System.out.println(" "+ topictype + "-"+ (i++) +"\n");
                }
                System.out.print("Please enter Topic number for unsubscribe that topic -  ");
                int chhosedtopic = Integer.parseInt(this.scan.nextLine());
                topicidnum = this.sublist.get(chhosedtopic);
            } catch (Exception e) {
                System.err.println("UnSubscription Error:- No valid selection topic ");
            }
        }
        try {
            this.brokerstubs.unSubscribe(topicidnum, this.Subaddr);
            this.sublist.remove(topicidnum);
        } catch (RemoteException e) {
            System.err.println("Revoking error on subscribtion " + topicidnum +"\n ");
        }
    }
    
}
