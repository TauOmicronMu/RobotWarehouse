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
    public String toPacketString() {
        return "Move";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoveAction that = (MoveAction) o;

        if (distance != that.distance) return false;
        if (Double.compare(that.speed, speed) != 0) return false;
        return destination != null ? destination.equals(that.destination) : that.destination == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = distance;
        result = 31 * result + (destination != null ? destination.hashCode() : 0);
        temp = Double.doubleToLongBits(speed);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
