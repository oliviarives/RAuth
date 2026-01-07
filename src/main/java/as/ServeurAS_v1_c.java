package as;

import cmd.CmdServ;
import protocoles.tcp.GestionDiscussionManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurAS_v1_c {

    private static final int PORT_MANAGER = 28415;

    public static void main(String[] args) {
        ListeAuth listeAuth = new ListeAuth();

        try (ServerSocket sockEcoute = new ServerSocket(PORT_MANAGER)) {
            System.out.println("Serveur AS (TCP Manager) en attente sur le port " + PORT_MANAGER);

            while (true) {
                Socket sockService = sockEcoute.accept();
                System.out.println("[TCP-MANAGER] Client connecté : " + sockService.getInetAddress());

                Thread t = new Thread(new GestionDiscussionManager(sockService, listeAuth));
                t.start();
            }

        } catch (IOException e) {
            System.out.println(CmdServ.ERROR.name() + " ServeurAS_v1_c : " + e.getMessage());
        }
    }
}
