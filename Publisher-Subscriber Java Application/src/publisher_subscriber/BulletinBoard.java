package publisher_subscriber;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.LinkedList;
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

public class BulletinBoard
{

    
    private static Map<String, Credentials> AUTH_MAP = new HashMap<String, Credentials>();

    public static void main(String[] args) throws IOException {

        System.out.print("\n\n");
        System.out.print("WELCOME TO THE BULLETIN BOARD\n");
        System.out.print("*------------------------------*\n\n");
        Scanner readdata = new Scanner(System.in);
        cred_data();
        choice choicemade;
        
        while (true) {
            try {

                System.out.print("Login System\n\n");
                System.out.print("Please enter your assign username: \n");
                String username = readdata.nextLine();
                System.out.print("Please enter your password or Exit: \n");
                String password = readdata.nextLine();
                if (password.toLowerCase().contains("exit"))
                    break;
                Credentials user_cred = AUTH_MAP.get(username);
                if (user_cred == null || !user_cred.passwordvalidation(password)) {
                    System.err.println("Credentials are not valid, Please check again....");
                    continue;
                }

                Logintype usercred = user_cred.get_loginType();
                if(usercred.equals(Logintype.pubsub))
                {
                    choicemade = new ManghaniP3broker(readdata);
                    choicemade.choices_call();
                    break;

                }

                else if(usercred.equals(Logintype.publisher))
                {
                    System.out.print("\n");
                    System.out.print("Welcome Publisher in the system\n");
                    System.out.print("*------------------------------*\n\n");
                    choicemade = new ManghaniP3Pub(readdata);
                    choicemade.choices_call();
                    break;

                }
                else if(usercred.equals(Logintype.subscriber))
                {
                    System.out.print("\n");
                    System.out.print("Welcome Subscriber in the System\n");
                    System.out.print("*------------------------------*\n\n");
                    choicemade = new ManghaniP3sub(readdata);
                    choicemade.choices_call();
                    break;

                }
            
            } catch (Exception e) {
                System.err.println("Exception in login system");
            }
        }
        readdata.close();

    }
    static void cred_data() {
        AUTH_MAP.put("pub1", new Credentials(Logintype.publisher, "pub1", "pub@123".toCharArray()));
        AUTH_MAP.put("pub2", new Credentials(Logintype.publisher, "pub2", "pub@123".toCharArray()));
        AUTH_MAP.put("pub3", new Credentials(Logintype.publisher, "pub3", "pub@123".toCharArray()));
        AUTH_MAP.put("pub4", new Credentials(Logintype.publisher, "pub4", "pub@123".toCharArray()));
        AUTH_MAP.put("pub5", new Credentials(Logintype.publisher, "pub5", "pub@123".toCharArray()));

        AUTH_MAP.put("sub1", new Credentials(Logintype.subscriber, "sub1", "sub@123".toCharArray()));
        AUTH_MAP.put("sub2", new Credentials(Logintype.subscriber, "sub2", "sub@123".toCharArray()));
        AUTH_MAP.put("sub3", new Credentials(Logintype.subscriber, "sub3", "sub@123".toCharArray()));
        AUTH_MAP.put("sub4", new Credentials(Logintype.subscriber, "sub4", "sub@123".toCharArray()));

        AUTH_MAP.put("board", new Credentials(Logintype.pubsub, "board", "board@123".toCharArray()));
    }
}
