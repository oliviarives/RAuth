package logs;

import java.util.Date;
import java.io.PrintWriter;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Classe Singleton qui permet de logger des requêtes vers un serveur de log sur le port 3244 de la machine locale
 *
 * @author torguet
 *
 */
public class JsonLogger {

    // Attributs à compléter
    private static final String LOG_HOST = "localhost";
    private static final int LOG_PORT = 3244;

    /**
     * Constructeur à compléter
     */
    private JsonLogger() {

    }

    /**
     * Transforme une requête en Json
     *
     * @param host machine client
     * @param port port sur la machine client
     * @param proto protocole de transport utilisé
     * @param type type de la requête
     * @param login login utilisé
     * @param result résultat de l'opération
     * @return un objet Json correspondant à la requête
     */
    private JsonObject reqToJson(String host, int port, String proto, String type, String login, String result) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("host", host)
                .add("port", port)
                .add("proto", proto)
                .add("type", type)
                .add("login", login)
                .add("result", result)
                .add("date", new Date().toString());

        return builder.build();
    }

    /**
     *  singleton
     */
    private static JsonLogger logger = null;

    /**
     * récupération du logger qui est créé si nécessaire
     *
     * @return le logger
     */
    private static JsonLogger getLogger() {
        if (logger == null) {
            logger = new JsonLogger();
        }
        return logger;
    }

    /**
     * méthode pour logger
     *
     * @param host machine client
     * @param port port sur la machine client
     * @param proto protocole de transport utilisé
     * @param type type de la requête
     * @param login login utilisé
     * @param result résultat de l'opération
     */
    public static void log(String host, int port, String proto, String type, String login, String result) {
        JsonLogger logger = getLogger();
        // à compléter
        JsonObject json = logger.reqToJson(host, port, proto, type, login, result);

        // Connexion au serveur de log (machine locale, port 3244) et envoi du message JSON
        try (Socket socket = new Socket(LOG_HOST, LOG_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(json.toString());

        } catch (Exception e) {
            // On évite de faire planter l'application si le serveur de log n'est pas disponible
            System.out.println("WARN JsonLogger : impossible d'envoyer le log (" + e.getMessage() + ")");
        }
    }
}
