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
        int fish = 7;
        int crab = 2;
        int seaSnake = 3;
        int starfish = 2;

        // spawn creatures - start with the biggest so they fit
        for (int i = 0; i < starfish; i++)
            starfish(p);
        removeBuffers(p);

        for (int i = 0; i < crab; i++)
            crab(p);
        removeBuffers(p);

        for (int i = 0; i < seaSnake; i++)
            seaSnake(p);
        removeBuffers(p);

        for (int i = 0; i < fish; i++)
            fish(p);
        removeBuffers(p);

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
            int len = 4;    // with buffer
            int area = 4;

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
                    if (x == startCol + 1 || x == startCol + 2) {
                        p.getTemp_board()[startRow][x] = 'F'; // saves layout to temp before valid to save time
                    }
                    else
                        p.temp_board[startRow][x] = 'b';
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
            int hei = 4;    // with buffer
            int len = 4;    // with buffer
            int area = 16;

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
                        if ((x == startCol + 1 || x == startCol + 2) && (y == startRow + 1 || y == startRow + 2)) {
                            p.temp_board[y][x] = 'C';
                        }
                        else
                            p.temp_board[y][x] = 'b';   // to distinguish that this is a buffer
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
            int len = 6;
            int area = 6;

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
                        if (x != startCol && x != endCol-1) {
                            p.temp_board[y][x] = 'S';
                        }
                        else
                            p.temp_board[y][x] = 'b';   // distinguish buffer
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
            int hei = 5;
            int len = 5;
            int area = 25;

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
                        if (y == startRow + 1 && x == startCol + 2)
                            p.getTemp_board()[y][x] = 'O';  // print top bit
                        else if (y == startRow + 2 && (x != startCol && x != endCol - 1))
                            p.getTemp_board()[y][x] = 'O';  // print middle bit
                        else if (y == startRow + 3 && x == startCol + 2)
                            p.getTemp_board()[y][x] = 'O';  // print bottom bit
                        else
                            p.temp_board[y][x] = 'b';
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

    /**
     * removes buffers from the hidden board
     * @param p player
     */
    private void removeBuffers(Player p) {
        for (int i = 0; i < p.height; i++)
            for (int j = 0; j < p.length - 1; j++)
                if (p.getHidden_board()[i][j] == 'b')
                    p.setHidden_board(i,j,'~');
    }
}