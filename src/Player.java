/**
 * a class representing a player
 * contains methods of playing the game
 *
 * @author Struan McKenzie
 * @version 1.1
 */

public class Player {
    // FIELDS
    private String name;
    private int points;
    private int health;
    public final int length = 20;
    public final int height = 15;
    private char[][] board = new char[height][length];
    private char[][] hidden_board = new char[height][length];
    private char[][] temp_board = new char[height][length];

    // CONSTRUCTOR

    /**
     * default constructor
     */
    Player() {
        name = "John";
        points = 0;
        health = 100;

        for (int i = 0; i < (height); i++)
            for (int j = 0; j < (length); j++) {
                board[i][j] = '~';
                hidden_board[i][j] = '~';
                temp_board[i][j] = '~';
            }
    }

    // METHODS
    /**
     * reveal a creature part if found
     * @param latitude;
     * @param longitude;
     * @param part creature part identifier
     */
    public void setBoard(int latitude, int longitude, char part) {
        board[latitude][longitude] = part;
    }

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
     * sets new hidden board layout
     * @param newHBoard new hidden board
     */
    public void setHidden_board(char[][] newHBoard) {
        for (int i = 0; i < (height); i++)
            System.arraycopy(newHBoard[i], 0, hidden_board[i], 0, length);
    }

    /**
     * gets hidden board
     * @return hidden board
     */
    public char[][] getHidden_board() { return hidden_board; }

    /**
     * sets new temporary board layout
     * @param newTBoard new hidden board
     */
    public void setTemp_board(char[][] newTBoard) {
        for (int i = 0; i < (height); i++)
            System.arraycopy(newTBoard[i], 0, temp_board[i], 0, newTBoard[0].length);
    }

    /**
     * gets temporary board
     * @return temporary board
     */
    public char[][] getTemp_board() { return temp_board; }




    /**
     * displays the current state of the board to the player
     */
    public void display() {
        System.out.println("\n" + getName());
        System.out.println("Points: " + getPoints());
        System.out.println("Health: " + getHealth());

        // print board and row numbers
        char letter = 97;
        for (int i = 0; i < height; i++) {
            System.out.print(letter + " ");
            letter++;

            for (int j = 0; j < length; j++) {
                System.out.print(hidden_board[i][j] + " ");    // FOR TESTING PURPOSES THIS IS THE HIDDEN BOARD
                //System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }

        // print bottom axis
        System.out.print("  ");
        letter = 97;
        for (int i = 0; i < length; i++) {
            System.out.print(letter + " ");
            letter++;
        }

    }
}
