package warehouse.util;

/**
 * Created by samtebbs on 03/03/2016.
 */
public enum Direction {

    NORTH, EAST, SOUTH, WEST;

    public Direction turnRight() {
        int i = ordinal() + 1;
        if (i >= values().length) i = 0;
        return values()[i];
    }

    public Direction turnLeft() {
        int i = ordinal() - 1;
        if (i < 0) i = values().length - 1;
        return values()[i];
    }

}
