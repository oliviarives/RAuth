import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        Serveur serv = new Serveur();
        try {
            serv.depart();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}