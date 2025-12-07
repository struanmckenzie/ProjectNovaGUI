/**
 * a class to run the game
 * contains the main method
 *
 * @author Struan McKenzie
 * @version 2.7
 */

// import libraries
import java.io.*;
import java.util.Scanner;

public class Game {
    // scanner available to all classes
    public static final Scanner scn = new Scanner(System.in);

    /**
     * main method - creates instance of Game object and starts menu
     * @param args command line args. unused
     */
    public static void main(String[] args) {
        Game g = new Game();
        g.menu();
    }

    /**
     * menu system for the game
     */
    private void menu() {
        clear();    // clear terminal
        System.out.print("""
                + ------- MENU ------- +
                | Start new game ••• 1 |
                | Load game •••••••• 2 |
                | View help •••••••• 3 |
                | Quit ••••••••••••• Q |
                + -------------------- +
                """);
        String option = scn.nextLine();

        switch (option) {
            case "1" -> {
                clear();    // clear terminal
                startNewGame();
            }
            case "2" -> {
                clear();    // clear terminal
                loadGame();
            }
            case "3" -> {
                clear();    // clear terminal
                help();
            }
            case "q", "Q" -> System.exit(0);
            default -> menu();
        }
    }

    /**
     * start new game,
     * initialises boards and players
     */
    private void startNewGame() {
        // initialise player array and create instance of GameObjects
        Player[] plr = {new Player(), new Player()};
        GameObjects c = new GameObjects();

        // explain who starts
        System.out.println("""
                The explorer starts on their quest to rescue the last
                remaining underwater creatures.
                The hunter knows the explorer will lead them to the
                best place to hunt and decides to follow discretely...
                """);

        // set first player to explorer
        plr[0].explorer = true;

        // get names of players
        System.out.print("Who will play as the\n" +
                "\33[32mMystical Explorer: ");
        plr[0].setName("\33[32;1m" + scn.nextLine() + "\33[0m");

        System.out.print("\33[31mEvil Hunter: ");
        plr[1].setName("\33[31;1m" + scn.nextLine() + "\33[0m");

        System.out.print("\33[0m"); // reset colour

        // call GameObjects to place objects on each players' board
        for (Player p : plr) c.spawn(p);

        // start gameplay
        play(plr);
    }

