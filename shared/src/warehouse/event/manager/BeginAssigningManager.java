package warehouse.event.manager;

import warehouse.event.BeginAssigningEvent;
import warehouse.event.listener.BeginAssigningListener;

/**
 * Created by sxt567 on 23/03/16.
 */
public class BeginAssigningManager extends RobotEventManager<BeginAssigningListener, BeginAssigningEvent> {
    @Override
    public void each(BeginAssigningListener beginAssigningListener, BeginAssigningEvent beginAssigningEvent) {
        beginAssigningListener.onBeginAssigning(beginAssigningEvent);
    }
}
