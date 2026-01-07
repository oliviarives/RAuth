package protocoles.tcp;

import as.ListeAuth;
import cmd.CmdServ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class GestionDiscussionManager implements Runnable {

    private final Socket sockService;
    private final ListeAuth listeAuth;

    public GestionDiscussionManager(Socket sockService, ListeAuth listeAuth) {
        this.sockService = sockService;
        this.listeAuth = listeAuth;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(sockService.getInputStream()));
                PrintStream out = new PrintStream(sockService.getOutputStream())
        ) {
            while (true) {
                // 1) Commande
                String cmd = in.readLine();
                if (cmd == null) return; // client ferme

                cmd = cmd.trim().toUpperCase();

                if (!cmd.equals("CHK") && !cmd.equals("ADD") && !cmd.equals("DEL") && !cmd.equals("MOD")) {
                    out.println(CmdServ.ERROR.name() + " commande inconnue");
                    continue;
                }

                out.println("ASK_LOGIN");

                // 2) Login
                String login = in.readLine();
                if (login == null) return;
                login = login.trim();

                // Validation "existence" selon la commande
                boolean existe = listeAuth.existe(login);

                if (cmd.equals("ADD")) {
                    if (existe) {
                        out.println(CmdServ.ERROR.name() + " login deja existant");
                        continue; // on ne demande pas le password
                    }
                } else { // CHK, DEL, MOD
                    if (!existe) {
                        out.println(CmdServ.ERROR.name() + " login inexistant");
                        continue; // on ne demande pas le password
                    }
                }

                out.println("ASK_PASSWORD");

                // Password
                String password = in.readLine();
                if (password == null) return;
                password = password.trim();

                switch (cmd) {
                    case "CHK": {
                        boolean ok = listeAuth.tester(login, password);
                        out.println(ok ? CmdServ.GOOD.name() : CmdServ.BAD.name());
                        break;
                    }
                    case "ADD": {
                        boolean ok = listeAuth.creer(login, password);
                        out.println(ok ? CmdServ.DONE.name() : CmdServ.BAD.name());
                        break;
                    }
                    case "DEL": {
                        // La suppression nécessite le bon password
                        boolean ok = listeAuth.supprimer(login, password);
                        out.println(ok ? CmdServ.DONE.name() : CmdServ.BAD.name());
                        break;
                    }
                    case "MOD": {
                        // Ici password = nouveau mot de passe
                        boolean ok = listeAuth.mettreAJour(login, password);
                        out.println(ok ? CmdServ.DONE.name() : CmdServ.BAD.name());
                        break;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println(CmdServ.ERROR.name() + " GestionDiscussionManager : " + e.getMessage());
        } finally {
            try { sockService.close(); } catch (IOException ignored) {}
        }
    }
}