    /**
     * loads a previously saved game
     */
    private void loadGame() {
        // get name of save and make sure it exists
        System.out.println("Please enter the name of the save: ");
        String saveName = ("./.ProjectNova/" + scn.nextLine() + "/");

        File save = new File(saveName + "0" + "Details.txt");
        if (!save.exists()) {
            System.out.println("Save does not exist press ENTER to return to menu\n");
            scn.nextLine();
            menu();
        }

        // initialise player array
        Player[] plr = new Player[2];

        for (int i = 0; i < plr.length; i++)
            plr[i] = new Player();

        int startingPlayer = 0;

        for (int player = 0; player < 2; player++) {
            // declare names of files to load
            String loadBoard = (saveName + player + "Board.txt");
            String loadHBoard = (saveName + player + "HBoard.txt");
            String loadDetails = (saveName + player + "Details.txt");

            FileReader fr;
            BufferedReader br;

            // load details
            try {
                fr = new FileReader(loadDetails);
                br = new BufferedReader(fr);

                plr[player].length = Integer.parseInt(br.readLine());
                plr[player].height = Integer.parseInt(br.readLine());
                plr[player].setName(br.readLine());
                plr[player].setPoints(Integer.parseInt(br.readLine()));
                plr[player].setHealth(Integer.parseInt(br.readLine()));
                plr[player].explorer = Boolean.parseBoolean(br.readLine());

                String nextLine = br.readLine();
                if (nextLine != null) {
                    startingPlayer = Integer.parseInt(nextLine);
                }

                // load board
                fr = new FileReader(loadBoard);
                br = new BufferedReader(fr);

                nextLine = br.readLine();
                int i = 0;
                while(nextLine != null) {
                    for (int j = 0; j < plr[player].length; j++)
                        plr[player].setBoard(i, j, nextLine.toCharArray()[j]);

                    nextLine = br.readLine();
                    i++;
                }
                br.close();

                // load hidden board
                fr = new FileReader(loadHBoard);
                br = new BufferedReader(fr);

                nextLine = br.readLine();
                i = 0;
                while (nextLine != null) {
                    for (int j = 0; j < plr[player].length; j++)
                        plr[player].setHidden_board(i, j, nextLine.toCharArray()[j]);

                    nextLine = br.readLine();
                    i++;
                }
                br.close();


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
        System.out.print("""
                + --------------------------- HELP PAGE -------------------------------- +
                | The aim of the game is to find all the creatures before your opponent. |
                | If the game ends early the player with the most points wins and the    |
                |  current winner is displayed on the pause menu before quitting.        |
                |                                                                        |
                | You can save and quit from the pause menu.                             |
                |                                                                        |
                | Enter the coordinates in the form: xy                                  |
                |  where 'x' is the letter on the x axis                                 |
                |  and 'y' is the letter on the y axis                                   |
                |                                                                        |
                | 5 points are earned for every part of a creature found                 |
                | 5 bonus points are earned once a whole creature is found               |
                | Bombs take away 25HP                                                   |
                | Seaweed adds 15HP (effectively acts as a shield if you have 100HP)     |
                | MegaGuess reveals a whole row                                          |
                |                                                                        |
                | If you are under 25HP you will be give the opportunity to buy a hint   |
                |  with your points which will reveal the board for 10 seconds.          |
                |  Use this time wisely                                                  |
                |                                                                        |
                | Objects ID key:                                                        |
                |    Fish | Sea Snake | Crab  | Starfish | Bomb | Seaweed | MegaGuess    |
                |  -------+-----------+-------+----------+------+---------+------------  |
                |    F F  |  S S S S  |  C C  |    O     |  B   |    H    |     M        |
                |         |           |  C C  |  O O O   |      |         |              |
                |         |           |       |    O     |      |         |              |
                + ---------------------------------------------------------------------- +
                
                Press ENTER to continue
                """);

        scn.nextLine();
        menu();
    }

    /**
     * for actual gameplay
     * @param p player array
     */
    private void play(Player[] p) {
        int turn = 0; // player whose turn it is
        int megaGuess;  // y coordinate MegaGuess is on
        boolean hint;    // store which board to display

        while (true) {
            clear();    // clear terminal
            hint = checkStatus(p, turn);

            // only display if player needs a hint
            if (hint) {
                hint = p[turn].hint(hint);
                p[turn].display(hint);
                hint = false;
            }

            clear();  // clear terminal
            p[turn].display(hint);

            //pauseMenu(p, (turn-1)*(-1));  i think this is just annoying, but it does work

            megaGuess = guess(p[turn], -1, 0);
            if (megaGuess != -1) {
                clear();  // clear terminal
                for (int x = 0; x < p[turn].length; x++) {
                    guess(p[turn], megaGuess, x);
                }
            }
            else {
                System.out.print("\33[22;56H\033[1J\33[H");
                System.out.flush();
            }
            scn.nextLine(); // eat up something i dont even know anymore

            checkStatus(p, turn);

            p[turn].display(hint);
            pauseMenu(p, turn);

            // switch to next player's turn
            turn = (turn-1)*(-1);
        }

    }

    /**
     * gets a players guess for where a creature might be
     *
     * @param p player
     */
    private int guess(Player p, int megaGuess, int iteration) {
        int x, y;
        if (megaGuess == -1) {
            System.out.println("Guess where a creature might be");

            // get the upper limit for coordinates
            int[] lim = {p.length, p.height};

            // get coordinates
            int[] coordinates = getCoordinates(lim);
            x = coordinates[0];
            y = coordinates[1];
        }
        else {
            y = megaGuess;
            x = iteration;
        }

        clear();    // clear terminal
        for (int i = 0; i < (p.height+6); i++) {
            System.out.print("\33[1B");
        }

        // already guessed
        if (p.getBoard()[y][x] != '+') {
            // recurs if you've already guessed the coordinates and it's not a MegaGuess
            if (megaGuess == -1) {
                clear();    // clear terminal
                p.display(false);
                System.out.println("You already guessed there, try again");
                guess(p, megaGuess, iteration);
            } else
                System.out.println("\nPrevious guess");
        }
        else if (p.getHidden_board()[y][x] != '+') {
            // bomb found
            if (p.getHidden_board()[y][x] == 'B') {
                System.out.println("\nYou have triggered an ocean bomb\nLose 25HP");
                p.setBoard(y, x, p.getHidden_board()[y][x]);
                p.setHealth(p.getHealth() - 25);
            }

            // seaweed found
            else if  (p.getHidden_board()[y][x] == 'H') {
                System.out.println("\nYou have found some medicinal seaweed\nGain 15HP");
                p.setBoard(y, x, p.getHidden_board()[y][x]);
                p.setHealth(p.getHealth() + 15);
            }

            // MegaGuess found
            else if (p.getHidden_board()[y][x] == 'M') {
                if  (megaGuess == -1) {
                    System.out.println("You found a MegaGuess tile - this reveals all the tiles in the same row.");
                    p.setBoard(y, x, p.getHidden_board()[y][x]);
                    return y;
                } else System.out.println("\nMegaGuess tile");
            }

            // creature found
            else if (p.getBoard()[y][x] == '+') {
                System.out.println("\nCreature part found!\n+5 Points");
                p.setBoard(y, x, p.getHidden_board()[y][x]);
                p.setPoints(p.getPoints() + 5);
            }



            /*
            detect what creature (part) was found

            I know there is definitely a more efficient way of doing this but I do not
            have the time to come up with one so I apologise to whoever has to read and mark this.
             */
            switch (p.getHidden_board()[y][x]) {
                case 'F' -> {
                    if (p.getBoard()[y][x+1] == 'F' || p.getBoard()[y][x-1] == 'F') {
                        System.out.println(" Fish found! +5 Bonus Points");
                        p.setPoints(p.getPoints() + 5);
                    }
                }
                case 'S' -> {
                    // get to the start of the found parts of the snake
                    while (p.getBoard()[y][x] == 'S') x--;

                    int count = 0;  // store the number of snake parts found
                    x++;    // compensate for the while loop taking away an extra 1
                    while (p.getBoard()[y][x] == 'S') {
                        count++;
                        x++;
                    }

                    if (count == 4) {
                        System.out.println(" Sea Snake found! +5 Bonus Points");
                        p.setPoints(p.getPoints() + 5);
                    }
                }
                case 'C' -> {
                    // these are separated out so its (kinda) easier to read
                    if (p.getBoard()[y][x+1] == 'C') {
                        if (p.getBoard()[y + 1][x] == 'C' && p.getBoard()[y + 1][x + 1] == 'C') {
                            System.out.println(" Crab found! +5 Bonus Points");
                            p.setPoints(p.getPoints() + 5);

                        } else if (p.getBoard()[y - 1][x] == 'C' && p.getBoard()[y - 1][x + 1] == 'C') {
                            System.out.println(" Crab found! +5 Bonus Points");
                            p.setPoints(p.getPoints() + 5);
                        }
                    }

                    else if (p.getBoard()[y][x-1] == 'C')
                        if (p.getBoard()[y + 1][x] == 'C' && p.getBoard()[y + 1][x - 1] == 'C') {
                            System.out.println(" Crab found! +5 Bonus Points");
                            p.setPoints(p.getPoints() + 5);

                        } else if (p.getBoard()[y - 1][x] == 'C' && p.getBoard()[y - 1][x - 1] == 'C') {
                            System.out.println(" Crab found! +5 Bonus Points");
                            p.setPoints(p.getPoints() + 5);
                        }
                }
                case 'O' -> {
                    // starting from the centre
                    if (p.getBoard()[y-1][x] == 'O' && p.getBoard()[y+1][x] == 'O' &&
                            p.getBoard()[y][x+1] == 'O' && p.getBoard()[y][x-1] == 'O') {
                        System.out.println(" Starfish found! +5 Bonus Points");
                        p.setPoints(p.getPoints() + 5);
                    }
                    // now we know it doesn't start in the centre
                    // starting from left or right side
                    else if (p.getBoard()[y][x+1] == 'O' && p.getBoard()[y][x+2] == 'O' ||
                             p.getBoard()[y][x-1] == 'O' && p.getBoard()[y][x-2] == 'O') {
                        if (p.getBoard()[y-1][x-1] == 'O' && p.getBoard()[y+1][x-1] == 'O' ||
                                p.getBoard()[y-1][x+1] == 'O' && p.getBoard()[y+1][x+1] == 'O') {
                            System.out.println(" Starfish found! +5 Bonus Points");
                            p.setPoints(p.getPoints() + 5);
                        }
                    }
                    // now we know it doesn't start in the middle row
                    // starting from the top or bottom
                    else if(p.getBoard()[y-1][x] == 'O' && p.getBoard()[y-2][x] == 'O' ||
                            p.getBoard()[y+1][x] == 'O' && p.getBoard()[y+2][x] == 'O') {
                        if (p.getBoard()[y+1][x-1] == 'O' && p.getBoard()[y+1][x+1] == 'O' ||
                                p.getBoard()[y-1][x+1] == 'O' && p.getBoard()[y-1][x-1] == 'O') {
                            System.out.println(" Starfish found! +5 Bonus Points");
                            p.setPoints(p.getPoints() + 5);
                        }
                    }
                }
            }
        } else {
            p.setBoard(y, x, 'x');
            System.out.println("\nNo luck!");
        }

        // return -1 if tile was not a MegaGuess tile
        return -1;
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
        boolean valid = true;

        char[] c;
        do {
            System.out.print("xy: ");
            c = scn.next().toCharArray();
            if (c.length != 2) System.out.println("\nInvalid input\nTry again");
        } while (c.length != 2);

        // convert chars into coordinates on grid
        for (int i = 0; i < 2; i++) {
            if (c[i] >= 97 && (c[i] < (lim[i] + 97))) {   // for input of a lower case letter
                xy[i] = (c[i] - 97);
            }
            else if (c[i] >= 65 && (c[i] < (lim[i] + 65))) {   // for input of an upper case letter
                xy[i] = (c[i] - 65);
            }
            else {
                System.out.println("\nInvalid input\nTry again.");
                valid = false;
                break;
                }
        }

        // recur if input was invalid
        if (!valid)
            xy = getCoordinates(lim);
        return xy;
    }

    /**
     * asks the player if they want to enter the pause menu
     */
    private void pauseMenu(Player[] p, int turn) {
        System.out.println("\n\n\n\nPress ENTER to continue or enter any other key for pause menu");

        String s = scn.nextLine();

        if (!s.isEmpty()) {
            clear();  // clear terminal
            System.out.print("""
                    + -------- PAUSED -------- +
                    | Save ••••••••••••••••• S |
                    | Quit to menu ••••••••• Q |
                    | Continue ••••••••• ENTER |
                    + ------------------------ +
                    """);
            String option = scn.nextLine();

            if (!option.isEmpty()) {
                switch (option) {
                    case "s", "S" -> save(p, turn);
                    case "q", "Q" -> {
                        clear(); // clear terminal
                        // print summary
                        System.out.println("+ -------");
                        if (p[1].getPoints() > p[0].getPoints())
                            System.out.println("| Winner:\n| " + p[1].getName());
                        else if (p[0].getPoints() > p[1].getPoints())
                            System.out.println("| Winner:\n| " + p[0].getName());
                        else
                            System.out.println("| Game is tied");

                        System.out.println("+ -------");
                        for (Player player : p) {
                            System.out.println("| " + player.getName());
                            System.out.println("| Points: " + player.getPoints());
                            System.out.println("| Health: " + player.getHealth());
                            System.out.println("+ -------");
                        }

                        System.out.println("\nQ to quit or ENTER to continue");
                        if (!scn.nextLine().isEmpty()) menu();
                    }
                }
            }
        }
    }

    /**
     * saves the current game to files on disk
     */
    private void save(Player[] p, int turn) {
        clear();
        System.out.println("Please enter a name for this save: ");
        String saveName = ("./.ProjectNova/" + scn.nextLine() + "/");

        // see if save folder exists, creates one if not
        File theDir = new File(saveName);
        if (!theDir.exists()){
            theDir.mkdirs();
        }

        for (int player = 0; player < p.length; player++) {
            // names to save files under
            String saveBoard = (saveName + player + "Board.txt");
            String saveHBoard = (saveName + player + "HBoard.txt");
            String saveDetails = (saveName + player + "Details.txt");

            FileOutputStream outStream;
            PrintWriter pw;

            try {
                // save board
                outStream = new FileOutputStream(saveBoard);
                pw = new PrintWriter(outStream);

                // write board to file
                for (int i = 0; i < p[player].height; i++) {
                    for (int j = 0; j < p[player].length; j++) {
                        pw.print(p[player].getBoard()[i][j]);
                    }
                    pw.println();
                }
                pw.close();

                // write hidden board to file
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

                pw.println(p[player].length + "\n" +
                        p[player].height + "\n" +
                        p[player].getName() + "\n" +
                        p[player].getPoints() + "\n" +
                        p[player].getHealth() + "\n" +
                        p[player].explorer);

                // mark the player who should start
                if (player != turn)
                    pw.println(player);
                pw.close();

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("""
            
            Press ENTER to continue or enter any other key for main menu""");

        String s = scn.nextLine();

        if (!s.isEmpty()) {
            menu();
        }

    }

    /**
     * checks to see if win/lose condition has been met
     * @param p player object
     * @return whether the player should be offered a hint
     */
    private boolean checkStatus(Player[] p, int turn) {
        // check if the player has died
        if (p[turn].getHealth() <= 0) {
            System.out.println("You died :(");
            if (p[turn].explorer)
                System.out.println("""
                        You failed to save the rest of the creatures from the \33[31;1mEvil Hunter\33[0m
                        who continued on and managed to hunt down
                        all the creatures and wins by default.""");
            else
                System.out.println("""
                        You failed to hunt all the creatures down.
                        The \33[32;1mMystical Explorer\33[0m went on to save the rest
                        of the creatures and wins by default.""");

            System.out.println("\n+ -------");
            for (Player player : p) {
                System.out.println("| " + player.getName());
                System.out.println("| Points: " + player.getPoints());
                System.out.println("| Health: " + player.getHealth());
                System.out.println("+ -------");
            }

            System.out.println("\nENTER to exit");
            scn.nextLine();
            menu();
        }

        // check if player has found all the creatures
        int hiddenCount = 0;  // number of creature parts in hidden board
        int visableCount = 0;  // number of creature parts in visable board

        for (int i = 0; i < p[turn].height; i++) {
            for (int j = 0; j < p[turn].length; j++) {
                if (p[turn].getHidden_board()[i][j] == 'F' || p[turn].getHidden_board()[i][j] == 'C' ||
                        p[turn].getHidden_board()[i][j] == 'S' || p[turn].getHidden_board()[i][j] == 'O')
                    hiddenCount++;
                if (p[turn].getBoard()[i][j] == 'F' || p[turn].getBoard()[i][j] == 'C' ||
                        p[turn].getBoard()[i][j] == 'S' || p[turn].getBoard()[i][j] == 'O')
                    visableCount++;
            }
        }

        // check to see if all the creatures have been found
        if (hiddenCount == visableCount) {
            if (p[turn].explorer)
                System.out.println("\n" + p[turn].getName() + " is the winner!\n" + """
                        As the \33[32;1mMystical Explorer\33[0m you managed to save all the creatures from the hunter.
                        """);
            else
                System.out.println("\n" + p[turn].getName() + " is the winner!\n" + """
                        As the \33[31;1mEvil Hunter\33[0m you have managed to hunt all the creatures to extinction.
                        """);

            System.out.println("\n+ -------");
            for (Player player : p) {
                System.out.println("| " + player.getName());
                System.out.println("| Points: " + player.getPoints());
                System.out.println("| Health: " + player.getHealth());
                System.out.println("+ -------");
            }

            System.out.println("\nPress ENTER to exit");
            scn.nextLine();
            menu();
            }

        return p[turn].getHealth() <= 25;
    }

    /**
     * clears the terminal
     */
    private void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}