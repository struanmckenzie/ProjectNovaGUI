import javax.swing.*;
import java.awt.*;

public class NewGameScreen {
    private JFrame frame;

    public NewGameScreen(JFrame frame) {
        this.frame = frame;

        startNewGame();
    }


    private void startNewGame() {
        // initialise player array and create instance of GameObjects
        Player[] player = { new Player(), new Player() };
        GameObjects c = new GameObjects();

        // explain who starts
        System.out.println("""
                The explorer starts on their quest to rescue the last
                remaining underwater creatures.
                The hunter knows the explorer will lead them to the
                best place to hunt and decides to follow discretely...
                """);

        // set first player to explorer
        player[0].explorer = true;

        // get names of players
        System.out.print("Who will play as the\n" +
                "\33[32mMystical Explorer: ");
        player[0].setName("\33[32;1m" + scn.nextLine() + "\33[0m");

        System.out.print("\33[31mEvil Hunter: ");
        player[1].setName("\33[31;1m" + scn.nextLine() + "\33[0m");

        System.out.print("\33[0m"); // reset colour

        // call GameObjects to place objects on each players' board
        for (Player p : player) c.spawn(p);

        // start gameplay
        new StartGame(player);
    }
}
