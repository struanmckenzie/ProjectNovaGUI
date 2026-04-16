import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class NewGameScreen {
    private final JFrame frame;

    public NewGameScreen(JFrame frame) {
        this.frame = frame;

        // empty frame for new screen
        frame.getContentPane().removeAll();

        frame.setTitle("New Game");
        startNewGame();
    }


    private void startNewGame() {
        // initialise player array and create instance of GameObjects
        Player[] player = { new Player(), new Player() };
        GameObjects objects = new GameObjects();

        // setup main panel
        JPanel mainPanel = new JPanel(new GridLayout(3,1));

        // initial message to players
        JTextArea plot = new JTextArea("The explorer starts on their quest to rescue " +
                "the last remaining underwater creatures. The hunter knows the explorer " +
                "will lead them to the best place to hunt and decides to follow discretely... ");

        plot.setFont(new Font("sans", Font.ITALIC, 20));
        plot.setWrapStyleWord(true);
        plot.setLineWrap(true);
        plot.setEditable(false);

        plot.setBorder(styledBorder());

        mainPanel.add(plot);

        // get player name buttons
        JPanel buttonPanel = getPanel(player);
        mainPanel.add(buttonPanel);

        // start game button
        JButton start = new JButton("Start Game");

        start.addActionListener(l -> {
            if (player[0].getName().isEmpty() | player[1].getName().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter names first");
            } else {
                System.out.println(getClass() + ": start game");
                new PlayGame(frame, player, 0);
            }
        });

        mainPanel.add(start);

        frame.add(mainPanel);

        // set first player to explorer
        player[0].isExplorer = true;

        // call GameObjects to place objects on each players' board
        for (Player p : player) objects.spawn(p);

        frame.repaint();
        frame.revalidate();
        frame.setVisible(true);
    }

    /**
     * create panel that will get details of each player
     * @param player array of players
     * @return completed panel
     */
    private JPanel getPanel(Player[] player) {
        JPanel buttonPanel = new JPanel(new GridLayout(0,2));

        JButton playerOneName = new JButton("Player One");
        playerOneName.addActionListener(l -> {
            String name = JOptionPane.showInputDialog(null, "Enter the name of the Mystical Explorer",
                    "Mystical Explorer", JOptionPane.INFORMATION_MESSAGE);

            if (name != null) {
                player[0].setName(name);
                System.out.println(getClass() + ": set p1 name: " + name);
            }
        });

        JButton playerTwoName = new JButton("Player Two");
        playerTwoName.addActionListener(l -> {
            String name = JOptionPane.showInputDialog(null, "Enter the name of the Evil Hunter",
                    "Evil Hunter", JOptionPane.INFORMATION_MESSAGE);

            if (name != null) {
                player[1].setName(name);
                System.out.println(getClass() + ": set p2 name: " + name);
            }
        });

        buttonPanel.add(playerOneName);
        buttonPanel.add(playerTwoName);
        return buttonPanel;
    }

    /**
     * style borders for any component
     * @return styled border
     */
    private static Border styledBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createBevelBorder(0),
                        BorderFactory.createLineBorder(Color.GRAY))
        );
    }
}
