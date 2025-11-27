/**
 * a class to run the game
 * contains the main method
 *
 * @author Struan McKenzie
 * @version 2.6
 */

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
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
            default -> menu();
        }
    }

    /**
     * for actual gameplay
     * @param p player array
     */
    private void play(Player[] p) {
        boolean finished = false;
        int turn = 0; // player whose turn it is
        String explorer = p[0].getName();   // identify the explorer for the end game message

        while (!finished) {
            p[turn].display();
            guess(p[turn]);
            checkBoard(p[turn], explorer);
            pauseMenu(p, turn);

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
            if (i == 0)
                System.out.print("Enter the name of the mystical explorer: ");
            else
                System.out.print("Enter the name of the hunter: ");
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

        // get the upper limit for coordinates
        int[] lim = new int[2];
        lim[0] = p.length;
        lim[1] = p.height;

        int[] coordinates = getCoordinates(lim);
        int x,y;
        x = coordinates[0];
        y = coordinates[1];


        if (p.getHidden_board()[y][x] != '~') {
            System.out.println("Creature part found!");
            p.setBoard(y, x, p.getHidden_board()[y][x]);
            p.setPoints(p.getPoints() + 5);

            // HOW TO IDENTIFY IF PLAYER FOUND LAST PART OF CREATURE?????????????????????????????????
        } else {
            p.setBoard(y, x, 'X');
            System.out.println("No luck!");
        }
    }

    /**
     * converts input from String to its
     * equivalent index on the hidden board
     *
     * @param lim the limit to prevent a higher number
     *            than the array can handle
     * @return the index for the hidden board
     */
    private int[] getCoordinates(int[] lim) {
        // setup array to store the coordinates
        int[] xy = new int[2];

        System.out.print("xy: ");
        char[] c = scn.next().toCharArray();

        boolean valid = true;
        for (int i = 0; i < 2; i++) {
            if (c[i] >= 97 && (c[i] < (lim[i] + 97))) {   // for input of a lower case letter
                xy[i] = (c[i] - 97);
            }
            else if (c[i] >= 65 && (c[i] < (lim[i] + 65))) {   // for input of an upper case letter
                xy[i] = (c[i] - 65);
            }
            else {
                System.out.println("\nError, invalid input\nTry again.");
                valid = false;
                break;
                }
        }

        // run method again if input was invalid
        if (!valid)
            xy = getCoordinates(lim);
        return xy;
    }

    /**
     * loads a previously saved game
     */
    private void loadGame() {
        // initialise player array
        Player[] plr = new Player[2];

        for (int i = 0; i < plr.length; i++)
            plr[i] = new Player();

        System.out.println("Please enter the name of the save: ");
        String saveName = ("save/" + scn.nextLine());

        int startingPlayer = 0;

        for (int player = 0; player < 2; player++) {
            // set up names to save files under
            String loadBoard = (saveName + "_" + player + "_Board.csv");
            String loadHBoard = (saveName + "_" + player + "_HBoard.csv");
            String loadDetails = (saveName + "_" + player + "_Details.csv");

            FileReader fr;
            BufferedReader br;

            try {
                // load board
                fr = new FileReader(loadBoard);
                br = new BufferedReader(fr);

                // effectively making a for loop from a while loop coz buffered reader
                String nextLine = br.readLine();
                int i = 0;
                while (i < 15 && nextLine != null) {
                    for (int j = 0; j < 20; j++) {
                        plr[player].setBoard(i, j, nextLine.toCharArray()[j]);
                    }
                    nextLine = br.readLine();
                    i++;
                }
                br.close();

                // load hidden board
                fr = new FileReader(loadHBoard);
                br = new BufferedReader(fr);

                nextLine = br.readLine();
                i = 0;
                while (i < 15 && nextLine != null) {
                    for (int j = 0; j < 20; j++) {
                        plr[player].setHidden_board(i, j, nextLine.toCharArray()[j]);
                    }
                    nextLine = br.readLine();
                    i++;
                }
                br.close();

                // load other details
                fr = new FileReader(loadDetails);
                br = new BufferedReader(fr);

                plr[player].setName(br.readLine());
                plr[player].setPoints(Integer.parseInt(br.readLine()));
                plr[player].setHealth(Integer.parseInt(br.readLine()));

                nextLine = br.readLine();
                if (nextLine != null) {
                    startingPlayer = Integer.parseInt(nextLine);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // make sure the correct player starts first
        if (startingPlayer != 0) {
            Player tmp = plr[0];
            plr[0] = plr[1];
            plr[1] = tmp;
        }

        // play game
        play(plr);
    }

    /**
     * prints a help/instructions page
     */
    private void help() {
        // print help page
        System.out.println("""
                Help page:
                The aim of the game is to find all the creatures before your opponent.
                If the game ends early the player with the most points wins
                
                Enter the coordinates in the form: xy
                where 'x' is the letter on the x axis
                and 'y' is the letter on the y axis
                
                Press enter to continue""");

        scn.nextLine();
        menu();
    }

    /**
     * asks the player if they want to enter the pause menu
     */
    private void pauseMenu(Player[] p, int turn) {
        scn.nextLine();

        System.out.println("""
                
                Press enter to continue
                or enter any other key for pause menu""");

        String s = scn.nextLine();

        if (!s.isEmpty()) {
            System.out.println("\n- - - - PAUSED - - - -");
            String winner = p[0].getName();
            if (p[1].getPoints() > p[0].getPoints())
                winner = p[1].getName();
            else if (p[0].getPoints() == p[1].getPoints())
                winner = "Tied";

            System.out.print("Current winner is: " + winner);
            System.out.println("""
                    
                    Save ----------------- 1
                    Quit to menu --------- 2
                    Continue --- press enter""");
            String option = scn.nextLine();

            if (!option.isEmpty()) {
                switch (option) {
                    case "1" -> {
                        System.out.println();
                        save(p, turn);
                    }
                    case "2" -> {
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
    private void save(Player[] p, int turn) {
        System.out.println("Please enter a name for this save: ");
        String saveName = ("save/" + scn.nextLine());

        for (int player = 0; player < p.length; player++) {
            // names to save files under
            String saveBoard = (saveName + "_" + player + "_Board.csv");
            String saveHBoard = (saveName + "_" + player + "_HBoard.csv");
            String saveDetails = (saveName + "_" + player + "_Details.csv");

            FileOutputStream outStream;
            PrintWriter pw;

            try {
                // save board
                outStream = new FileOutputStream(saveBoard);
                pw = new PrintWriter(outStream);

                for (int i = 0; i < p[player].height; i++) {
                    for (int j = 0; j < p[player].length; j++) {
                        pw.print(p[player].getBoard()[i][j]);
                    }
                    pw.println();
                }
                pw.close();

                // save hidden board
                outStream = new FileOutputStream(saveHBoard);
                pw = new PrintWriter(outStream);

                for (int i = 0; i < p[player].height; i++) {
                    for (int j = 0; j < p[player].length; j++) {
                        pw.print(p[player].getHidden_board()[i][j]);
                    }
                    pw.println();
                }
                pw.close();

                // save other details
                outStream = new FileOutputStream(saveDetails);
                pw = new PrintWriter(outStream);

                pw.println(p[player].getName() + "\n" + p[player].getPoints() + "\n" + p[player].getHealth());

                // mark the player who should start
                if (player != turn)
                    pw.println(player);
                pw.close();

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("""
            
            Press enter to continue
            or enter any other key for main menu""");

        String s = scn.nextLine();

        if (!s.isEmpty()) {
            menu();
        }

    }

    /**
     * checks to see if the win condition has been met
     * @param p player object
     * @param explorer used to identify which player has won
     */
    private void checkBoard(Player p, String explorer) {
        int hiddenCount = 0;  // number of creature parts in hidden board
        int visableCount = 0;  // number of creature parts in visable board

        for (int i = 0; i < p.height; i++) {
            for (int j = 0; j < p.length; j++) {
                if (p.getHidden_board()[i][j] != '~')   // edit this if more things are added to board
                    hiddenCount++;
            }
        }

        for (int i = 0; i < p.height; i++) {
            for (int j = 0; j < p.length; j++) {
                if (p.getBoard()[i][j] != '~' && p.getBoard()[i][j] != 'X') // edit this if more things are added to board
                    visableCount++;
            }
        }

        // check to see if all the creatures have been found
        if (hiddenCount == visableCount) {
            if (p.getName().equals(explorer))
                System.out.println("\n" + p.getName() + " is the winner!\n" + """
                        Congratulations!
                        You managed to save the creatures from the hunter.
                        Hunter, you failed your mission. Do better next time.""");
            else
                System.out.println("\n" + p.getName() + " is the winner!\n" + """
                        End of mission.
                        You have hunted all the creatures to extinction.
                        Explorer, you failed to save the creatures. Try harder next time.""");

            System.out.println("\nPress enter to exit");
            scn.nextLine();
            scn.nextLine();
            System.exit(0);
            }
    }
}