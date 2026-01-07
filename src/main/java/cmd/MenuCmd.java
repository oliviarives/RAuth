package cmd;

public final class MenuCmd {

    // Constructeur privé : classe utilitaire
    private MenuCmd() {}

    public static void afficherMenuManager() {
        System.out.println("+----------------------------+");
        System.out.println("| CHK  - Checker une paire   |");
        System.out.println("| ADD  - Ajouter une paire   |");
        System.out.println("| DEL  - Supprimer une paire |");
        System.out.println("| MOD  - Modifier une paire  |");
        System.out.println("| HELP - Afficher le menu    |");
        System.out.println("| QUIT - Quitter             |");
        System.out.println("+----------------------------+");
        System.out.println();
    }
}
