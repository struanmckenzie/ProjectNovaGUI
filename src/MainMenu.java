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
        JPanel leftImagePanel = new JPanel();
        loadImage(leftImagePanel, "Images/Explorer.jpg");
        mainPanel.add(leftImagePanel);

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
        JPanel rightImagePanel = new JPanel();
        loadImage(rightImagePanel, "Images/Hunter.jpg");
        mainPanel.add(rightImagePanel);

        frame.add(mainPanel);

        frame.repaint();
        frame.revalidate();
        frame.setVisible(true);
    }

    /**
     * load image in to panel
     * @param imagePanel panel that will contain the image
     * @param location of image in files
     */
    private void loadImage(JPanel imagePanel, String location) {
        try {
            ImageIcon rawImage = new ImageIcon(Objects.requireNonNull(getClass().getResource(location)));
            Image image = rawImage.getImage();
            Image scaledImage = image.getScaledInstance(frame.getWidth() / 3, -1, Image.SCALE_SMOOTH);
            ImageIcon finalImage = new ImageIcon(scaledImage);
            JLabel finalImageLabel = new JLabel(finalImage);
            imagePanel.add(finalImageLabel);

            System.out.println(getClass() + ": successfully loaded " + location);
        } catch (NullPointerException e) {
            System.out.println(getClass() + ": failed to load " + location);
        }
    }
}
