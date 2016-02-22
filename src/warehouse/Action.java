package warehouse;

/**
 * Created by samtebbs on 22/02/2016.
 */
public abstract class Action {

    public static class MoveAction extends Action {

        public boolean moveInX;
        public int distance;
        public Location destination;

        public MoveAction(boolean moveInX, int distance, Location destination) {
            this.moveInX = moveInX;
            this.distance = distance;
            this.destination = destination;
        }
    }

    public static class PickupAction extends Action {

        public ItemPickup pickup;

    }

}
