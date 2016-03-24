package warehouse.event.manager;

import warehouse.event.RobotLostEvent;
import warehouse.event.listener.RobotLostListener;

/**
 * Created by sxt567 on 24/03/16.
 */
public class RobotLostManager extends RobotEventManager<RobotLostListener, RobotLostEvent> {
    @Override
    public void each(RobotLostListener robotLostListener, RobotLostEvent robotLostEvent) {
        robotLostListener.onRobotLost(robotLostEvent);
    }
}
