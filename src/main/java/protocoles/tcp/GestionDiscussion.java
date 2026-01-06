package protocoles.tcp;

import as.ListeAuth;
import cmd.CmdServ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class GestionDiscussion implements Runnable {

    private final Socket sockService;
    private final ListeAuth listeAuth;

    public GestionDiscussion(Socket sockService, ListeAuth listeAuth) {
        this.sockService = sockService;
        this.listeAuth = listeAuth;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(sockService.getInputStream()));
                PrintStream out = new PrintStream(sockService.getOutputStream())
        ) {
            // Protocole TCP v1 : 3 lignes
            // 1) cmd
            // 2) login
            // 3) password
            String cmd = in.readLine();
            String login = in.readLine();
            String password = in.readLine();

            if (cmd == null || login == null || password == null) {
                out.println(CmdServ.ERROR.name());
                return;
            }

            cmd = cmd.trim().toUpperCase();
            login = login.trim();
            password = password.trim();

            // Pour l'instant : uniquement CHK
            if (!"CHK".equals(cmd)) {
                out.println(CmdServ.ERROR.name());
                return;
            }

            boolean ok = listeAuth.tester(login, password);
            out.println(ok ? CmdServ.GOOD.name() : CmdServ.BAD.name());

        } catch (IOException e) {
            System.out.println(CmdServ.ERROR.name() + " GestionDiscussion : " + e.getMessage());
        } finally {
            try { sockService.close(); } catch (IOException ignored) {}
        }
    }
}
