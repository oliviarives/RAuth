package clients.checker;

import java.net.*;
import java.util.Scanner;
import cmd.CmdServ;

public class ClientCheckerUDP {

    public static void main(String[] args) {
        String hote = "localhost";
        int port = 28414;
        Scanner sc = new Scanner(System.in);

        System.out.println("--- Client UDP Checker ---");

        try {
            DatagramSocket socket = new DatagramSocket();

            System.out.println("// 1 - Client -> Serveur ");
            String message = sc.nextLine();

            byte[] data = message.getBytes();

            // Envoi du paquet
            InetAddress adresseServeur = InetAddress.getByName(hote);
            DatagramPacket paquetEnvoi = new DatagramPacket(data, data.length, adresseServeur, port);
            socket.send(paquetEnvoi);

            // Attente de la réponse
            byte[] bufferReponse = new byte[256];
            DatagramPacket paquetRecu = new DatagramPacket(bufferReponse, bufferReponse.length);

            socket.receive(paquetRecu);

            // Analyse de la réponse
            String reponseTexte = new String(paquetRecu.getData(), 0, paquetRecu.getLength());

            System.out.println("// 2- Serveur -> Client ");
            if (CmdServ.GOOD.name().equals(reponseTexte)) {
                System.out.println(CmdServ.GOOD.name());
            } else {
                System.out.println(CmdServ.BAD.name());
            }

            socket.close();
            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}