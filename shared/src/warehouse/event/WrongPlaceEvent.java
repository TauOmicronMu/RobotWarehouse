package warehouse.event;

/**
 * Event called when a robot is in the wrong place
 * @author txs
 *
 */
public class WrongPlaceEvent
{

import warehouse.util.Robot;

/**
 * Created by samtebbs on 22/03/2016.
 */
public class WrongPlaceEvent extends Event {

    protected WrongPlaceEvent(Robot robot) {
        super(robot);
    }
}
