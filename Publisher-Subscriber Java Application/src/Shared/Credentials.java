package Shared;

public class Credentials {

    public enum Logintype {
        pubsub,
        publisher,
        subscriber
    }
    private Logintype logintype;
    private String username;
    private char[] password;

    public Credentials(Logintype nodeType, String username, char[] password) {
        this.username = username;
        this.password = password;
        this.logintype = nodeType;
    }

    public boolean passwordvalidation(String toMatch) {
        return (toMatch.equals(new String(this.password)));
    }

    public String get_username() {
        return username;
    }

    public Logintype get_loginType() {
        return logintype;
    }


    
    
}
