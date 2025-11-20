/**
 * a class which is used to spawn creatures on a board
 *
 * @author Struan McKenzie
 * @version 1.0
 */

public class Creatures {

    public void spawn(Player p) {
        // spawn creatures
        fish(p);
        crab(p);
    }

    private void fish(Player p) {
        // fields of a fish
        int len = 2;
        int hei = 1;
        int nParts = 2;

        boolean valid = false;

        // loop until fish spawned in valid location
        while (!valid) {
            // gen start of object, with limits to account for size of object
            int row = (int) (Math.random() * (p.height - hei));
            int col = (int) (Math.random() * (p.length - len));

            len = col+2;    // convert to spaces on board

            int cnt = 0;
            for (int j = col; j < len; j++)
                if (p.hidden_board[row][j] == '#') {
                    p.temp_board[row][j] = 'F'; // saves layout to temp before valid to save time
                    cnt++;
                }

            // test to make sure the lil fish can fit in the random space
            if (cnt == nParts) {
                p.setHidden_board(p.temp_board);
                valid = true;
            }
        }
    }

    private void crab(Player p) {
        // fields of a crab
        int len = 2;
        int hei = 2;
        int nParts = 4;

        boolean valid = false;

        // loop until fish spawned in valid location
        while (!valid) {
            int row = (int) (Math.random() * (p.height - hei));
            int col = (int) (Math.random() * (p.length - len));

            // convert to spaces on board
            len = col+2;
            hei = row+2;

            // make sure there is space for a crab
            int cnt = 0;
            for (int i = row; i < len; i++)
                for (int j = col; j < hei; j++)
                    if (p.hidden_board[i][j] == '#') {
                        p.temp_board[i][j] = 'C';
                        cnt++;
                    }

            if (cnt == nParts) {
                p.setHidden_board(p.temp_board);
                valid = true;
            }
        }
    }
}
