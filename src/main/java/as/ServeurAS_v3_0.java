package as;

import cmd.CmdServ;
import protocoles.tcp.GestionDiscussion;          // Checker TCP (28414)
import protocoles.tcp.GestionDiscussionManager;   // Manager TCP (28415)
import protocoles.udp.ServiceCheckerUDP;          // Checker UDP (28414)

import java.io.IOException;
import java.net.*;

public class ServeurAS_v3_0 {

    private static final int PORT_CHECKER = 28414;
    private static final int PORT_MANAGER = 28415;

    public static void main(String[] args) {
        ListeAuth listeAuth = new ListeAuth();

        try {
            ServerSocket tcpCheckerSocket = new ServerSocket(PORT_CHECKER);
            DatagramSocket udpCheckerSocket = new DatagramSocket(PORT_CHECKER);
            ServerSocket tcpManagerSocket = new ServerSocket(PORT_MANAGER);

            System.out.println("Serveur AS v3.0 démarré");
            System.out.println(" - Checker TCP : port " + PORT_CHECKER);
            System.out.println(" - Checker UDP : port " + PORT_CHECKER);
            System.out.println(" - Manager TCP : port " + PORT_MANAGER);
            System.out.println(" - Logger L    : TCP 3244 (client intégré)");

            Thread tCheckerTCP = new Thread(() -> demarrerCheckerTCP(tcpCheckerSocket, listeAuth), "ASv3-Checker-TCP");
            Thread tCheckerUDP = new Thread(() -> demarrerCheckerUDP(udpCheckerSocket, listeAuth), "ASv3-Checker-UDP");
            Thread tManagerTCP = new Thread(() -> demarrerManagerTCP(tcpManagerSocket, listeAuth), "ASv3-Manager-TCP");

            tCheckerTCP.start();
            tCheckerUDP.start();
            tManagerTCP.start();

            tCheckerTCP.join();
            tCheckerUDP.join();
            tManagerTCP.join();

        } catch (BindException e) {
            System.out.println(CmdServ.ERROR.name() + " port déjà utilisé : " + e.getMessage());
        } catch (Exception e) {
            System.out.println(CmdServ.ERROR.name() + " ServeurAS_v3_0 : " + e.getMessage());
        }
    }

    private static void demarrerCheckerTCP(ServerSocket server, ListeAuth listeAuth) {
        try (ServerSocket s = server) {
            System.out.println("[AS v3.0] Checker TCP prêt");
            while (true) {
                Socket sock = s.accept();
                Thread t = new Thread(new GestionDiscussion(sock, listeAuth));
                t.start();
            }
        } catch (IOException e) {
            System.out.println(CmdServ.ERROR.name() + " Checker TCP : " + e.getMessage());
        }
    }

    private static void demarrerCheckerUDP(DatagramSocket socket, ListeAuth listeAuth) {
        try (DatagramSocket udp = socket) {
            System.out.println("[AS v3.0] Checker UDP prêt");

            ServiceCheckerUDP service = new ServiceCheckerUDP(udp, listeAuth);
            byte[] buffer = new byte[256];

            while (true) {
                DatagramPacket paquet = new DatagramPacket(buffer, buffer.length);
                udp.receive(paquet);
                service.traiter(paquet);
            }
        } catch (IOException e) {
            System.out.println(CmdServ.ERROR.name() + " Checker UDP : " + e.getMessage());
        }
    }

    private static void demarrerManagerTCP(ServerSocket server, ListeAuth listeAuth) {
        try (ServerSocket s = server) {
            System.out.println("[AS v3.0] Manager TCP prêt");
            while (true) {
                Socket sock = s.accept();
                Thread t = new Thread(new GestionDiscussionManager(sock, listeAuth));
                t.start();
            }
        } catch (IOException e) {
            System.out.println(CmdServ.ERROR.name() + " Manager TCP : " + e.getMessage());
        }
    }
}
