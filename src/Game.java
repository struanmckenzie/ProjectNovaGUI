/**
 * a class to run the game
 * contains the main method
 *
 * @author Struan McKenzie
 * @version 1.0
 */

import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        Game g =  new Game();
        Menu.menu(g);
    }

    public void startGame() {
        Scanner scn = new Scanner(System.in);

        // start game
        Player[] plr = new Player[2];
        Creatures c = new Creatures();
        for (int i = 0; i < plr.length; i++) {
            plr[i] = new Player();
            System.out.print("Enter name of player " + (i+1) + ": ");
            plr[i].setName(scn.nextLine());
        }

        // spawns game objects on each players board
        for (Player p : plr) c.spawn(p);

        boolean finished = false;
        int turn = 0; // player whose turn it is
        while (!finished) {
            plr[turn].display();
            plr[turn].guess();

            if (turn == 0) turn = 1;
            else turn = 0;
        }

    }

    public void loadGame() {
        // load game
        System.out.println("Loading game");
    }

    public void help(Game g) {
        // print help page
        System.out.println("Game help");


        Scanner scn = new Scanner(System.in);
        System.out.println("\nPress enter to go back to the menu");
        scn.nextLine();
        Menu.menu(g);
    }

    public void promptForMenu() {
        Scanner scn = new Scanner(System.in);
        System.out.println("\nPress enter to continue\nEnter any other key for menu");

    }
}
