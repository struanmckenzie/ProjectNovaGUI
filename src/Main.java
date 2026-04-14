import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

void main(String[] args) {
    JFrame frame = new JFrame();

    frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            // SAVE GAME
            frame.dispose();
        }
    });

    new MainMenu(frame);
}