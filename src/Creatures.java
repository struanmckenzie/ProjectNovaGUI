public class Creatures {

    public void spawn(Player p) {
        // spawn creatures
        fish(p);
        crab(p);
    }

    private void fish(Player p) {
        int numParts = 2;   // number of parts which make up a fish perimeter

        boolean valid = false;
        while (!valid) {    // loop until fish spawned in valid location
            int lat = (int) (Math.random() * 15);   // math.random is exclusive
            int lon = (int) (Math.random() * 19);

            int validCount = 0;

            for (int j = lon; j < 2; j++)
                if (p.hidden_board[lat][j] == '#') {
                    p.temp_board[lat][j] = 'F';
                    validCount++;
                }

            if (validCount == numParts) {
                p.setHidden_board(p.temp_board);
                valid = true;
            }
        }
    }

    private void crab(Player p) {
        int numParts = 4;   // number of parts which make up a crab perimeter

        boolean valid = false;
        while (!valid) {    // loop until fish spawned in valid location
            int lat = (int) (Math.random() * 14);   // math.random is exclusive
            int lon = (int) (Math.random() * 19);

            int validCount = 0;

            // make sure there is space for a crab
            for (int i = lat; i < 2; i++)
                for (int j = lon; j < 2; j++)
                    if (p.hidden_board[i][j] == '#') {
                        p.temp_board[i][j] = 'C';
                        validCount++;
                    }

            if (validCount == numParts) {
                p.setHidden_board(p.temp_board);
                valid = true;
            }
        }
    }
}
