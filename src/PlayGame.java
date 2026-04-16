import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class PlayGame {
    // fields
    private final JFrame frame;

    private final Player[] player; // player array
    int turn; // player whose turn it is
    int megaGuess;  // y coordinate MegaGuess is on
    boolean hint;    // store which board to display


    public PlayGame(JFrame frame, Player[] player, int turn) {
        this.frame = frame;
        this.player = player;
        this.turn = turn;

        frame.getContentPane().removeAll();

        frame.setTitle("Project Nova - " + player[turn].getName());
        startUI();
    }

    private void startUI() {
        // menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem quit = new JMenuItem("Quit");
        JMenuItem returnToMenu = new JMenuItem("Menu");
        JMenuItem getHint = new JMenuItem("Hint");

        menu.add(save);
        menu.add(returnToMenu);
        menu.add(quit);
        menu.add(getHint);
        menuBar.add(menu);

        // menu item action listeners
        save.addActionListener(l -> {
            System.out.println(getClass() + ": save game");
            String saveName = JOptionPane.showInputDialog(null, "Enter name for the save",
                    "Save game", JOptionPane.INFORMATION_MESSAGE);

            save(player, turn, saveName);
        });
        quit.addActionListener(l -> {
            // confirm quit
            if (JOptionPane.showConfirmDialog(frame, "Quit without saving?") == 0)
                System.exit(0);
            System.out.println(getClass() + ": quit game");
        });
        returnToMenu.addActionListener(l -> System.out.println("return to menu"));
        getHint.addActionListener(l -> System.out.println(player[turn].getName() + ": request hint"));

        frame.setJMenuBar(menuBar);

        // main panel setup
        JPanel mainPanel = new JPanel(new BorderLayout());

        // ================ player stats panel =============
        JPanel stats = getStats(turn);
        mainPanel.add(stats, BorderLayout.NORTH);

        // ================ MAIN GAME =======================

        hint = checkStatus(player, turn);

        // game panel
        JPanel buttonBoard = new JPanel(new GridLayout(Config.BOARD_SIZE, Config.BOARD_SIZE));

        for (int i = Config.BOARD_SIZE - 1; i >= 0; i--) {     // y coordinate iterator
            for (int j = 0; j < Config.BOARD_SIZE; j++) {  // x coordinate iterator
                if (hint)
                    buttonBoard.add(styledButton(String.valueOf(player[turn].getHidden_board()[i][j]), j, i));
                else
                    buttonBoard.add(styledButton(String.valueOf(player[turn].getBoard()[i][j]), j, i));

            }
        }
        mainPanel.add(buttonBoard, BorderLayout.CENTER);

        frame.add(mainPanel);

        frame.repaint();
        frame.revalidate();
        frame.setVisible(true);
    }

    private JButton styledButton(String charToDisplay, int x, int y) {
        JButton btn = new JButton(charToDisplay);

        // if tile has been guessed, disable it
        if (!charToDisplay.equals("+")) btn.setEnabled(false);

        // set font
        btn.setFont(new Font(null, Font.BOLD, 20));

        // compute what happens when player makes a guess by pressing a button
        btn.addActionListener(l -> {
            System.out.println("Clicked button: " + x +", "+ y);    // TURN INTO SYSTEM MESSAGE
            megaGuess = guess(player[turn], -1, 0, x, y);
            if (megaGuess != -1) {
                for (int z = 0; z < player[turn].length; z++) {
                    guess(player[turn], megaGuess, z, x, y);
                }
            }

            checkStatus(player, turn);

            frame.getContentPane().removeAll();
            startUI();
        });
        return btn;
    }

    /**
     * gets the current statistics of the current player
     * @param turn player whose turn it is
     * @return stats panel
     */
    private JPanel getStats(int turn) {
        JPanel stats = new JPanel(new GridLayout(0, 4));

        // initialise player stats fields
        JTextField name = new JTextField(), points = new JTextField(), health = new JTextField();
        name.setEditable(false);
        points.setEditable(false);
        health.setEditable(false);

        // put player details in the boxes
        name.setText(player[turn].getName());
        points.setText("Points: " + (player[turn].getPoints()));
        health.setText("Health: " + (player[turn].getHealth()));

        stats.add(name);
        stats.add(points);
        stats.add(health);

        // next player button
        JButton next = new JButton("Next Player");
        next.addActionListener(l -> new PlayGame(frame, player, (turn-1)*(-1)));
        stats.add(next);

        stats.setSize(new Dimension(frame.getWidth(), frame.getHeight() / 10));

        return stats;
    }

    private boolean checkStatus(Player[] p, int turn) {
        // check if the player has died
        if (p[turn].getHealth() <= 0) {
            System.out.println("You died :(");
            if (p[turn].isExplorer)
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
            new MainMenu(frame);
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
            if (p[turn].isExplorer)
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

            new MainMenu(frame);
        }

        return p[turn].getHealth() <= 25;
    }

    /**
     * saves the current game to files on disk
     */
    private void save(Player[] p, int turn, String saveName) {
        saveName = ("Saves/" + saveName + "/");

        // see if save folder exists, creates one if not
        File theDir = new File(saveName);
        if (!theDir.exists()){
            theDir.mkdirs();
        }

        for (int player = 0; player < p.length; player++) {
            // names to save files under
            String saveBoard = (saveName + player + "Board.txt");
            String saveHiddenBoard = (saveName + player + "HiddenBoard.txt");
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
                outStream = new FileOutputStream(saveHiddenBoard);
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
                        p[player].isExplorer);

                // mark the player who should start
                if (player != turn)
                    pw.println(player);
                pw.close();

            } catch (FileNotFoundException e) {
                System.out.println("Error: failed to save to files");
            }
        }
    }

    /**
     * gets a players guess for where a creature might be
     *
     * @param p player
     */
    private int guess(Player p, int megaGuess, int iteration, int x, int y) {
        if (megaGuess != -1) {
            y = megaGuess;
            x = iteration;
        }

        for (int i = 0; i < (p.height+6); i++) {
            System.out.print("\33[1B");
        }

        // already guessed
        if (p.getBoard()[y][x] != Config.UNDISCOVERED_TILE) {
            // recurs if you've already guessed the coordinates and it's not a MegaGuess
            if (megaGuess == -1) {
                p.display(false);
                System.out.println("You already guessed there, try again");
                guess(p, megaGuess, iteration, x, y);
            } else
                System.out.println("\nPrevious guess");
        }
        else if (p.getHidden_board()[y][x] != Config.UNDISCOVERED_TILE) {
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
            else if (p.getBoard()[y][x] == Config.UNDISCOVERED_TILE) {
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
}
