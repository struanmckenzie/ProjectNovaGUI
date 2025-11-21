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

    public void startGame(Game g) {
        Scanner scn = new Scanner(System.in);

        // start game
        Player[] plr = new Player[2];
        GameObjects c = new GameObjects();
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
            guess(plr[turn]);
            promptForMenu(g);

            if (turn == 0) turn = 1;
            else turn = 0;

        }
    }

    public void guess(Player p) {
        Scanner scn = new Scanner(System.in);
        System.out.println("\n\nEnter a coordinate you think a creature might be");

        int x, y;   // declare variable to store coords
        String tmp; // temporary variable for input

        System.out.print("x: ");
        tmp = scn.next();

        x = inputConversion(tmp);

        System.out.print("y: ");
        tmp = scn.next();

        y = inputConversion(tmp);

        /*  TESTING PURPOSES ONLY
        System.out.println("x, y: " + x + " " + y);
        System.out.println(hidden_board[y][x]);
         */

        if (p.getHidden_board()[y][x] != '~') {
            System.out.println("Creature part found!");
            p.setBoard(y, x, p.getHidden_board()[y][x]);
            p.setPoints(p.getPoints() + 5);
        } else {
            p.setBoard(y,x,'X');
            System.out.println("No luck!");
        }
    }

    /**
     * converts input from String to its
     * equivalent index on the hidden board
     *
     * @param s input from the player
     * @return the index for the hidden board
     */
    private int inputConversion(String s) {
        // convert string to int in order to work with it
        int c = s.toCharArray()[0];

        int i = 0;
        if (c >= 97 && c <= 116)    // for input of a lower case letter
            i = (c - 97);   // because arr starts at 0
        else if (c >= 65 && c <= 84)    // for input of an upper case letter
            i = (c - 65);   // because arr starts at 0

        return i;
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

    public void promptForMenu(Game g) {
        Scanner scn = new Scanner(System.in);
        System.out.println("""
                
                Press enter to continue
                or any other key to enter pause menu""");

        if (!scn.nextLine().isEmpty()) {
            System.out.println("!!! PAUSE MENU UNDER CONSTRUCTION !!!");
        }

        // game continues
    }
}
