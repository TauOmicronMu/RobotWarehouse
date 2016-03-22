package warehouse.robot_interface2;

import com.sun.istack.internal.NotNull;
import warehouse.event.*;
import warehouse.job.AssignedJob;
import warehouse.util.EventDispatcher;
import warehouse.util.ItemPickup;
import warehouse.util.Subscriber;

/**
 * Created by samtebbs on 22/03/2016.
 */
public class RobotInterface {

    public final static String[] menuItems = {"Lost", "Cancel"};
    public final static ButtonListener listener = new ButtonListener() {
        @Override
        public void buttonPressed(Button b) {
            if(b == Button.ENTER && currentPickup != null) {
                if(itemCount == 1) {
                    currentPickup = null;
                    EventDispatcher.onEvent2(new PickupCompleteEvent(job.robot, currentPickup));
                } else {
                    currentPickup.itemCount--;
                    updateItemCount();
                }
            } else {
                switch (b) {
                    case Button.LEFT:
                        selectionIndex = selectionIndex == 0 ? menuItems.length - 1 : selectionIndex - 1;
                        break;
                    case Button.RIGHT:
                        selectionIndex = selectionIndex == menuItems.length - 1 ? 0 : selectionIndex + 1;
                        break;
                    case Button.ENTER:
                        executeMenuItem();
                        break;
                    default:
                        return;
                }
                updateJobMenu();
            }
        }
    };

    static {
        EventDispatcher.subscribe2(RobotInterface.class);
        Button.addListener(listener);
    }

    private static void executeMenuItem() {
        switch(menuItems[selectionIndex]) {
            case "Lost":
                EventDispatcher.onEvent2(new RobotLostEvent(job.robot));
                clear();
                print("Robot is lost", 1);
                reset();
                break;
            case "Cancel":
                EventDispatcher.onEvent2(new JobCancellationEvent(job));
                break;
        }
    }

    public static ItemPickup currentPickup;
    public static AssignedJob job;
    public static int selectionIndex = 0;

    private static void clear() {
        //TODO
    }

    private static void updateItemCount() {
        clear();
        print(String.format("Please add %d %s", currentPickup.itemCount, currentPickup.itemName), 1);
    }

    private static void print(@NotNull Object obj, int y) {
        LCD.drawString(1, y, obj.toString());
    }

    @Subscriber
    public static void onJobCancelled(JobCancellationEvent event) {
        clear();
        print("Job was cancelled", 1);
        reset();
    }

    private static void reset() {
        currentPickup = null;
        job = null;
    }

    @Subscriber
    public static void onJobAssigned(JobAssignedEvent event) {
        job = event.assignedJob;
        updateJobMenu(0;
    }

    private static void updateJobMenu() {
        clear();
        print("Executing job: " + job.id, 0);
        for(int i = 0; i < menuItems.length; i++) print((selectionIndex == i ? "-> " : "") + menuItems[i], i + 2);
    }

    @Subscriber
    public static void onDropoffReached(DropOffReachedEvent event) {
        EventDispatcher.onEvent2(new JobCompleteEvent(job));
        reset();
    }

    @Subscriber
    public static void onPickupReached(PickupReachedEvent event) {
        currentPickup = event.pickup;
        updateItemCount();
    }

}
