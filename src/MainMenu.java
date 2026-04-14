import javax.swing.*;
import java.awt.*;

public class MainMenu {
    // Fields
    private JFrame frame;

    /**
     * Constructor
     */
    public MainMenu(JFrame frame) {
        this.frame = frame;

        frame.getContentPane().removeAll();
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
            System.out.println(getClass() + ": new game");
            new NewGameScreen(frame);
        });
        loadGame.addActionListener(l -> System.out.println(getClass() + ": load game"));
        help.addActionListener(l -> System.out.println(getClass() + ": help screen"));
        quit.addActionListener(l -> {
            System.out.println(getClass() + ": quit game");
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

        frame.repaint();
        frame.revalidate();
        frame.setVisible(true);
    }
}
