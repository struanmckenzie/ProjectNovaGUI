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
        frame.setSize(780,750);
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

            saveGame(player, turn, saveName);
        });
        quit.addActionListener(l -> {
            // confirm quit
            if (JOptionPane.showConfirmDialog(frame, "Quit without saving?") == 0)
                System.exit(0);
            System.out.println(getClass() + ": quit game");
        });
        returnToMenu.addActionListener(l -> {
            if (JOptionPane.showConfirmDialog(frame, "Return to menu?\nAny unsaved progress will be lost!") == 0)
                new MainMenu(frame);
            System.out.println("return to menu");
        });
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
            checkStatus(player, turn);  // REMOVE THIS LATER

            megaGuess = guess(player[turn], -1, 0, x, y);
            if (megaGuess != -1) {
                for (int z = 0; z < player[turn].length; z++) {
                    guess(player[turn], megaGuess, z, x, y);
                }
            }

            // only change to other player if game hasnt ended
            if (!checkStatus(player, turn)) {
                // flip player turn
                turn = (turn - 1) * -1;

                frame.getContentPane().removeAll();
                startUI();
            }
        });
        return btn;
    }

    /**
     * gets the current statistics of the current player
     * @param turn player whose turn it is
     * @return stats panel
     */
    private JPanel getStats(int turn) {
        JPanel stats = new JPanel(new GridLayout(0, 3));

        // initialise player stats fields
        JTextField name = textBox(), points = textBox(), health = textBox();

        // put player details in the boxes
        name.setText(player[turn].getName());
        points.setText("Points: " + (player[turn].getPoints()));
        health.setText("Health: " + (player[turn].getHealth()));

        stats.add(name);
        stats.add(points);
        stats.add(health);

        // set size of stats panel
        stats.setSize(new Dimension(frame.getWidth(), frame.getHeight() / 10));

        return stats;
    }

    /**
     * creates a styled text box
     * @return textField
     */
    private JTextField textBox() {
        JTextField textField = new JTextField();
        textField.setEnabled(false);
        textField.setFont(new Font(null, Font.PLAIN, 20));
        textField.setDisabledTextColor(Config.TEXT_COLOUR);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        return textField;
    }

    /**
     * checks to see if the player has found all creatures/died
     * then shows end screen
     * @param players player array
     * @param turn current player
     * @return true if game has ended
     */
    private boolean checkStatus(Player[] players, int turn) {
        // check if the player has died
        if (players[turn].getHealth() <= 0) {
            new EndScreen(frame, 0, players, turn);
            return true;
        }

        // check if player has found all the creatures
        int hiddenCount = 0;  // number of creature parts in hidden board
        int visibleCount = 0;  // number of creature parts in visible board

        // count number of creature parts on the board
        // count number of found creature parts
        for (int i = 0; i < players[turn].height; i++) {
            for (int j = 0; j < players[turn].length; j++) {
                if (players[turn].getHidden_board()[i][j] == Config.FISH_TILE || players[turn].getHidden_board()[i][j] == Config.CRAB_TILE ||
                        players[turn].getHidden_board()[i][j] == Config.SNAKE_TILE || players[turn].getHidden_board()[i][j] == Config.STARFISH_TILE)
                    hiddenCount++;
                if (players[turn].getBoard()[i][j] == Config.FISH_TILE || players[turn].getBoard()[i][j] == Config.CRAB_TILE ||
                        players[turn].getBoard()[i][j] == Config.SNAKE_TILE || players[turn].getBoard()[i][j] == Config.STARFISH_TILE)
                    visibleCount++;
            }
        }

        // DEBUGGING STUFF
        System.out.println(hiddenCount + " " + visibleCount);

        // check to see if all the creatures have been found
        if (hiddenCount == visibleCount) {
            new EndScreen(frame, 1, players, turn);
            return true;
        }

        return false;
    }

    /**
     * saves the current game to files on disk
     */
    private void saveGame(Player[] p, int turn, String saveName) {
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
                if (player == turn)
                    pw.println("not null");
                pw.close();

            } catch (FileNotFoundException e) {
                System.out.println("Error: failed to save to files");
            }
        }
    }

    /**
     * gets a players guess for where a creature might be
     *
     * @param currentPlayer player
     */
    private int guess(Player currentPlayer, int megaGuess, int iteration, int x, int y) {
        if (megaGuess != -1) {
            y = megaGuess;
            x = iteration;
        }

         if (currentPlayer.getHidden_board()[y][x] != Config.UNDISCOVERED_TILE) {
            // bomb found
            if (currentPlayer.getHidden_board()[y][x] == Config.BOMB_TILE) {
                popup("You triggered an ocean bomb\nLose " + Config.BOMB_STRENGTH + "HP");
                currentPlayer.setBoard(y, x, Config.BOMB_TILE);
                currentPlayer.setHealth(currentPlayer.getHealth() - Config.BOMB_STRENGTH);
            }

            // seaweed found
            else if  (currentPlayer.getHidden_board()[y][x] == Config.WEED_TILE) {
                popup("You have found some medicinal seaweed\nGain " + Config.WEED_STRENGTH + "HP");
                currentPlayer.setBoard(y, x, Config.WEED_TILE);
                currentPlayer.setHealth(currentPlayer.getHealth() + Config.WEED_STRENGTH);
            }

            // MegaGuess found
            else if (currentPlayer.getHidden_board()[y][x] == Config.MEGAGUESS_TILE) {
                if  (megaGuess == -1) {
                    popup("You found a MegaGuess tile\nthis reveals all the tiles in the same row.");
                    currentPlayer.setBoard(y, x, currentPlayer.getHidden_board()[y][x]);
                    return y;
                }
            }

            // creature found
            // this is a lazy way to do it. works because the tile will only
            // remain "undiscovered" if it is a creature part
            else if (currentPlayer.getBoard()[y][x] == Config.UNDISCOVERED_TILE) {
                popup("Creature part found!\n+" + Config.CREATURE_PART_POINTS +" points");
                currentPlayer.setBoard(y, x, currentPlayer.getHidden_board()[y][x]);
                currentPlayer.setPoints(currentPlayer.getPoints() + Config.CREATURE_PART_POINTS);
            }




            /*
            detect what creature (part) was found
            I know there is definitely a more efficient way of doing this
             */
            switch (currentPlayer.getHidden_board()[y][x]) {
                // fish found
                case Config.FISH_TILE -> {
                    if (currentPlayer.getBoard()[y][x+1] == Config.FISH_TILE ||
                            currentPlayer.getBoard()[y][x-1] == Config.FISH_TILE) {
                        System.out.println(" Fish found!\n +" + Config.CREATURE_FOUND_POINTS + " Bonus Points");
                        currentPlayer.setPoints(currentPlayer.getPoints() + Config.CREATURE_FOUND_POINTS);
                    }
                }

                // snake found
                case Config.SNAKE_TILE -> {
                    // get to the start of the found parts of the snake
                    while (currentPlayer.getBoard()[y][x] == Config.SNAKE_TILE) x--;

                    int count = 0;  // store the number of snake parts found
                    x++;    // compensate for the while loop taking away an extra 1
                    while (currentPlayer.getBoard()[y][x] == Config.SNAKE_TILE) {
                        count++;
                        x++;
                    }

                    if (count == 4) {
                        popup("Sea Snake found!\n +" + Config.CREATURE_FOUND_POINTS + " Bonus Points");
                        currentPlayer.setPoints(currentPlayer.getPoints() + Config.CREATURE_FOUND_POINTS);
                    }
                }

                // crab found
                case Config.CRAB_TILE -> {
                    if (currentPlayer.getBoard()[y][x+1] == Config.CRAB_TILE) {
                        if (currentPlayer.getBoard()[y + 1][x] == Config.CRAB_TILE &&
                                currentPlayer.getBoard()[y + 1][x + 1] == Config.CRAB_TILE) {
                            popup("Crab found!\n +" + Config.CREATURE_FOUND_POINTS + " Bonus Points");
                            currentPlayer.setPoints(currentPlayer.getPoints() + Config.CREATURE_FOUND_POINTS);

                        } else if (currentPlayer.getBoard()[y - 1][x] == Config.CRAB_TILE &&
                                currentPlayer.getBoard()[y - 1][x + 1] == Config.CRAB_TILE) {
                            popup("Crab found!\n +" + Config.CREATURE_FOUND_POINTS + " Bonus Points");
                            currentPlayer.setPoints(currentPlayer.getPoints() + Config.CREATURE_FOUND_POINTS);
                        }
                    }

                    else if (currentPlayer.getBoard()[y][x-1] == Config.CRAB_TILE)
                        if (currentPlayer.getBoard()[y + 1][x] == Config.CRAB_TILE &&
                                currentPlayer.getBoard()[y + 1][x - 1] == Config.CRAB_TILE) {
                            popup("Crab found!\n +" + Config.CREATURE_FOUND_POINTS + " Bonus Points");
                            currentPlayer.setPoints(currentPlayer.getPoints() + Config.CREATURE_FOUND_POINTS);

                        } else if (currentPlayer.getBoard()[y - 1][x] == Config.CRAB_TILE &&
                                currentPlayer.getBoard()[y - 1][x - 1] == Config.CRAB_TILE) {
                            popup("Crab found!\n +" + Config.CREATURE_FOUND_POINTS + " Bonus Points");
                            currentPlayer.setPoints(currentPlayer.getPoints() + Config.CREATURE_FOUND_POINTS);
                        }
                }

                // starfish found
                case Config.STARFISH_TILE -> {
                    // starting from the centre
                    if (currentPlayer.getBoard()[y-1][x] == Config.STARFISH_TILE &&
                            currentPlayer.getBoard()[y+1][x] == Config.STARFISH_TILE &&
                            currentPlayer.getBoard()[y][x+1] == Config.STARFISH_TILE &&
                            currentPlayer.getBoard()[y][x-1] == Config.STARFISH_TILE) {
                        popup("Starfish found!\n +" + Config.CREATURE_FOUND_POINTS + " Bonus Points");
                        currentPlayer.setPoints(currentPlayer.getPoints() + Config.CREATURE_FOUND_POINTS);
                    }
                    // now we know it doesn't start in the centre
                    // starting from left or right side
                    else if (currentPlayer.getBoard()[y][x+1] == Config.STARFISH_TILE &&
                            currentPlayer.getBoard()[y][x+2] == Config.STARFISH_TILE ||
                            currentPlayer.getBoard()[y][x-1] == Config.STARFISH_TILE &&
                                    currentPlayer.getBoard()[y][x-2] == Config.STARFISH_TILE) {
                        if (currentPlayer.getBoard()[y-1][x-1] == Config.STARFISH_TILE &&
                                currentPlayer.getBoard()[y+1][x-1] == Config.STARFISH_TILE ||
                                currentPlayer.getBoard()[y-1][x+1] == Config.STARFISH_TILE &&
                                        currentPlayer.getBoard()[y+1][x+1] == Config.STARFISH_TILE) {
                            popup("Starfish found!\n +" + Config.CREATURE_FOUND_POINTS + " Bonus Points");
                            currentPlayer.setPoints(currentPlayer.getPoints() + Config.CREATURE_FOUND_POINTS);
                        }
                    }
                    // now we know it doesn't start in the middle row
                    // starting from the top or bottom
                    else if(currentPlayer.getBoard()[y-1][x] == Config.STARFISH_TILE &&
                            currentPlayer.getBoard()[y-2][x] == Config.STARFISH_TILE ||
                            currentPlayer.getBoard()[y+1][x] == Config.STARFISH_TILE &&
                                    currentPlayer.getBoard()[y+2][x] == Config.STARFISH_TILE) {
                        if (currentPlayer.getBoard()[y+1][x-1] == Config.STARFISH_TILE &&
                                currentPlayer.getBoard()[y+1][x+1] == Config.STARFISH_TILE ||
                                currentPlayer.getBoard()[y-1][x+1] == Config.STARFISH_TILE &&
                                        currentPlayer.getBoard()[y-1][x-1] == Config.STARFISH_TILE) {
                            popup("Starfish found!\n +" + Config.CREATURE_FOUND_POINTS + " Bonus Points");
                            currentPlayer.setPoints(currentPlayer.getPoints() + Config.CREATURE_FOUND_POINTS);
                        }
                    }
                }
            }
        } else {
            currentPlayer.setBoard(y, x, Config.GUESSED_TILE);
            popup("No luck!");
        }

        // return -1 if tile was not a MegaGuess tile
        return -1;
    }

    /**
     * creates a popup telling the player what they found
     * @param message
     */
    private void popup(String message) {
        JOptionPane.showMessageDialog(null, message, player[turn].getName(), JOptionPane.INFORMATION_MESSAGE);
    }
}
