import javax.swing.*;
import java.awt.*;
import java.util.Objects;

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

        // Main panel to contain sub-panels
        JPanel mainPanel = new JPanel(new GridLayout(1,3));

        // Panel containing image of explorer
        JPanel leftImage = new JPanel();

        // get image of explorer
        try {
            ImageIcon rawImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("Images/Explorer.jpg")));
            Image image = rawImage.getImage();
            Image scaledImage = image.getScaledInstance(frame.getWidth() / 3, -1, Image.SCALE_SMOOTH);
            ImageIcon explorerImage = new ImageIcon(scaledImage);
            JLabel explorerImageLabel = new JLabel(explorerImage);
            leftImage.add(explorerImageLabel);

            System.out.println(getClass() + ": successfully loaded explorer image");
        } catch (NullPointerException e) {
            System.out.println(getClass() + ": failed to load explorer image");
        }

        mainPanel.add(leftImage);

        // panel to hold buttons
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

        mainPanel.add(buttonPanel);

        // Panel containing image of explorer
        JPanel rightImage = new JPanel();

        try {
            // get image of explorer
            ImageIcon rawImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("Images/Hunter.jpg")));
            Image image = rawImage.getImage();

            // scale image with panel
            Image scaledImage = image.getScaledInstance(frame.getWidth() / 3, -1, Image.SCALE_SMOOTH);
            ImageIcon hunterImage = new ImageIcon(scaledImage);
            JLabel hunterImageLabel = new JLabel(hunterImage);
            rightImage.add(hunterImageLabel);

            System.out.println(getClass() + ": successfully loaded hunter image");
        } catch (NullPointerException e) {
            System.out.println(getClass() + ": failed to load hunter image");
        }

        mainPanel.add(rightImage);

        frame.add(mainPanel);

        frame.repaint();
        frame.revalidate();
        frame.setVisible(true);
    }
}
