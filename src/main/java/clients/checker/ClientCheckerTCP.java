package clients.checker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientCheckerTCP {

    private static final String ROLE = "ClientChecker"; // utile pour réutiliser le même style plus tard

    public static void main(String[] args) {
        String hote = "localhost";
        int port = 28414;

        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("--- " + ROLE + " TCP ---");

            // 1) Affichage AVANT la saisie
            System.out.println("// 1 - " + ROLE + " -> ServeurAS");

            // 2) Saisie "formulaire"
            System.out.print("Commande (ex: CHK) : ");
            String cmd = sc.nextLine();

            System.out.print("Login : ");
            String login = sc.nextLine();

            System.out.print("Password : ");
            String password = sc.nextLine();

            // 3) Connexion + envoi
            try (Socket socket = new Socket(hote, port);
                 PrintStream out = new PrintStream(socket.getOutputStream());
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                out.println(cmd);
                out.println(login);
                out.println(password);

                // 4) Réception + affichage
                String reponse = in.readLine();

                System.out.println("// 2 - ServeurAS -> " + ROLE);
                System.out.println(reponse); // UNIQUEMENT GOOD / BAD / ERROR
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
