package Broker;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import Shared.choice;
import Shared.Basic;
import Shared.Brokerinterface;
import Shared.Eventmessage;
import Shared.Eventtype;
import Shared.ManghaniP3SubscriberRMIinterface;
import Shared.choice;


public class ManghaniP3broker extends choice implements Brokerinterface
{

    private Map<Eventtype, List<Eventmessage>> message_type_map;
    private Map<Eventtype, List<String>> sublist_map;
    private Map<String, ManghaniP3SubscriberRMIinterface> substub_map;

    public ManghaniP3broker(Scanner scan) throws IOException {
        super(scan);

        System.out.print("Enter the PORT-NUMBER on which you want to run this:"+"\n");
        int port = Integer.parseInt(scan.nextLine());// = 911;//
        Basic.connectionPort(port);
        Registry serviceRegistry = LocateRegistry.createRegistry(port);
        Brokerinterface brokerinterfaces;
        brokerinterfaces = (Brokerinterface) UnicastRemoteObject.exportObject(this, 0);
        serviceRegistry.rebind("BulletinboardRegistry", brokerinterfaces);
        message_type_map = new HashMap<Eventtype, List<Eventmessage>>();
        this.sublist_map = new HashMap<Eventtype, List<String>>();
        this.substub_map = new HashMap<String, ManghaniP3SubscriberRMIinterface>();
    }

    public void choices_call() {
        System.out.println(" Welcome to Bulletin Board\n *------------------------------*\n\n ");
        String selected_menu;
        boolean value = false;
        while (true) {
            System.out.print("Please enter your selected Menu\n ");
            System.out.print("1.Print the number of Publications\n 2. Print the number of Subscribers\n\n Your Selected Menu is : ");
            selected_menu = this.scan.nextLine();

            switch (selected_menu.charAt(0)) {
                case '1':
                    System.out.println(" Total Publications in the System are as follows-\n");
                    message_type_map.forEach((t, p) -> {
                        System.out.println("  " + t + ":");
                        p.forEach(m -> System.out.println("   " + m));
                    });
                    break;
                case '2':
                System.out.println("\n");
                    System.out.println(" Total Subscribers in the System are as follows-\n");
                    for (Eventtype topics_content : this.sublist_map.keySet()) {
                        System.out.println( "Subscribed Topic is :"+ topics_content);
                        this.sublist_map.get(topics_content).forEach((stub_ip) -> System.out.println("   "));
                    }
                    break;
                default:
                System.err.println("Please try again......");
            }
        }
    }

    @Override
    public void publish(Eventmessage textmessage) throws RemoteException {
        Eventtype messagetype = textmessage.get_topiccontent();
        if (message_type_map.containsKey(messagetype)) {
            LinkedList<Eventmessage> Publist = (LinkedList<Eventmessage>) message_type_map.get(messagetype);
            Publist.addFirst(textmessage);
        } else {
            List<Eventmessage> Publist = new LinkedList<Eventmessage>();
            Publist.add(textmessage);
            message_type_map.put(messagetype, Publist);
        }

        List<String> sublist_data = this.sublist_map.get(messagetype);
        if (sublist_data != null) {
            for (String subscribers : sublist_data) {
                this.substub_map.get(subscribers).publishing_event(textmessage);
            }
        }
    }


   

    @Override
    public void subscribe(Eventtype topiccontent, String subaddr) throws RemoteException {
        ManghaniP3SubscriberRMIinterface temp_sub_stub;
        try {
            Registry registry = LocateRegistry.getRegistry(subaddr.split(":")[0],
                    Integer.parseInt(subaddr.split(":")[1]));
            temp_sub_stub = (ManghaniP3SubscriberRMIinterface) registry.lookup("Subscriberegistry");
            List<String> sublistdata = this.sublist_map.get(topiccontent);
            if (sublistdata == null) {
                sublistdata = new ArrayList<String>();
                this.sublist_map.put(topiccontent, sublistdata);
            }
            sublistdata.add(subaddr);
            if (!this.substub_map.containsKey(subaddr)) {
                this.substub_map.put(subaddr, temp_sub_stub);
            }
        } catch (Exception e) {
            System.err.println("It is no the valid subscriber:" + subaddr);
            e.printStackTrace();
        }
    }

    @Override
    public void unSubscribe(Eventtype topiccontent, String subscriberaddr) throws RemoteException {
        try {
            boolean boolvalue = false;
            List<String> subcriberlist = this.sublist_map.get(topiccontent);
            subcriberlist.remove(subscriberaddr);
            for (Eventtype topictype : this.sublist_map.keySet()) {
                boolvalue = this.sublist_map.get(topictype).contains(subscriberaddr);
                if (boolvalue)
                    break;
            }
            if (!boolvalue) {
                System.out.println("This Subscribe is not subscribe to any available topic in the system.");
                this.substub_map.remove(subscriberaddr);
            }
        } catch (Exception e) {
            System.err.println("We are sorry that its not possible to unsubscribe");
         
        }
    }

    @Override
    public List<Eventmessage> get_publishersnotify(Eventtype topiccontent) throws RemoteException {
        return this.message_type_map.get(topiccontent);
    }

  
}
    

