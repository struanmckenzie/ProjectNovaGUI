import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LaunchGUI {
    private final JFrame frame = new JFrame();

    void main() {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // SAVE GAME
                // using names of player 1, player2 as name

                System.out.println("Game Closed");
                frame.dispose();
            }
        });

        new MainMenu(frame);
    }
}