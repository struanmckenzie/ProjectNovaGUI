import javax.swing.*;
import java.awt.*;

public class MainMenu {
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
        // configure window frame
        frame.setTitle("Project Nova");
        frame.setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel(new GridLayout(4,1));

        // buttons
        JButton newGame = new JButton("New Game");
        JButton loadGame = new JButton("Load Game");
        JButton help = new JButton("Need Help?");
        JButton quit = new JButton("Quit");

        // action listeners
        newGame.addActionListener(l -> {
            LaunchGUI.systemMessages.add("New game");
            new NewGameScreen(frame);
        });

        loadGame.addActionListener(l -> LaunchGUI.systemMessages.add("Load game"));
        help.addActionListener(l -> LaunchGUI.systemMessages.add("Help page"));
        quit.addActionListener(l -> {
            // confirm quit
            if (JOptionPane.showConfirmDialog(frame, "Quit game?") == 0)
                System.exit(0);
        });

        // add buttons to panel
        buttonPanel.add(newGame);
        buttonPanel.add(loadGame);
        buttonPanel.add(help);
        buttonPanel.add(quit);

        frame.add(buttonPanel);

        frame.setVisible(true);
    }
}
