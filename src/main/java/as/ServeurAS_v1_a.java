package as;

import cmd.CmdServ;
import protocoles.udp.ServiceCheckerUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

//Partie UDP - Q3
public class ServeurAS_v1_a {

    public static void main(String[] args) {
        // Le port imposé par le sujet
        int port = 28414;

        // On implémente la logique métier qui est donc la
        // Préparation de la bd en mémoire
        ListeAuth listeAuth = new ListeAuth();

        // Déclaration du socket datagramme
        DatagramSocket sockEcoute = null;

        try {
            // Création du socket lié au port 28414
            sockEcoute = new DatagramSocket(port);
            System.out.println("Serveur AS (UDP) en attente sur le port " + port);

            // Préparation du tampon (buffer) pour recevoir les données
            byte[] buffer = new byte[256];

            while (true) {
                // On créé un paquet vide prêt à être rempli
                DatagramPacket paquetRecu = new DatagramPacket(buffer, buffer.length);

                // BLOQUANT : on attend un message
                sockEcoute.receive(paquetRecu);

                // On appelle notre classe spécialisé ServiceCheckerUDP
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