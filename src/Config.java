import java.awt.*;

public class Config {
    static final int BOARD_SIZE = 16;

    static final char UNDISCOVERED_TILE = '+';
    static final char GUESSED_TILE = 'x';
    static final char BOMB_TILE = 'B';
    static final char WEED_TILE = 'H';
    static final char MEGAGUESS_TILE = 'M';

    static final char FISH_TILE = 'F';
    static final char SNAKE_TILE = 'S';
    static final char CRAB_TILE = 'C';
    static final char STARFISH_TILE = 'O';

    static final int BOMB_STRENGTH = 25;
    static final int WEED_STRENGTH = 15;
    static final int CREATURE_PART_POINTS = 5;
    static final int CREATURE_FOUND_POINTS = 10;

    static final Color TEXT_COLOUR = Color.BLACK;

}
