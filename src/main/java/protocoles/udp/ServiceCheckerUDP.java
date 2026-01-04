package protocoles.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;

import as.ListeAuth;
import cmd.CmdServ;

public class ServiceCheckerUDP {

    private DatagramSocket socket;
    private ListeAuth listeAuth;

    public ServiceCheckerUDP(DatagramSocket socket, ListeAuth listeAuth) {
        this.socket = socket;
        this.listeAuth = listeAuth;
    }

    public void traiter(DatagramPacket paquetRecu) {
        try {
            // --- ETAPE 1 : EXTRACTION DES DONNEES (Comme le cours) ---
            // Attention : on utilise bien getLength() pour ne lire que les octets utiles
            // et pas tout le buffer de 1024 cases.
            String message = new String(paquetRecu.getData(), 0, paquetRecu.getLength());

            // --- ETAPE 2 : LOGIQUE METIER ---
            // On attend le format "login;password"
            String[] morceaux = message.split(";");
            String reponseStr = CmdServ.BAD.name(); // Par défaut on dit non

            if (morceaux.length == 2) {
                String login = morceaux[0];
                String passwd = morceaux[1];

                if (listeAuth.tester(login, passwd)) {
                    reponseStr = CmdServ.GOOD.name();
                }
            }

            // --- ETAPE 3 : PREPARATION DE LA REPONSE ---
            // On récupère l'adresse et le port de l'expéditeur (le client)
            // C'est indispensable en UDP car il n'y a pas de connexion permanente
            InetAddress adresseClient = paquetRecu.getAddress();
            int portClient = paquetRecu.getPort();

            // Conversion en octets
            byte[] dataReponse = reponseStr.getBytes();

            // Construction du paquet réponse
            DatagramPacket paquetReponse = new DatagramPacket(
                    dataReponse,
                    dataReponse.length,
                    adresseClient,
                    portClient
            );

            // --- ETAPE 4 : ENVOI ---
            socket.send(paquetReponse);
            System.out.println("[UDP] Réponse " + reponseStr + " envoyée à " + adresseClient);

        } catch (IOException e) {
            System.out.println(CmdServ.ERROR.name() + " Service UDP : " + e.getMessage());
        }
    }
}