/**
 * a class representing a player
 * contains methods of playing the game
 *
 * @author Struan McKenzie
 * @version 2.0
 */

public class Player {
    // FIELDS
    private String name;
    public boolean explorer;
    private int points;
    private int health;
    public int length;
    public int height;
    private char[][] board;
    private char[][] hidden_board;
    public char[][] temp_board;

    // CONSTRUCTOR
    /**
     * default constructor
     */
    public Player() {
        name = "John";
        explorer = false;
        points = 0;
        health = 100;
        height = 16;
        length = 16;
        board = new char[height][length];
        hidden_board = new char[height][length];
        temp_board = new char[height][length];

        for (int i = 0; i < (height); i++)
            for (int j = 0; j < (length); j++) {
                board[i][j] = '+';
                hidden_board[i][j] = '+';
                temp_board[i][j] = '+';
            }
    }

    // METHODS
    /**
     * reveal a creature part if found
     * @param latitude y axis value
     * @param longitude x asis value
     * @param part creature part identifier
     */
    public void setBoard(int latitude, int longitude, char part) { board[latitude][longitude] = part; }

    /**
     * gets board
     * @return current board state
     */
    public char[][] getBoard() { return board; }

    /**
     * set players name
     * @param newName is the name for the player
     */
    public void setName(String newName) { name = newName; }

    /**
     * gets players name
     * @return players name
     */
    public String getName() { return name; }

    /**
     * sets players points
     * @param newPoints new points
     */
    public void setPoints(int newPoints) { points = newPoints; }

    /**
     * gets players points
     * @return number of points player has
     */
    public int getPoints() { return points; }

    /**
     * sets players health value
     * @param newHealth new value of health
     */
    public void setHealth(int newHealth) { health = newHealth; }

    /**
     * gets players health
     * @return amount of health player has
     */
    public int getHealth() { return health; }

    /**
     * sets entire new hidden board layout
     * @param newHBoard new hidden board
     */
    public void setHidden_board(char[][] newHBoard) {
        for (int i = 0; i < (height); i++)
            System.arraycopy(newHBoard[i], 0, hidden_board[i], 0, length);
    }

    /**
     * sets a specific part of the hidden board
     * @param y axis value
     * @param x axis value
     * @param c character specific part should be set to
     */
    public void setHidden_board(int y, int x, char c) { hidden_board[y][x] = c; }

    /**
     * gets hidden board
     * @return hidden board
     */
    public char[][] getHidden_board() { return hidden_board; }

    /**
     * gets temporary board
     * @return temporary board
     */
    public char[][] getTemp_board() { return temp_board; }

    public boolean hint(boolean hint) {
        System.out.println("Player: " + getName());
        System.out.println("Points: " + getPoints());
        System.out.println("Health: " + getHealth());

        if (getPoints() > 9)
            System.out.print("""
            \33[91m
            Your health is under 25HP!!\33[91m
            You have the opportunity to spend 10 points to receive a hint.
            The hint reveals all for 10 seconds. Use this time wisely.
            To buy this hint enter any character, to decline press ENTER.
            """);
        else
            System.out.print("""
                    \33[91m
                    Your health is under 25HP!!\33[91m
                    Unfortunately you're poor. It costs 10 points for a hint so
                    you don't have enough to get one. Unlucky.
                    Press ENTER to continue.""");

        String h = Game.scn.nextLine();

        if (h.isEmpty() || getPoints() < 10) hint = false;
        else points = points - 10;

        return hint;
    }

    /**
     * displays the current state of the board to the player
     */
    public void display(boolean hint) {
        if (!hint) {
            System.out.println("Player: " + getName());
            System.out.println("Points: " + getPoints());
            System.out.println("Health: " + getHealth());
        } else
            System.out.println("\n\33[91m= = = = HINT = = = =\33[0m");

        // print top border
        System.out.print("+ ");
        for (int i = 0; i < length * 2 + 3; i++)
            System.out.print("-");
        System.out.println(" +");

        // print top border
        char l = 97;
        System.out.print("|   ");
        for (int i = 0; i < length; i++) {
            System.out.print(l + " ");
            l++;
        }
        System.out.println("  |");

        // print board and row numbers
        l = 97;
        for (int i = 0; i < height; i++) {
            System.out.print("| " + l + " ");

            for (int j = 0; j < length; j++) {
                if (hint)
                    System.out.print("\33[46;30m" + hidden_board[i][j]);
                else
                    System.out.print("\33[46;30m" + board[i][j]);

                if (j < length - 1)
                    System.out.print(" \33[0m");
                else
                    System.out.print("\33[0m ");
            }
            System.out.println(l + " |");
            l++;
        }

        // print bottom axis
        System.out.print("|   ");
        l = 97;
        for (int i = 0; i < length; i++) {
            System.out.print(l + " ");
            l++;
        }
        System.out.println("  |");

        // print bottom border
        System.out.print("+ ");
        for (int i = 0; i < length * 2 + 3; i++)
            System.out.print("-");
        System.out.print(" +");

        // reset text
        System.out.println("\33[0m");

        if (hint) {
            System.out.println("\33[91m= = = = HINT = = = =\33[0m");
            // wait for 10 seconds
            try { Thread.sleep(10000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }  // sleep

            System.out.print("\033[H\033[2J");  // clear terminal
            System.out.flush();
        }
    }
}
