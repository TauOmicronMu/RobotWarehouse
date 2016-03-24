package warehouse.event.manager;

import samtebbs33.event.EventManager;

/**
 * Created by sxt567 on 23/03/16.
 */
public abstract class RobotEventManager<E, T> extends EventManager<E> {

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

    public void onEvent(T t) {
        for(E e : listeners) each(e, t);
    }

    public abstract void each(E e, T t);
}
