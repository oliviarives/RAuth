package as;

import protocoles.tcp.ServiceCheckerTCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Partie TCP - Q2
public class ServeurAS_v1_b {

    public static void main(String[] args) {
        // Le port imposé par le sujet
        int port = 28414;

        // On implémente la logique métier qui est donc la
        // Préparation de la bd en mémoire
        ListeAuth listeAuth = new ListeAuth();

        // Déclaration du ServerSocket
        ServerSocket sockEcoute;

        try {
            // Étape 1 : Port 28414
            sockEcoute = new ServerSocket(port);
            System.out.println("Serveur AS (TCP) en attente sur le port " + port);

            // Étape 2
            Socket sockService;

            // On gère les clients SEQUENTIELLEMENT
            while (true) {
                try {
                    // BLOQUANT : on attend un client
                    sockService = sockEcoute.accept();
                    System.out.println("Client TCP connecté.");

                    // On appelle notre classe ServiceCheckerTCP
                    // Pour ne pas le traiter ici on utilise notre classe spécalisé
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
    }
}