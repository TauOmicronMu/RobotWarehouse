package warehouse.event;

import warehouse.util.Direction;

public class StartingCoordinatesEvent {

    private int x, y;
    private Direction startingDirection;

    public StartingCoordinatesEvent(int x, int y, Direction d)
    {
        this.x = x;
        this.y = y;
        this.startingDirection = d;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public Direction getStartingDirection()
    {
        return startingDirection;
    }
}
