/**
 * a class to run the game
 * contains the main method
 *
 * @author Struan McKenzie
 * @version 1.0
 */

import java.io.*;
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
        while (!finished) {
            p[turn].display();
            guess(p[turn]);
            pauseMenu(p,turn);

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
        x = getCoordinate("x: ", p.length);
        y = getCoordinate("y: ", p.height);

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
     * @param toPrint what the player should be prompted
     * @return the index for the hidden board
     */
    private int getCoordinate(String toPrint, int lim) {
        int i = -1;
        while (i == -1) {
            System.out.print(toPrint);
            int c = scn.next().toCharArray()[0]; // convert string to int

            if (c >= 97 && c <= (lim+97))    // for input of a lower case letter, i attempted validation but gave up
                i = (c - 97);
            else if (c >= 65 && c <= (lim+65))    // for input of an upper case letter
                i = (c - 65);
        }
        return i;
    }

    /**
     * loads a previously saved game
     */
    public void loadGame() {
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
    public void help() {
        // print help page
        System.out.println("""
                Help page:
                The aim of the game is to find all the creatures before your opponent.
                If the game ends early the player with the most points wins
                
                Press enter to continue""");

        scn.nextLine();
        menu();
    }

    /**
     * asks the player if they want to enter the pause menu
     */
    public void pauseMenu(Player[] p, int turn) {
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
    public void save(Player[] p, int turn) {
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
}
