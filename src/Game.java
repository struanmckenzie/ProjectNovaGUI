/**
 * a class to run the game
 * contains the main method
 *
 * @author Struan McKenzie
 * @version 1.0
 */

import java.util.Scanner;

public class Game {
    private final Scanner scn = new Scanner(System.in);

    public static void main(String[] args) {
        Game g = new Game();
        g.menu();
    }

    /**
     * menu system for the game
     */
    private void menu() {
        System.out.println("\nMenu:");
        System.out.println("""
                Start new game - 1
                Load game ------ 2
                View help ------ 3
                Quit ----------- 4""");
        String option = scn.nextLine();

        switch (option) {
            case "1" -> {
                System.out.println();
                startNewGame();
            }
            case "2" -> {
                System.out.println();
                loadGame();
            }
            case "3" -> {
                System.out.println();
                help();
            }
            case "4" -> {
                System.out.println("""
                        
                        Press enter to stay
                        or enter q to quit""");
                String opt = scn.nextLine();

                if (opt.equals("q") || opt.equals("Q"))
                    System.exit(0);
                else
                    menu();
            }
        }
    }

    /**
     * for actual gameplay
     * @param p player array
     */
    private void play(Player[] p) {
        boolean finished = false;
        int turn = 0; // player whose turn it is
        while (!finished) {
            p[turn].display();
            guess(p[turn]);
            pauseMenu();

            if (turn == 0) turn = 1;
            else turn = 0;
        }
    }

    /**
     * start new game,
     * initialises boards and players
     */
    private void startNewGame() {
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


    /**
     * gets a players guess for where a creature might be
     *
     * @param p player
     */
    private void guess(Player p) {
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

    /**
     * loads a previously saved game
     */
    public void loadGame() {
        // load game
        System.out.println("look over reading from files");
        /* Files needed:
         1 for each board
         1 for each player's info*/

        // needs to somehow return all the information
    }

    /**
     * prints a help/instructions page
     */
    public void help() {
        // print help page
        System.out.println("Game help");

        System.out.println("\nPress enter to continue");
        scn.nextLine();
    }

    /**
     * asks the player if they want to enter the pause menu
     */
    public void pauseMenu() {
        scn.nextLine();
        System.out.println("""
                
                Press enter to continue
                or enter any other key for pause menu""");

        String s = scn.nextLine();

        if (!s.isEmpty()) {
            System.out.println("\n- - - - PAUSED - - - -");
            System.out.println("""
                    Save & quit ---------- 1
                    View help ------------ 2
                    Quit without saving -- 3
                    Continue --- press enter""");
            String option = scn.nextLine();

            if (!option.isEmpty()) {
                switch (option) {
                    case "1" -> {
                        System.out.println();
                        saveQuit();
                    }
                    case "2" -> {
                        System.out.println();
                        help();
                    }
                    case "3" -> {
                        System.out.println();
                        menu();
                    }
                }
            }
        }
        // game continues
    }

    /**
     * saves the current game to files on disk
     */
    public void saveQuit() {
        System.out.println("look over printing to files");
    }
}
