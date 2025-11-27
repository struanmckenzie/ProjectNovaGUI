/**
 * a class which is used to spawn game objects on a board
 *
 * @author Struan McKenzie
 * @version 1.2
 */

public class GameObjects {

    /**
     * allows programmer to choose how many of each
     * object to place on the player's board
     * @param p player
     */
    public void spawn(Player p) {
        // num of each object to spawn
        int fish = 4;
        int crab = 2;
        int seaSnake = 2;
        int starfish = 3;

        // spawn creatures
        for (int i = 0; i < fish; i++)
            fish(p);
        for (int i = 0; i < crab; i++)
            crab(p);
        for (int i = 0; i < seaSnake; i++)
            seaSnake(p);
        for (int i = 0; i < starfish; i++)
            starfish(p);
    }

    /**
     * places a fish on the player's board
     * @param p player
     */
    private void fish(Player p) {

        boolean valid = false;

        // loop until fish spawned in valid location
        while (!valid) {
            // fields of a fish
            int hei = 1;
            int len = 2;
            int area = 2;

            // make sure temporary board is the same as the hidden one to begin
            for (int i = 0; i < (p.height); i++)
                System.arraycopy(p.getHidden_board()[i], 0, p.getTemp_board()[i], 0, p.length);

            // gen start of object, with limits to account for size of object
            int startRow = (int) (Math.random() * (p.height - hei));
            int startCol = (int) (Math.random() * (p.length - len));

            int endCol = (startCol + len);

            int count = 0;
            for (int x = startCol; x < endCol; x++) {
                if (p.getHidden_board()[startRow][x] == '~') {
                    p.getTemp_board()[startRow][x] = 'F'; // saves layout to temp before valid to save time
                    count++;
                } else
                    // stop early if there isn't space
                    break;
            }

            // test to make sure the lil fish can fit in the random space
            if (count == area) {
                p.setHidden_board(p.getTemp_board());
                valid = true;
            }
        }
    }

    /**
     * places a crab on the player's board
     * @param p player
     */
    private void crab(Player p) {
        boolean valid = false;

        // loop until fish spawned in valid location
        while (!valid) {
            // fields of a crab
            int hei = 2;
            int len = 2;
            int area = 4;

            // make sure temporary board is the same as the hidden one to begin
            for (int i = 0; i < (p.height); i++)
                System.arraycopy(p.getHidden_board()[i], 0, p.getTemp_board()[i], 0, p.length);

            int startRow = (int) (Math.random() * (p.height - hei));
            int startCol = (int) (Math.random() * (p.length - len));

            // translate to actual spaces on the board
            int endRow = (startRow + hei);
            int endCol = (startCol + len);

            // make sure there is space for a crab
            int count = 0;
            for (int y = startRow; y < endRow; y++)
                for (int x = startCol; x < endCol; x++) {
                    if (p.getHidden_board()[y][x] == '~') {
                        p.getTemp_board()[y][x] = 'C';  // I have literally no idea why this works
                        count++;
                    } else
                        // stop early if there isn't space
                        break;
                }

            if (count == area) {
                p.setHidden_board(p.getTemp_board());
                valid = true;
            }
        }
    }

    /**
     * places a sea snake on the player's board
     * @param p player
     */
    private void seaSnake(Player p) {
        boolean valid = false;
        while (!valid) {
            // fields of a sea snake
            int hei = 1;
            int len = 4;
            int area = 4;

            // make sure temporary board is the same as the hidden one to begin
            for (int i = 0; i < (p.height); i++)
                System.arraycopy(p.getHidden_board()[i], 0, p.getTemp_board()[i], 0, p.length);

            int startRow = (int) (Math.random() * (p.height - hei));
            int startCol = (int) (Math.random() * (p.length - len));

            // translate to actual end spaces
            int endRow = (startRow + hei);
            int endCol = (startCol + len);

            // make sure there is space for a sea snake
            int count = 0;
            for (int y = startRow; y < endRow; y++)
                for (int x = startCol; x < endCol; x++) {
                    if (p.getHidden_board()[y][x] == '~') {
                        p.getTemp_board()[y][x] = 'S';
                        count++;
                    } else
                        // stop early if there isn't space
                        break;
                }

            if (count == area) {
                p.setHidden_board(p.getTemp_board());
                valid = true;
            }
        }
    }

    /**
     * places a starfish on the player's board
     * @param p player
     */
    private void starfish(Player p) {
        boolean generated = false;
        while (!generated) {
            // fields of a starfish
            int hei = 3;
            int len = 3;
            int area = 9;

            // make sure temporary board is the same as the hidden one to begin
            for (int i = 0; i < (p.height); i++)
                System.arraycopy(p.getHidden_board()[i], 0, p.getTemp_board()[i], 0, p.length);

            int startRow = (int) (Math.random() * (p.height - hei));
            int startCol = (int) (Math.random() * (p.length - len));

            // translate to actual end spaces
            int endRow = (startRow + hei);
            int endCol = (startCol + len);

            // make sure there is space for a starfish
            int count = 0;
            for (int y = startRow; y < endRow; y++)
                for (int x = startCol; x < endCol; x++) {

                    if (p.getHidden_board()[y][x] == '~') {
                        if (y == startRow && x == startCol + 1)
                            p.getTemp_board()[y][x] = 'O';  // print top bit
                        else if (y == startRow + 1)
                            p.getTemp_board()[y][x] = 'O';  // print middle bit
                        else if (y == startRow + 2 && x == startCol + 1)
                            p.getTemp_board()[y][x] = 'O';  // print bottom bit
                        count++;
                    } else
                        // stop early if there isn't space
                        break;
                }

            if (count == area) {
                p.setHidden_board(p.getTemp_board());
                generated = true;
            }
        }
    }
}