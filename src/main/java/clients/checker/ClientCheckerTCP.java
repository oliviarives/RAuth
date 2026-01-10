package clients.checker;

import logs.JsonLogger; // Q9

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientCheckerTCP {

    private static final String ROLE = "ClientChecker";
    private static final String HOTE = "localhost";
    private static final int PORT_CHECKER = 28414;

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("--- " + ROLE + " TCP ---");

            // Affichage interaction AVANT saisie
            System.out.println("// 1 - " + ROLE + " -> ServeurAS");

            System.out.print("Commande (ex: CHK) : ");
            String cmd = sc.nextLine();

            System.out.print("Login : ");
            String login = sc.nextLine();

            System.out.print("Password : ");
            String password = sc.nextLine();

            try (Socket socket = new Socket(HOTE, PORT_CHECKER);
                 PrintStream out = new PrintStream(socket.getOutputStream());
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // Envoi protocole (inchangé)
                out.println(cmd);
                out.println(login);
                out.println(password);

                // Réponse serveur
                String reponse = in.readLine();

                System.out.println("// 2 - ServeurAS -> " + ROLE);
                System.out.println(reponse);

                // =========================
                // Q9 (optionnel) : log côté client (n'affecte pas le protocole)
                // =========================
                try {
                    // host/port ici = infos du client (on met localhost par simplicité)
                    JsonLogger.log("localhost", 0, "TCP", cmd.trim().toUpperCase(), login.trim(), reponse);
                } catch (Exception ignored) {}
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
