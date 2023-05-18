package Shared;
import java.net.SocketException;
import java.io.Serializable;
import java.util.Scanner;
import java.util.List;

public abstract class choice {
    
    public Scanner scan;
    public String name;

    public choice(Scanner scan) {
        this.scan = scan;
        
    }

    public abstract void choices_call() throws SocketException;
}
