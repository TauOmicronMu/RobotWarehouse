package warehouse;

/**
 * Created by samtebbs on 22/02/2016.
 */
public abstract class Action {

    public static class MoveAction extends Action {

        public boolean moveInX;
        public int distance;
        public Location destination;

    }

    public static class PickupAction extends Action {

        public ItemPickup pickup;

    }

}
