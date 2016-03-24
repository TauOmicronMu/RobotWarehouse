package warehouse.event.manager;

import samtebbs33.event.EventManager;
import warehouse.event.ActionCompleteEvent;
import warehouse.event.listener.ActionCompleteListener;

/**
 * Created by sxt567 on 23/03/16.
 */
public class ActionCompleteManager extends RobotEventManager<ActionCompleteListener, ActionCompleteEvent> {

    @Override
    public void each(ActionCompleteListener l, ActionCompleteEvent actionCompleteEvent) {
        l.onActionComplete(actionCompleteEvent);
    }
}
