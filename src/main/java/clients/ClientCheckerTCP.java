package clients;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientCheckerTCP {

    public static void main(String[] args) {
        // Configuration
        String hote = "localhost";
        int port = 28414; // Le même port que le serveur

        Scanner sc = new Scanner(System.in);

        try {
            System.out.println("Client Checker");
            System.out.print("Login : ");
            String login = sc.nextLine();

            System.out.print("Password : ");
            String pass = sc.nextLine();

            // Connexion
            Socket sock = new Socket(hote, port);

            // Flux
            PrintStream out = new PrintStream(sock.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            // Envoi avec le protocole du serveur
            out.println(login);
            out.println(pass);

            // Réception
            String reponse = in.readLine();
            System.out.println("Réponse du serveur : " + reponse);

            // Fermeture de la connexion
            sock.close();

        } catch (IOException e) {
            System.out.println("Erreur client : " + e.getMessage());
        }
    }
}