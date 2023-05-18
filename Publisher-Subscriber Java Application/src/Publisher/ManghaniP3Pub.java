package Publisher;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import Shared.Basic;
import Shared.Brokerinterface;
import Shared.Eventmessage;
import Shared.Eventtype;
import Shared.ManghaniP3SubscriberRMIinterface;
import Shared.choice;

public class ManghaniP3Pub extends choice{

    private Eventtype topiccontent;
    private Brokerinterface brokerservice;

    public ManghaniP3Pub(Scanner s) {
        super(s);

        while (this.topiccontent == null) {
            try {
                int i = 1;
                System.out.println("Here is the list of topics :-");
                for (Eventtype eventtypes : Eventtype.values()) {
                   
                    System.out.println("  "+(i++) +"." + eventtypes);
                }
                System.out.print("Please select the Topic of publication: ");
                int choosedtopic = Integer.parseInt(this.scan.nextLine());
                this.topiccontent = Eventtype.values()[choosedtopic-1];
            } catch (Exception e) {
                System.err.println("Wrong choice made, Please try again...............");
            }


             while (this.brokerservice == null) 
             {
                try {
                    System.out.print("Please Enter the IP add of bulletin board for connection\n");
                    String server_ip = s.nextLine();
                    Registry registry = LocateRegistry.getRegistry(server_ip.split(":")[0],
                            Integer.parseInt(server_ip.split(":")[1]));
                    System.out.println("\nSuccesfully Connected to the Bulletin Board\n");

                
                    this.brokerservice = (Brokerinterface) registry.lookup("BulletinboardRegistry");
                } catch (Exception e) {
               
                    System.err.println(" Exception....");
                }
            }
             
        }
    }

  
    @Override
    public void choices_call() {
        while (true) {
            try {

                System.out.print("Enter your text message for publishing:- ");
                String publishedtext = this.scan.nextLine();
                this.brokerservice.publish(new Eventmessage(this.topiccontent, publishedtext));
                
            } catch (Exception e) {
                System.err.println("Wrong selection, please try again....");
            }
        }
    }

 

}

