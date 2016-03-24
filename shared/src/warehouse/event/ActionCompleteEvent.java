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
}
