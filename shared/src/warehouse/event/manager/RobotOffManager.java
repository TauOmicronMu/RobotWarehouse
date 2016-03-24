package warehouse.event.manager;

import warehouse.event.RobotOffEvent;
import warehouse.event.listener.RobotOffListener;

/**
 * Created by sxt567 on 24/03/16.
 */
public class RobotOffManager extends RobotEventManager<RobotOffListener, RobotOffEvent> {

    @Override
    public void each(RobotOffListener robotOffListener, RobotOffEvent robotOffEvent) {
        robotOffListener.onRobotOff(robotOffEvent);
    }
}
