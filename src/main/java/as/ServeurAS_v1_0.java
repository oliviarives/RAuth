package as;

import cmd.CmdServ;
import protocoles.tcp.GestionDiscussion;
import protocoles.udp.ServiceCheckerUDP;

import java.io.IOException;
import java.net.*;

public class ServeurAS_v1_0 {

    private static final int PORT = 28414;

    public static void main(String[] args) {
        ListeAuth listeAuth = new ListeAuth();

        try {
            // On ouvre les deux sockets sur le même port, mais sur deux protocoles différents
            ServerSocket tcpServerSocket = new ServerSocket(PORT);
            DatagramSocket udpSocket = new DatagramSocket(PORT);

            System.out.println("Serveur AS (MULTI TCP+UDP) en attente sur le port " + PORT);

            // Thread TCP
            Thread tcpThread = new Thread(() -> demarrerTCP(tcpServerSocket, listeAuth));
            tcpThread.start();

            // Thread UDP
            Thread udpThread = new Thread(() -> demarrerUDP(udpSocket, listeAuth));
            udpThread.start();

            // Option : empêcher main de se terminer
            // (les threads tournent, mais c'est plus propre de join)
            tcpThread.join();
            udpThread.join();

        } catch (BindException e) {
            System.out.println(CmdServ.ERROR.name() + " Port déjà utilisé : " + e.getMessage());
        } catch (Exception e) {
            System.out.println(CmdServ.ERROR.name() + " ServeurAS_v1_0 : " + e.getMessage());
        }
    }

    private static void demarrerTCP(ServerSocket sockEcoute, ListeAuth listeAuth) {
        try (ServerSocket server = sockEcoute) {
            System.out.println("[TCP] Serveur prêt");

            while (true) {
                Socket sockService = server.accept();
                System.out.println("[TCP] Client connecté : " + sockService.getInetAddress());

                Thread t = new Thread(new GestionDiscussion(sockService, listeAuth));
                t.start();
            }

        } catch (IOException e) {
            System.out.println(CmdServ.ERROR.name() + " TCP : " + e.getMessage());
        }
    }

    private static void demarrerUDP(DatagramSocket sockEcoute, ListeAuth listeAuth) {
        try (DatagramSocket udp = sockEcoute) {
            System.out.println("[UDP] Serveur prêt");

            // Instancier une fois
            ServiceCheckerUDP service = new ServiceCheckerUDP(udp, listeAuth);

            byte[] buffer = new byte[256];

            while (true) {
                DatagramPacket paquetRecu = new DatagramPacket(buffer, buffer.length);
                udp.receive(paquetRecu);
                service.traiter(paquetRecu);
            }

        } catch (IOException e) {
            System.out.println(CmdServ.ERROR.name() + " UDP : " + e.getMessage());
        }
    }
}
