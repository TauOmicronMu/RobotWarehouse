package warehouse.action;

import warehouse.util.Location;

/**
 * Created by samtebbs on 05/03/2016.
 */
public class MoveAction extends Action {

    public int distance;
    public Location destination;
    public double speed;

    public MoveAction(int distance, Location destination) {
        this.distance = distance;
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "Move";
    }
}
