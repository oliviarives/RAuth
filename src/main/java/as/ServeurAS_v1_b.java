package as;

import cmd.CmdServ;
import protocoles.tcp.GestionDiscussion;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurAS_v1_b {

    private static final int PORT = 28414;

    public static void main(String[] args) {
        ListeAuth listeAuth = new ListeAuth();

        try (ServerSocket sockEcoute = new ServerSocket(PORT)) {
            System.out.println("Serveur AS (TCP) en attente sur le port " + PORT);

            while (true) {
                Socket sockService = sockEcoute.accept();
                System.out.println("[TCP] Client connecté : " + sockService.getInetAddress());

                // 1 client = 1 thread = 1 discussion
                Thread t = new Thread(new GestionDiscussion(sockService, listeAuth));
                t.start();
            }

        } catch (IOException e) {
            System.out.println(CmdServ.ERROR.name() + " socket serveur : " + e.getMessage());
        }
    }
}
