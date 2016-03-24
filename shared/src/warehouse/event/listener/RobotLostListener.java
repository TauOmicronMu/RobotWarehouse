package warehouse.event.listener;

import warehouse.event.RobotLostEvent;

/**
 * Created by sxt567 on 23/03/16.
 */
public interface RobotLostListener {
    public void onRobotLost(RobotLostEvent e);
}
