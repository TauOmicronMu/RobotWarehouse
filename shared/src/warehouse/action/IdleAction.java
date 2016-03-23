package warehouse.action;

/**
 * Created by samtebbs on 05/03/2016.
 */
public class IdleAction extends Action {

    public int time;

    public IdleAction(int time) {
        this.time = time;
    }

    @Override
    public String toPacketString() {
        return "Idle";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdleAction that = (IdleAction) o;

        return time == that.time;

    }

    @Override
    public int hashCode() {
        return time;
    }
}
