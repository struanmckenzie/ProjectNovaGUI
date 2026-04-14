import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LaunchGUI {
    private final JFrame frame = new JFrame();
    public static SystemLog<String> playerOneActions;
    public static SystemLog<String> playerTwoActions;
    public static SystemLog<String> events;
    public static SystemLog<String> errors;
    public static SystemLog<String> systemMessages;

    void main() {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // SAVE GAME


                frame.dispose();
            }
        });

        /*
         * gets the dimensions of the screen
         * sets the window size to be a percentage of it
         */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // set window size to percentage of screen size
        int width = (int) screenSize.getWidth() * 3/5;
        int height = (int) screenSize.getHeight() * 3/5;

        frame.setSize(new Dimension(width, height));

        new MainMenu(frame);
    }
}