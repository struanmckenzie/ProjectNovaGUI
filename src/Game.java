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