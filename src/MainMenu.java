import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

        /*
         * gets the dimensions of the screen
         * sets the window size to be a percentage of it
         */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // set window size to percentage of screen size
        int height, width;
        width = (int) screenSize.getWidth() * 3/5;
        height = (int) screenSize.getHeight() * 3/5;

        frame.setSize(new Dimension(width, height));
        frame.setTitle("Main Menu");
        launchUI();
    }

    private void launchUI() {
        frame.setJMenuBar(null);


        // configure window frame
        frame.setLocationRelativeTo(null);

        // Main panel to contain sub-panels
        JPanel mainPanel = new JPanel(new GridLayout(0,3));

        // Panel containing image of explorer
        JPanel leftImagePanel = new JPanel();
        loadImage(leftImagePanel, "Images/Explorer.jpg");
        mainPanel.add(leftImagePanel);

        // panel to hold buttons
        JPanel buttonPanel = new JPanel(new GridLayout(4,0));

        // buttons
        JButton newGame = new JButton("New Game");
        JButton loadGame = new JButton("Load Game");
        JButton help = new JButton("Need Help?");
        JButton quit = new JButton("Quit");

        // action listeners
        // new game
        newGame.addActionListener(l -> {
            System.out.println(getClass() + ": new game");
            new NewGameScreen(frame);
        });

        // load game
        loadGame.addActionListener(l -> {
            System.out.println(getClass() + ": load game");
            String saveName = JOptionPane.showInputDialog(null, "Enter name of the save",
                    "Save game", JOptionPane.INFORMATION_MESSAGE);

            loadGame(saveName);
        });

        // help page
        help.addActionListener(l -> {
            System.out.println(getClass() + ": help screen");
            new HelpScreen(frame);
        });

        // quit game
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
            Image scaledImage = image.getScaledInstance(-1, frame.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon finalImage = new ImageIcon(scaledImage);
            JLabel finalImageLabel = new JLabel(finalImage);
            imagePanel.add(finalImageLabel);

            System.out.println(getClass() + ": successfully loaded " + location);
        } catch (NullPointerException e) {
            System.out.println(getClass() + ": failed to load " + location);
        }
    }

    /**
     * loads a previously saved game
     */
    private void loadGame(String saveName) {
        // get name of save and make sure it exists
        saveName = ("Saves/" + saveName + "/");

        File save = new File(saveName + "0" + "Details.txt");
        if (!save.exists()) {
            JOptionPane.showMessageDialog(null, "Save does not exist");
            new MainMenu(frame);
        }

        // initialise player array
        Player[] player = new Player[2];

        for (int i = 0; i < player.length; i++)
            player[i] = new Player();

        int startingPlayer = 0;

        for (int turn = 0; turn < 2; turn++) {
            // declare names of files to load
            String loadBoard = (saveName + turn + "Board.txt");
            String loadHBoard = (saveName + turn + "HiddenBoard.txt");
            String loadDetails = (saveName + turn + "Details.txt");

            FileReader fr;
            BufferedReader br;

            // load details
            try {
                fr = new FileReader(loadDetails);
                br = new BufferedReader(fr);

                player[turn].length = Integer.parseInt(br.readLine());
                player[turn].height = Integer.parseInt(br.readLine());
                player[turn].setName(br.readLine());
                player[turn].setPoints(Integer.parseInt(br.readLine()));
                player[turn].setHealth(Integer.parseInt(br.readLine()));
                player[turn].isExplorer = Boolean.parseBoolean(br.readLine());

                // get the player who should start
                String nextLine = br.readLine();
                if (nextLine != null) {
                    if (nextLine.equals("not null")) startingPlayer = turn;
                }

                // load board
                fr = new FileReader(loadBoard);
                br = new BufferedReader(fr);

                nextLine = br.readLine();
                int i = 0;
                while(nextLine != null) {
                    for (int j = 0; j < player[turn].length; j++)
                        player[turn].setBoard(i, j, nextLine.toCharArray()[j]);

                    nextLine = br.readLine();
                    i++;
                }
                br.close();

                // load hidden board
                fr = new FileReader(loadHBoard);
                br = new BufferedReader(fr);

                nextLine = br.readLine();
                i = 0;
                while (nextLine != null) {
                    for (int j = 0; j < player[turn].length; j++)
                        player[turn].setHidden_board(i, j, nextLine.toCharArray()[j]);

                    nextLine = br.readLine();
                    i++;
                }
                br.close();


            } catch (IOException e) {
                System.out.println("Error: unable to load save");
                new MainMenu(frame);
            }
        }

        // play game
        new PlayGame(frame, player, startingPlayer);
    }
}
