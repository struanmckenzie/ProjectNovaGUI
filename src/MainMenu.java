import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    // Fields
    public JFrame frame;
    /**
     * Constructor
     */
    public MainMenu(JFrame frame) {
        this.frame = frame;
        launchUI();
    }

    private void launchUI() {
        setTitle("Project Nova");
        setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel(new GridLayout(4,1));

        // buttons
        JButton newGame = new JButton("New Game");
        JButton loadGame = new JButton("Load Game");
        JButton help = new JButton("Need Help?");
        JButton quit = new JButton("Quit");

        // action listeners
        newGame.addActionListener(l -> System.out.println("Start game"));
        loadGame.addActionListener(l -> System.out.println("Load game"));
        help.addActionListener(l -> System.out.println("Help page"));
        quit.addActionListener(l -> {
             new JOptionPane("Quit game?");
        });

        // add buttons to panel
        buttonPanel.add(newGame);
        buttonPanel.add(loadGame);
        buttonPanel.add(help);
        buttonPanel.add(quit);

        add(buttonPanel);

        setVisible(true);
    }
}
