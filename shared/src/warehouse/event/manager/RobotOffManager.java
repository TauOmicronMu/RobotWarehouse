package warehouse.event.manager;

import warehouse.event.RobotOffEvent;
import warehouse.event.listener.RobotOffListener;

/**
 * Created by sxt567 on 24/03/16.
 */
public class RobotOffManager extends RobotEventManager<RobotOffListener, RobotOffEvent> {

    public static ActionCompleteManager actionCompleteManager = new ActionCompleteManager();
    public static BeginAssigningManager beginAssigningManager = new BeginAssigningManager();
    public static DropOffReachedManager dropOffReachedManager = new DropOffReachedManager();
    public static JobAssignedManager jobAssignedManager = new JobAssignedManager();
    public static JobCancellationMAnager jobCancellationMAnager = new JobCancellationMAnager();
    public static JobCompleteManager jobCompleteManager = new JobCompleteManager();
    public static PickupCompleteManager pickupCompleteManager = new PickupCompleteManager();
    public static PickupReachedManager pickupReachedManager = new PickupReachedManager();
    public static RobotLostManager robotLostManager = new RobotLostManager();
    public static RobotOffManager robotOffManager = new RobotOffManager();
    public static WrongPlaceManager wrongPlaceManager = new WrongPlaceManager();

    @Override
    public void each(RobotOffListener robotOffListener, RobotOffEvent robotOffEvent) {
        robotOffListener.onRobotOff(robotOffEvent);
    }
}
