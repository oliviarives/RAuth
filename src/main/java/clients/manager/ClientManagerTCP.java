package clients.manager;

import cmd.MenuCmd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientManagerTCP {

    private static final String ROLE = "ClientManager";
    private static final String HOTE = "localhost";
    private static final int PORT_MANAGER = 28415;

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in);
             Socket socket = new Socket(HOTE, PORT_MANAGER);
             PrintStream out = new PrintStream(socket.getOutputStream());
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("--- " + ROLE + " TCP ---");
            System.out.println("Connexion au serveur sur " + HOTE + ":" + PORT_MANAGER);
            System.out.println();

            MenuCmd.afficherMenuManager();

            while (true) {
                // Affichage interaction AVANT saisie
                System.out.println("// 1 - " + ROLE + " -> ServeurAS");

                System.out.print("Commande : ");
                String cmd = sc.nextLine().trim();

                if (cmd.isEmpty()) {
                    System.out.println();
                    continue;
                }

                if ("HELP".equalsIgnoreCase(cmd)) {
                    System.out.println();
                    MenuCmd.afficherMenuManager();
                    continue;
                }

                if ("QUIT".equalsIgnoreCase(cmd)) {
                    System.out.println("Fin " + ROLE + ".");
                    return;
                }

                // 1) Envoi CMD
                out.println(cmd);

                // 2) Réponse serveur : ASK_LOGIN ou ERROR ...
                String rep1 = in.readLine();
                if (rep1 == null) {
                    System.out.println("Connexion fermée par le serveur.");
                    return;
                }

                if (rep1.startsWith("ERROR")) {
                    System.out.println("// 2 - ServeurAS -> " + ROLE);
                    System.out.println(rep1);
                    System.out.println();
                    continue;
                }

                if (!"ASK_LOGIN".equalsIgnoreCase(rep1.trim())) {
                    System.out.println("// 2 - ServeurAS -> " + ROLE);
                    System.out.println("ERROR protocole inattendu (attendu ASK_LOGIN, reçu: " + rep1 + ")");
                    System.out.println();
                    continue;
                }

                // Saisie login
                System.out.print("Login : ");
                String login = sc.nextLine();
                out.println(login);

                // Réponse serveur : ASK_PASSWORD ou ERROR ...
                String rep2 = in.readLine();
                if (rep2 == null) {
                    System.out.println("Connexion fermée par le serveur.");
                    return;
                }

                if (rep2.startsWith("ERROR")) {
                    System.out.println("// 2 - ServeurAS -> " + ROLE);
                    System.out.println(rep2);
                    System.out.println();
                    continue;
                }

                if (!"ASK_PASSWORD".equalsIgnoreCase(rep2.trim())) {
                    System.out.println("// 2 - ServeurAS -> " + ROLE);
                    System.out.println("ERROR protocole inattendu (attendu ASK_PASSWORD, reçu: " + rep2 + ")");
                    System.out.println();
                    continue;
                }

                // Saisie password (ou newPassword si MOD)
                System.out.print("Password (newPassword si MOD) : ");
                String password = sc.nextLine();
                out.println(password);

                // Réponse finale du serveur : GOOD/BAD/DONE/ERROR...
                String repFinale = in.readLine();
                if (repFinale == null) {
                    System.out.println("Connexion fermée par le serveur.");
                    return;
                }

                System.out.println("// 2 - ServeurAS -> " + ROLE);
                System.out.println(repFinale);
                System.out.println();

            }

        } catch (Exception e) {
            System.out.println("Erreur " + ROLE + " : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
