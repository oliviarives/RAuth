package logs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurLog {

    private static final int PORT_LOG = 3244;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT_LOG)) {
            System.out.println("Serveur L (LOG) en attente sur le port " + PORT_LOG);

            while (true) {
                Socket client = server.accept();

                // 1 thread par connexion de log (simple et suffisant)
                Thread t = new Thread(() -> traiterClient(client));
                t.start();
            }

        } catch (IOException e) {
            System.out.println("ERROR ServeurLog : " + e.getMessage());
        }
    }

    private static void traiterClient(Socket client) {
        try (Socket s = client;
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {

            // On lit toutes les lignes envoyées (souvent 1 seule)
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("[LOG] " + line);
            }

        } catch (IOException e) {
            System.out.println("ERROR ServeurLog-client : " + e.getMessage());
        }
    }
}
