package clients.manager;

import cmd.MenuCmd;
import logs.JsonLogger; // Q9

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

                // Envoi CMD
                out.println(cmd);

                String rep1 = in.readLine(); // ASK_LOGIN ou ERROR ...
                if (rep1 == null) return;

                if (rep1.startsWith("ERROR")) {
                    System.out.println("// 2 - ServeurAS -> " + ROLE);
                    System.out.println(rep1);
                    System.out.println();

                    // Q9 (optionnel) : log côté client (pas de login)
                    try { JsonLogger.log("localhost", 0, "TCP", cmd.toUpperCase(), "-", rep1); } catch (Exception ignored) {}
                    continue;
                }

                if (!"ASK_LOGIN".equalsIgnoreCase(rep1.trim())) {
                    String err = "ERROR protocole inattendu (attendu ASK_LOGIN, reçu: " + rep1 + ")";
                    System.out.println("// 2 - ServeurAS -> " + ROLE);
                    System.out.println(err);
                    System.out.println();
                    try { JsonLogger.log("localhost", 0, "TCP", cmd.toUpperCase(), "-", err); } catch (Exception ignored) {}
                    continue;
                }

                System.out.print("Login : ");
                String login = sc.nextLine().trim();
                out.println(login);

                String rep2 = in.readLine(); // ASK_PASSWORD ou ERROR ...
                if (rep2 == null) return;

                if (rep2.startsWith("ERROR")) {
                    System.out.println("// 2 - ServeurAS -> " + ROLE);
                    System.out.println(rep2);
                    System.out.println();

                    // Q9 (optionnel)
                    try { JsonLogger.log("localhost", 0, "TCP", cmd.toUpperCase(), login, rep2); } catch (Exception ignored) {}
                    continue;
                }

                if (!"ASK_PASSWORD".equalsIgnoreCase(rep2.trim())) {
                    String err = "ERROR protocole inattendu (attendu ASK_PASSWORD, reçu: " + rep2 + ")";
                    System.out.println("// 2 - ServeurAS -> " + ROLE);
                    System.out.println(err);
                    System.out.println();
                    try { JsonLogger.log("localhost", 0, "TCP", cmd.toUpperCase(), login, err); } catch (Exception ignored) {}
                    continue;
                }

                System.out.print("Password (newPassword si MOD) : ");
                String password = sc.nextLine();
                out.println(password);

                String repFinale = in.readLine(); // GOOD/BAD/DONE/ERROR...
                if (repFinale == null) return;

                System.out.println("// 2 - ServeurAS -> " + ROLE);
                System.out.println(repFinale);
                System.out.println();

                // =========================
                // Q9 (optionnel) : log côté client (n'affecte pas le protocole)
                // =========================
                try {
                    JsonLogger.log("localhost", 0, "TCP", cmd.toUpperCase(), login, repFinale);
                } catch (Exception ignored) {}
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
