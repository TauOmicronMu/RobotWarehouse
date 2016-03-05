package warehouse.action;

import warehouse.util.Location;

/**
 * Created by samtebbs on 05/03/2016.
 */
public class MoveAction extends Action {

    public boolean moveInX;
    public int distance;
    public Location destination;

    public MoveAction(boolean moveInX, int distance, Location destination) {
        this.moveInX = moveInX;
        this.distance = distance;
        this.destination = destination;
    }
}
