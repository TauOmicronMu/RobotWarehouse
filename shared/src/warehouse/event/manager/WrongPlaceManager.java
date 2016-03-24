package warehouse.event.manager;

import warehouse.event.WrongPlaceEvent;
import warehouse.event.listener.WrongPlaceListener;

/**
 * Created by sxt567 on 24/03/16.
 */
public class WrongPlaceManager extends RobotEventManager<WrongPlaceListener, WrongPlaceEvent> {
    @Override
    public void each(WrongPlaceListener wrongPlaceListener, WrongPlaceEvent wrongPlaceEvent) {
        wrongPlaceListener.onWrongPlace(wrongPlaceEvent);
    }
}
