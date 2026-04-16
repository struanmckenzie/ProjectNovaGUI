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
        new Game().menu();
    }

    /**
     * menu system for the game
     */
    private void menu() {
        new LaunchGUI();
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
     * clears the terminal
     */
    private void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}