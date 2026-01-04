package as;

import cmd.CmdServ;
import protocoles.tcp.ServiceCheckerTCP;
import protocoles.udp.ServiceCheckerUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurAS {

    public static void main(String[] args) {
        // On implémente la logique métier qui est donc la
        // Préparation de la bd en mémoire
        ListeAuth listeAuth = new ListeAuth();

        //Partie TCP - Q2
        // Déclaration du ServerSocket
        /*ServerSocket sockEcoute;

        try {
            // Étape 1 : Port 28414 (de la Q2)
            sockEcoute = new ServerSocket(28414);
            System.out.println("Serveur AS (TCP) en attente sur 28414...");

            // Étape 2
            Socket sockService;

            // On gère les clients SEQUENTIELLEMENT
            while (true) {
                try {
                    // BLOQUANT : on attend un client
                    sockService = sockEcoute.accept();
                    System.out.println("Client TCP connecté.");

                    // On appelle notre classe ServiceCheckerTCP
                    // Pour ne pas traiter le traiter ici. Uniquement dans notre classe spécalisé
                    ServiceCheckerTCP service = new ServiceCheckerTCP(sockService, listeAuth);
                    service.traiter();

                } catch (IOException ioe) {
                    System.out.println("Erreur de accept : " + ioe.getMessage());
                    break;
                }
            }
        } catch (IOException ioe) {
            System.out.println("Erreur de création du server socket: " + ioe.getMessage());
        }
    }*/

        //Partie UDP - Q3
        // Le port imposé par le sujet
        int port = 28414;

        // Déclaration du socket datagramme
        DatagramSocket sockEcoute = null;

        try {
            // Création du socket lié au port 28414
            sockEcoute = new DatagramSocket(port);
            System.out.println("Serveur AS (UDP) en attente sur le port " + port);

            // Préparation du tampon (buffer) pour recevoir les données
            // Le cours utilise new byte[256], on met 1024 pour être large
            byte[] buffer = new byte[1024];

            while (true) {
                // Création d'un paquet vide prêt à être rempli
                DatagramPacket paquetRecu = new DatagramPacket(buffer, buffer.length);

                // Attente bloquante d'un message
                sockEcoute.receive(paquetRecu);

                // Dès qu'on reçoit quelque chose, on délègue au Service
                ServiceCheckerUDP service = new ServiceCheckerUDP(sockEcoute, listeAuth);
                service.traiter(paquetRecu);
            }

        } catch (IOException e) {
            System.out.println(CmdServ.ERROR.name() + " socket serveur : " + e.getMessage());
        } finally {
            if (sockEcoute != null) sockEcoute.close();
        }
    }
}