import javax.swing.*;
import java.awt.*;

public class EndScreen {
    private final JFrame frame;
    private final Player[] players;
    private final int current;    // the current player

    /**
     * default constructor
     * @param frame main frame
     */
    public EndScreen(JFrame frame, int endType, Player[] players, int current) {
        this.frame = frame;
        this.players = players;
        this.current = current;

        frame.getContentPane().removeAll();
        
        // select corresponding end screen
        switch (endType) {
            case 0 -> deathEnd();
            case 1 -> winEnd();
        }
    }
    
    private void winEnd() {
        JPanel mainPanel = new JPanel(new GridLayout(2, 0));

        // end message text
        JTextArea text;
        
        if (players[current].isExplorer)
            text = endText(players[current].getName() + " is the winner!\n" + """
                        As the Mystical Explorer you managed to save all the creatures from the hunter.
                        """);
        else
            text = endText(players[current].getName() + " is the winner!\n" + """
                        As the Evil Hunter you have managed to hunt all the creatures to extinction.
                        """);

        mainPanel.add(text);

        // get and display both player's stats
        JPanel stats = new JPanel(new GridLayout(0, 2));

        // add stats to panel
        stats.add(getStats(0));
        stats.add(getStats(1));
        mainPanel.add(stats);
        
        frame.add(mainPanel);

        frame.repaint();
        frame.revalidate();
        frame.setVisible(true);
    }

    /**
     * end screen to display if one of the players dies
     */
    private void deathEnd() {
        JPanel mainPanel = new JPanel(new GridLayout(2, 0));

        // end message text
        JTextArea text;

        // add correct message to end screen
        if (players[current].isExplorer)
            text = endText("""
                        You failed to save the rest of the creatures from the Evil Hunter
                        who continued on and managed to hunt down
                        all the creatures and wins by default.""");
        else
            text = endText("""
                        You failed to hunt all the creatures down.
                        The Mystical Explorer went on to save the rest
                        of the creatures and wins by default.""");

        mainPanel.add(text);

        // get and display both player's stats
        JPanel stats = new JPanel(new GridLayout(0, 2));
        
        // add stats to panel
        stats.add(getStats(0));
        stats.add(getStats(1));
        mainPanel.add(stats);

        frame.add(mainPanel);

        frame.repaint();
        frame.revalidate();
        frame.setVisible(true);
    }

    /**
     * get a text area
     * @param text the mmessage
     * @return text area
     */
    private JTextArea endText(String text) {
        JTextArea t = new JTextArea(text);
        t.setEditable(false);
        return t;
    }

    /**
     * gets players stats and puts them in a text area
     * @param p player
     * @return stats text
     */
    private JTextArea getStats(int p) {
        return new JTextArea("Player: " + players[p].getName()
                        +"\nPoints: "+ players[p].getPoints()
                        +"\nHealth: "+ players[p].getHealth());
    }
}
