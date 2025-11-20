/**
 * a menu system
 *
 * @author Struan McKenzie
 * @version 1.0
 */

import java.util.Scanner;

public class Menu {
    public static void menu(Game g) {
        Scanner scn = new Scanner(System.in);
        System.out.println("\nMenu:");
        System.out.println("Start new game - 1");
        System.out.println("Load game ------ 2");
        System.out.println("View help ------ 3");
        System.out.println("Exit ----------- 4");
        int option = scn.nextInt();
        scn.nextLine();

        if (option == 1) {
            System.out.println();
            g.startGame();
        } else if (option == 2) {
            System.out.println();
            g.loadGame();
        } else if (option == 3) {
            System.out.println();
            g.help(g);
        } else {
            System.out.println("\nAre you sure you sure you want to exit?");
            System.out.println("Exit - 1");
            System.out.println("Stay - Enter");
            String optn = scn.nextLine();

            if (optn.equals("1"))
                System.exit(0);
            else
                menu(g);
        }
    }
}