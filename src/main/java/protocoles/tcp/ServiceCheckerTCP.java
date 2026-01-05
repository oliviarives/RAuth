package protocoles.tcp;

import as.ListeAuth;
import cmd.CmdServ;

import java.io.*;
import java.net.Socket;

public class ServiceCheckerTCP {

    private Socket sockService;
    private ListeAuth listeAuth;

    public ServiceCheckerTCP(Socket sockService, ListeAuth listeAuth) {
        this.sockService = sockService;
        this.listeAuth = listeAuth;
    }

    // Logique de l'échange soit le protocole
    public void traiter() {
        try {
            // Positionner le flux entrant de la Socket
            BufferedReader insocket = new BufferedReader(new InputStreamReader(
                    sockService.getInputStream()));

            // Positionner le flux de sortie de la Socket
            PrintStream outsocket = new PrintStream(sockService.getOutputStream());

            // PROTOCOLE D'AUTHENTIFICATION
            // Lecture Login envoyé par le client
            String login = insocket.readLine();

            // Lecture Password envoyé par le client
            String password = insocket.readLine();

            // Vérification par la classe ListeAuth
            // L'énoncé dit : "Le rôle d’un serveur AS limité à la seule opération de vérification"
            boolean resultat = listeAuth.tester(login, password);

            // Retour de la réponse au client
            if (resultat) {
                // La méthode name() permet de cast notre enum en String
                outsocket.println(CmdServ.GOOD.name());
            } else {
                outsocket.println(CmdServ.BAD.name());
            }

            // Clôture de communication avec ce client TCP
            sockService.close();

        } catch (IOException io) {
            System.out.println(CmdServ.ERROR.name() + " Service TCP : " + io.getMessage());
        }
    }
}