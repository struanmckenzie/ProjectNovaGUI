/**
 * a class to run the game
 * contains the main method
 *
 * @author Struan McKenzie
 * @version 1.0
 */

import java.util.Scanner;

public class Game {
    public Scanner scn = new Scanner(System.in);

    public static void main(String[] args) {
        Game g = new Game();
        g.menu();
    }

    /**
     * menu system for the game
     */
    public void menu() {
        System.out.println("\nMenu:");
        System.out.println("""
                Start new game - 1
                Load game ------ 2
                View help ------ 3
                Exit ----------- 4""");
        int option = scn.nextInt();
        scn.nextLine();

        if (option == 1) {
            System.out.println();
            startNewGame();
        } else if (option == 2) {
            System.out.println();
            loadGame();
        } else if (option == 3) {
            System.out.println();
            help();
        } else if (option == 4) {
            System.out.println("""
                    
                    Press enter to stay
                    or enter any other key to exit""");
            String optn = scn.nextLine();

            if (!optn.isEmpty())
                System.exit(0);
            else
                menu();
        }
    }

    public void startNewGame() {
        // initialise player array and create instance of GameObjects
        Player[] plr = new Player[2];
        GameObjects c = new GameObjects();

        for (int i = 0; i < plr.length; i++) {
            plr[i] = new Player();
            System.out.print("Enter name of player " + (i + 1) + ": ");
            plr[i].setName(scn.nextLine());
        }

        // spawns game objects on each players board
        for (Player p : plr) c.spawn(p);

        play(plr);
    }

    public void play(Player[] p) {
        boolean finished = false;
        int turn = 0; // player whose turn it is
        while (!finished) {
            p[turn].display();
            guess(p[turn]);
            promptForMenu();

            if (turn == 0) turn = 1;
            else turn = 0;
        }
    }

    /**
     * gets a players guess for where a creature might be
     *
     * @param p player
     */
    public void guess(Player p) {
        System.out.println("\n\nEnter a coordinate you think a creature might be");

        int x, y;   // declare variable to store coordinates
        x = getCoordinate("x: ");
        y = getCoordinate("y: ");

        if (p.getHidden_board()[y][x] != '~') {
            System.out.println("Creature part found!");
            p.setBoard(y, x, p.getHidden_board()[y][x]);
            p.setPoints(p.getPoints() + 5);
        } else {
            p.setBoard(y, x, 'X');
            System.out.println("No luck!");
        }
    }

    /**
     * converts input from String to its
     * equivalent index on the hidden board
     *
     * @param toPrint what the player should be prompted
     * @return the index for the hidden board
     */
    private int getCoordinate(String toPrint) {
        int i = -1;
        while (i == -1) {
            System.out.print(toPrint);
            int c = scn.next().toCharArray()[0]; // convert string to int

            if (c >= 97 && c <= 116)    // for input of a lower case letter
                i = (c - 97);
            else if (c >= 65 && c <= 84)    // for input of an upper case letter
                i = (c - 65);
            else
                System.out.println("Invalid input, please try again");
        }
        return i;
    }

    public void loadGame() {
        // load game
        System.out.println("look over reading from files");
        /* Files needed:
         1 for each board
         1 for each player's info*/
    }

    public void help() {
        // print help page
        System.out.println("Game help");

        System.out.println("\nPress enter to go back to the menu");
        scn.nextLine();
        menu();
    }

    public void promptForMenu() {
        System.out.println("""
                
                Press enter to continue
                or enter any other key for pause menu""");

        if (!scn.nextLine().isEmpty()) {
            System.out.println("!!! PAUSE MENU UNDER CONSTRUCTION !!!");    // TEMPORARY

            System.out.println("\n- - - PAUSED - - -");
            System.out.println("""
                    Save game ------ 1
                    View help ------ 2
                    Main menu ------ 3""");
            int option = scn.nextInt();
            scn.nextLine();

            if (option == 1) {
                System.out.println();
                saveGame();
            } else if (option == 2) {
                System.out.println();
                help();
            } else if (option == 3) {
                System.out.println();
                menu();
            }
        }
        // game continues
    }

    public void saveGame() {
        System.out.println("look over printing to files");
    }
}
