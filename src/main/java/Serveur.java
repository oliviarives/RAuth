import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {
    // Déclaration du ServerSocket
    ServerSocket sockEcoute;

    // Instanciation du ServerSocket en utilisant le constructeur
    // le plus simple (on précise le port)
    public void depart() throws IOException {
        // Étape 1
        sockEcoute = new ServerSocket(13214);

        // Etape 2

        // Declaration du socket de service
        Socket s;

        // on gère les clients SEQUENTIELLEMENT
        while (true) {
            try {
                s = sockEcoute.accept();
                // Exemple pour le mode texte
                // avec un simple reception/envoi du serveur

                try {
                    // positionner le flux entrant de la Socket
                    BufferedReader insocket = new BufferedReader (new InputStreamReader(
                            s.getInputStream()));

                    // positionner le flux de sortie de la Socket
                    PrintStream outsocket = new PrintStream(s.getOutputStream());

                    // Réception message de la socket
                    String messagerecu = insocket.readLine();

                    // Envoi message dans la socket
                    String messagenvoi = "toto";
                    outsocket.println(messagenvoi);
                }
                catch (IOException io) {
                    // Exception levée en cas d'erreur d'E/S
                    // ex : le client a fermé la connexion
                }

                // Clôture de communication avec un client
                s.close();

            }
            catch(IOException ioe) {
                System.out.println("Erreur de accept : " + ioe.getMessage());
                break;
            }
        }
    }
}
