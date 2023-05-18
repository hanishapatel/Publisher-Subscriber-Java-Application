package Shared;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class Basic {

        public static String[] connectionPort(int port) {
        List<String> portslist = new ArrayList<String>();
        Enumeration<NetworkInterface> networkinterf;
        try {
            networkinterf = NetworkInterface.getNetworkInterfaces();
         
            for (NetworkInterface networkinterface : Collections.list(networkinterf)) {
                Enumeration<InetAddress> inetAddresses = networkinterface.getInetAddresses();
                String ip_list = "";
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    String temp_ip = String.format("%s", inetAddress);
                    if (temp_ip.contains(":") || temp_ip.contains("127.0.0.1"))
                        continue;
                    portslist.add(temp_ip.substring(1) + ":"+ port);
                    ip_list += String.format("%23s\n\n", temp_ip.substring(1) + ":" + port);
                }
                if (!ip_list.equalsIgnoreCase("")) {
                    System.out.printf("IP-Addresses- ", networkinterface.getName());
                    System.out.print(ip_list);
                }
            }
           

        } catch (SocketException e) {
            System.out.println("ERROR EXCEPTION");
        }
        return portslist.toArray(new String[0]);
    }
    
}
