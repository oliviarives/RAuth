import java.io.IOException;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        // Déclaration du socket
        Socket sock;

        try {
            // Instanciation du socket en précisant le nom de machine et le port
            sock = new Socket("localhost", 13214);

            // autres solutions :
            // sock = new Socket("10.5.4.1", 13214);
            // sock = new Socket("2001:cdba::3257:9652", 13214);

            // Positionner les flux d'E/S
            // Envoi réception de messages

            // Fermeture du socket

        } catch(IOException ioe) {

            System.out.println("Erreur de création ou de connexion : " + ioe.getMessage());

            return;

        }
    }
}
