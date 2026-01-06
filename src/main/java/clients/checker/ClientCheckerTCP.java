package clients.checker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientCheckerTCP {

    private static final String ROLE = "ClientChecker";

    public static void main(String[] args) {
        String hote = "localhost";
        int port = 28414;

        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("--- " + ROLE + " TCP ---");

            // Affichage avant la saisie
            System.out.println("// 1 - Client -> Serveur ");

            // Saisie
            System.out.print("Commande : ");
            String cmd = sc.nextLine();

            System.out.print("Login : ");
            String login = sc.nextLine();

            System.out.print("Password : ");
            String password = sc.nextLine();

            // Connexion + envoi
            try (Socket socket = new Socket(hote, port);
                 PrintStream out = new PrintStream(socket.getOutputStream());
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                out.println(cmd);
                out.println(login);
                out.println(password);

                // Réception + affichage
                String reponse = in.readLine();

                System.out.println("// 2- Serveur -> Client ");
                System.out.println(reponse); // GOOD / BAD / ERROR
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
