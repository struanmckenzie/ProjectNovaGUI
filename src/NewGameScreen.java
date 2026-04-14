import javax.swing.*;
import java.awt.*;

public class NewGameScreen {
    private JFrame frame;

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
        JTextArea plot = new JTextArea("""
                The explorer starts on their quest to rescue the last
                remaining underwater creatures.
                The hunter knows the explorer will lead them to the
                best place to hunt and decides to follow discretely...
                """);
        plot.setFont(new Font("sans", Font.ITALIC, 20));

        plot.setEditable(false);
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
                new StartGame(frame, player);
            }
        });

        mainPanel.add(start);

        frame.add(mainPanel);


        // set first player to explorer
        player[0].explorer = true;

        // call GameObjects to place objects on each players' board
        for (Player p : player) objects.spawn(p);

        frame.repaint();
        frame.revalidate();
        frame.setVisible(true);
    }

    private static JPanel getPanel(Player[] player) {
        JPanel buttonPanel = new JPanel(new GridLayout(1,2));

        JButton playerOneName = new JButton("Player One");
        playerOneName.addActionListener(l ->
            player[0].setName(JOptionPane.showInputDialog("Enter the name of the Mystical Explorer")));

        JButton playerTwoName = new JButton("Player Two");
        playerTwoName.addActionListener(l ->
            player[1].setName(JOptionPane.showInputDialog("Enter the name of the Evil Hunter")));

        buttonPanel.add(playerOneName);
        buttonPanel.add(playerTwoName);
        return buttonPanel;
    }
}
