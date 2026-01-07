package cmd;

import java.util.Scanner;

public class MenuCmd {


    public String menuServeur () {
        Scanner sc = new Scanner(System.in);
        String choix = "QUIT";

        System.out.println("+----------------------------+");
        System.out.println("| CHK  - Checker une paire   |");
        System.out.println("| ADD  - Ajouter une paire   |");
        System.out.println("| DEL  - Supprimer une paire |");
        System.out.println("| MOD  - Modifier une paire  |");
        System.out.println("| QUIT - Quitter             |");
        System.out.println("+----------------------------+");

        choix = sc.nextInt();

        sc.nextLine();

        return "";
    }
}
