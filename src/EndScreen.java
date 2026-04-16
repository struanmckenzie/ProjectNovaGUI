import javax.swing.*;
import java.awt.*;

public class EndScreen {
    private final JFrame frame;

    /**
     * default constructor
     * @param frame main frame
     */
    public EndScreen(JFrame frame) {
        this.frame = frame;

        frame.getContentPane().removeAll();
        startUI();
    }

    private void startUI() {

    }
}
