package clients.checker;

import cmd.CmdServ;
import logs.JsonLogger; // Q9 (optionnel)

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientCheckerUDP {

    private static final String ROLE = "ClientChecker";
    private static final String HOTE = "localhost";
    private static final int PORT = 28414;

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in);
             DatagramSocket socket = new DatagramSocket()) {

            System.out.println("--- " + ROLE + " UDP ---");

            // Affichage avant saisie
            System.out.println("// 1 - " + ROLE + " -> ServeurAS");
            System.out.println("Format attendu: CHK <login> <password>");
            System.out.print("> ");

            // On garde votre logique : 1 datagramme = cmd + login + password
            String message = sc.nextLine().trim();

            byte[] data = message.getBytes();

            InetAddress adresseServeur = InetAddress.getByName(HOTE);
            DatagramPacket paquetEnvoi = new DatagramPacket(data, data.length, adresseServeur, PORT);
            socket.send(paquetEnvoi);

            // Réception de la réponse
            byte[] bufferReponse = new byte[256];
            DatagramPacket paquetRecu = new DatagramPacket(bufferReponse, bufferReponse.length);
            socket.receive(paquetRecu);

            String reponseTexte = new String(paquetRecu.getData(), 0, paquetRecu.getLength()).trim();

            System.out.println("// 2 - ServeurAS -> " + ROLE);
            // On affiche la réponse serveur telle quelle (GOOD/BAD/ERROR...)
            System.out.println(reponseTexte);

            // =========================
            // Q9 (optionnel) : log côté client (ne change rien au protocole)
            // =========================
            try {
                // On essaye de récupérer cmd/login sans imposer quoi que ce soit
                String[] parts = message.replaceAll("\\s+", " ").split(" ");
                String type = (parts.length >= 1) ? parts[0].toUpperCase() : "CHK";
                String login = (parts.length >= 2) ? parts[1] : "-";

                JsonLogger.log("localhost", 0, "UDP", type, login, reponseTexte);
            } catch (Exception ignored) {}

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
