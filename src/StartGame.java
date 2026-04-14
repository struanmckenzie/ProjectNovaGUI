import javax.swing.*;

public class StartGame {
    // fields
    private final JFrame frame;
    private final Player[] player;

    public StartGame(JFrame frame, Player[] player) {
        this.frame = frame;
        this.player = player;

        // clear frame ready for game board
        frame.getContentPane().removeAll();

        frame.setTitle("THE PLAYER WHO'S BOARD IT IS");
        startGame();
    }

    private void startGame() {

        frame.repaint();
        frame.revalidate();
        frame.setVisible(true);
    }
}
