package warehouse.event;

import warehouse.action.Action;
import warehouse.util.Robot;

/**
 * Created by samtebbs on 22/03/2016.
 */
public class ActionCompleteEvent extends Event {
    public final Action action;

    public ActionCompleteEvent(Robot robot, Action action) {
        super(robot);
        this.action = action;
    }

    /*
     * "ActionComplete,robot.toPacketString(),action"
     */
    public String toPacketString() {
        String s = "";
        s += "ActionComplete";
        s += ",";
        s += robot.toPacketString();
        s += ",";
        s += action.toPacketString();
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionCompleteEvent that = (ActionCompleteEvent) o;

        return action.equals(that.action);

    }

    @Override
    public int hashCode() {
        return action.hashCode();
    }
}
