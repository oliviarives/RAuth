package clients.checker;

import java.net.*;
import java.util.Scanner;
import cmd.CmdServ; // Import de l'enum pour comparer

public class ClientCheckerUDP {

    public static void main(String[] args) {
        String hote = "localhost";
        int port = 28414;
        Scanner sc = new Scanner(System.in);

        System.out.println("--- Client UDP Checker ---");

        try {
            DatagramSocket socket = new DatagramSocket();

            System.out.print("Login : ");
            String login = sc.nextLine();
            System.out.print("Password : ");
            String pass = sc.nextLine();

            // Concaténation du login et du password avec un ';' pour effectuer
            // La séparation dans la classe ServerCheckerUDP
            String message = login + ";" + pass;
            byte[] data = message.getBytes();

            // Envoi du paquet
            InetAddress adresseServeur = InetAddress.getByName(hote);
            DatagramPacket paquetEnvoi = new DatagramPacket(data, data.length, adresseServeur, port);
            socket.send(paquetEnvoi);

            // Attente de la réponse
            byte[] bufferReponse = new byte[1024];
            DatagramPacket paquetRecu = new DatagramPacket(bufferReponse, bufferReponse.length);

            socket.receive(paquetRecu);

            // Analyse de la réponse
            String reponseTexte = new String(paquetRecu.getData(), 0, paquetRecu.getLength());

            if (CmdServ.GOOD.name().equals(reponseTexte)) {
                System.out.println("SERVEUR UDP : " + CmdServ.GOOD.name());
            } else {
                System.out.println("SERVEUR UDP : "+ CmdServ.BAD.name());
            }

            socket.close();
            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}