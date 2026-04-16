import javax.swing.*;
import java.awt.*;

public class PlayGame {
    // fields
    private final JFrame frame;
    private final Player[] player;

    public PlayGame(JFrame frame, Player[] player) {
        this.frame = frame;
        this.player = player;

        // clear frame ready for game board
        frame.getContentPane().removeAll();

        frame.setTitle("THE PLAYER WHO'S BOARD IT IS");
        startGame();
    }

    private void startGame() {
        int turn = 0; // player whose turn it is
        int megaGuess;  // y coordinate MegaGuess is on
        boolean hint;    // store which board to display

        while (true) {
            hint = checkStatus(player, turn);

            // only display if player needs a hint
            if (hint) {
                hint = player[turn].hint(hint);
                player[turn].display(hint);
                hint = false;
            }

            player[turn].display(hint);

            //pauseMenu(p, (turn-1)*(-1));  i think this is just annoying, but it does work

            megaGuess = guess(player[turn], -1, 0);
            if (megaGuess != -1) {
                for (int x = 0; x < player[turn].length; x++) {
                    guess(player[turn], megaGuess, x);
                }
            }
            else {
                System.out.print("\33[22;56H\033[1J\33[H");
                System.out.flush();
            }

            checkStatus(player, turn);

            player[turn].display(hint);
            pauseMenu(player, turn);

            // switch to next player's turn
            turn = (turn-1)*(-1);
        }

        // main panel setup
        JPanel mainPanel = new JPanel(new FlowLayout());

        // player stats panel
        JPanel stats = new JPanel();


        frame.repaint();
        frame.revalidate();
        frame.setVisible(true);
    }
}
