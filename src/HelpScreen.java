/**
 * displays a screen with details on how to play the game with the gui
 */

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.awt.*;

public class HelpScreen {
    // fields
    private JFrame frame;

    /**
     * constructor
     * @param frame main frame
     */
    public HelpScreen(JFrame frame) {
        this.frame = frame;

        // empty frame for new screen
        frame.getContentPane().removeAll();

        frame.setTitle("Help Page");
        startHelp();
    }

    private void startHelp() {
        // menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu navigation = new JMenu("Navigation");
        JMenuItem rtrn = new JMenuItem("Main Menu");
        navigation.add(rtrn);
        menuBar.add(navigation);

        rtrn.addActionListener(l -> new MainMenu(frame));
        frame.setJMenuBar(menuBar);

        // main panel setup
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // the text to display
        JTextArea info = new JTextArea("""
                The aim of the game is to find all the creatures before your opponent.
                If the game ends early the player with the most points wins and the
                current winner is displayed on the pause menu before quitting.
                
                You can save and quit from the pause menu.
                
                Enter the coordinates in the form: xy
                where 'x' is the letter on the x axis
                and 'y' is the letter on the y axis
                
                5 points are earned for every part of a creature found
                5 bonus points are earned once a whole creature is found
                Bombs take away 25HP
                Seaweed adds 15HP (effectively acts as a shield if you have 100HP)
                MegaGuess reveals a whole row
                
                If you are under 25HP you will be give the opportunity to buy a hint
                with your points which will reveal the board for 10 seconds.
                Use this time wisely
                
                | Objects ID key:                                                        |
                |    Fish | Sea Snake | Crab  | Starfish | Bomb | Seaweed | MegaGuess    |
                |  -------+-----------+-------+----------+------+---------+------------  |
                |    F F  |  S S S S  |  C C  |    O     |  B   |    H    |     M        |
                |         |           |  C C  |  O O O   |      |         |              |
                |         |           |       |    O     |      |         |              |
                """);
        info.setFont(new Font("sans", Font.ITALIC, 20));
        info.setWrapStyleWord(true);
        info.setEditable(false);

        // scroll pane
        JScrollPane infoPane = new JScrollPane(info);
        infoPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        infoPane.getVerticalScrollBar().setUnitIncrement(20);

        mainPanel.add(infoPane);
        frame.add(mainPanel);

        frame.repaint();
        frame.revalidate();
        frame.setVisible(true);
    }
}
