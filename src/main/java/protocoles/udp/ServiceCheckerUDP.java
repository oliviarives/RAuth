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
            String message = new String(paquetRecu.getData(), 0, paquetRecu.getLength());

            // On découpe le message en 3 différents morceaux
            // Morceaux 1 : Commande Serveur
            // Morceaux 2 : login
            // Morceaux 3 : mdp
            String[] morceaux = message.split(" ");

            String reponseStr;

            // On vérifie que :
            // - Le nombre d'arguments' est égale à 3
            // - la commande est bien égale à CHK
            if (morceaux.length == 3 && morceaux[0].equals("CHK")) {
                String login = morceaux[1];
                String mdp = morceaux[2];

                // Vérification du couple login / mdp
                if (listeAuth.tester(login, mdp)) {
                    reponseStr = CmdServ.GOOD.name();
                } else {
                    reponseStr = CmdServ.BAD.name();
                }
            } else {
                reponseStr = CmdServ.ERROR.name();
            }

            // Préparation de la réponse
            InetAddress adresseClient = paquetRecu.getAddress();
            int portClient = paquetRecu.getPort();
            byte[] dataReponse = reponseStr.getBytes();

            DatagramPacket paquetReponse = new DatagramPacket(
                    dataReponse,
                    dataReponse.length,
                    adresseClient,
                    portClient
            );

            socket.send(paquetReponse);
            // historique du serveur des messages reçus avec la réponse asssociée
            System.out.println("[UDP] Reçu : " + message + " -> Réponse : " + reponseStr);

        } catch (IOException e) {
            // Message console client
            System.out.println(CmdServ.ERROR.name() + " Service UDP : " + e.getMessage());
        }
    }
}