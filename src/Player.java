/**
 * a class representing a player
 * contains methods of playing the game
 *
 * @author Struan McKenzie
 * @version 1.0
 */

import java.util.Scanner;

public class Player {
    Scanner scn = new Scanner(System.in);

    // FIELDS
    private String name;
    private int points;
    private int health;
    private int length = 20;
    private int height = 15;
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
                board[i][j] = '#';
                hidden_board[i][j] = '#';
                temp_board[i][j] = '#';
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

    /**
     *
     * @return the current hidden board layout
     */
    public char[][] getHidden_board() { return hidden_board; }



    public void guess() {
        int x, y;
        System.out.println("\nEnter a coordinate you think a creature might be");
        System.out.print("x: ");
        x = (scn.nextInt() - 1);    // -1 is to account for the array starting at 0
        scn.nextLine();

        System.out.print("y: ");
        y = (scn.nextInt() - 1);    // -1 is to account for the array starting at 0
        scn.nextLine();

        if (hidden_board[x][y] != '#') {
            System.out.println("Creature part found!");
            setBoard(x, y, hidden_board[x][y]);
            points += 5;
        }
        else
            System.out.println("No luck!");
    }


    /**
     * displays the current state of the board to the player
     */
    public void display() {
        System.out.println("\n" + getName());
        System.out.println("Points: " + getPoints());
        System.out.println("Health: " + getHealth());
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++)
                System.out.print(hidden_board[i][j] + " ");    // FOR TESTING PURPOSES THIS IS THE HIDDEN BOARD
            System.out.println();
        }
    }
}
