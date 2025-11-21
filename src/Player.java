/**
 * a class representing a player
 * contains methods of playing the game
 *
 * @author Struan McKenzie
 * @version 1.1
 */

import java.util.Scanner;

public class Player {
    Scanner scn = new Scanner(System.in);

    // FIELDS
    private String name;
    private int points;
    private int health;
    public int length = 20;
    public int height = 15;
    private char[][] board = new char[height][length];
    public char[][] hidden_board = new char[height][length];
    public char[][] temp_board = new char[height][length];

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
     * sets value for points
     * @param newPoints new points value
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
            System.arraycopy(newHBoard[i], 0, hidden_board[i], 0, board[0].length);
    }



    public void guess() {
        System.out.println("\n\nEnter a coordinate you think a creature might be");

        int x, y;   // declare variable to store coords
        String tmp; // temporary variable for input

        System.out.print("x: ");
        tmp = scn.next();

        x = inputConversion(tmp);

        System.out.print("y: ");
        tmp = scn.next();

        y = inputConversion(tmp);

        /*  TESTING PURPOSES ONLY
        System.out.println("x, y: " + x + " " + y);
        System.out.println(hidden_board[y][x]);
         */

        if (hidden_board[y][x] != '~') {
            System.out.println("Creature part found!");
            setBoard(y, x, hidden_board[y][x]);
            points += 5;
        } else
            System.out.println("No luck!");
    }

    /**
     * converts input from String to its
     * equivalent index on the hidden board
     *
     * @param s input from the player
     * @return the index for the hidden board
     */
    private int inputConversion(String s) {
        // convert string to int in order to work with it
        int c = s.toCharArray()[0];

        int i = 0;
        if (c >= 97 && c <= 116)    // for input of a lower case letter
            i = (c - 97);   // because arr starts at 0
        else if (c >= 65 && c <= 84)    // for input of an upper case letter
            i = (c - 65);   // because arr starts at 0
        else if (c >= 1 && c <= 20)   // for input of a number
            i--;

        return i;
    }


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

            for (int j = 0; j < length; j++)
                //System.out.print(hidden_board[i][j] + " ");    // FOR TESTING PURPOSES THIS IS THE HIDDEN BOARD
                System.out.print(board[i][j] + " ");
            System.out.println();
        }

        // print footer
        System.out.print("  ");
        letter = 97;
        for (int i = 0; i < length; i++) {
            System.out.print(letter + " ");
            letter++;
        }

    }
}
