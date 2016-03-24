package warehouse.robot_interface2;

import com.sun.istack.internal.NotNull;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import warehouse.event.DropOffReachedEvent;
import warehouse.event.JobAssignedEvent;
import warehouse.event.JobCancellationEvent;
import warehouse.event.JobCompleteEvent;
import warehouse.event.PickupCompleteEvent;
import warehouse.event.PickupReachedEvent;
import warehouse.event.RobotLostEvent;
import warehouse.job.AssignedJob;
import warehouse.util.EventDispatcher;
import warehouse.util.ItemPickup;
import warehouse.util.Subscriber;

/**
 * Created by samtebbs on 22/03/2016.
 */
public class RobotInterface {

	public final static String[] menuItems = { "Lost", "Cancel" };
	public final static ButtonListener listener = new ButtonListener() {
		@Override
		public void buttonPressed(Button b) {
			if (b == Button.ENTER && currentPickup != null) {
				if (currentPickup.itemCount == 1) {
					currentPickup = null;
					EventDispatcher.onEvent2(new PickupCompleteEvent(job.robot, currentPickup));
				} else {
					currentPickup.itemCount--;
					updateItemCount();
				}
			} else {
				switch (b.getId()) {
				case Button.ID_LEFT:
					selectionIndex = selectionIndex == 0 ? menuItems.length - 1 : selectionIndex - 1;
					break;
				case Button.ID_RIGHT:
					selectionIndex = selectionIndex == menuItems.length - 1 ? 0 : selectionIndex + 1;
					break;
				case Button.ID_ENTER:
					executeMenuItem();
					break;
				default:
					return;
				}
				updateJobMenu();
			}
		}

		@Override
		public void buttonReleased(Button b) {
			// TODO Auto-generated method stub

		}
	};

	static {
		EventDispatcher.subscribe2(RobotInterface.class);
		Button.ENTER.addButtonListener(listener);
		Button.LEFT.addButtonListener(listener);
		Button.RIGHT.addButtonListener(listener);
	}

	private static void executeMenuItem() {
		String foundString = menuItems[selectionIndex];
		if (foundString.equals("Lost")) {
			EventDispatcher.onEvent2(new RobotLostEvent(job.robot));
			clear();
			print("Robot is lost", 1);
			reset();
		} else {
			EventDispatcher.onEvent2(new JobCancellationEvent(job));
		}
	}

	public static ItemPickup currentPickup;
	public static AssignedJob job;
	public static int selectionIndex = 0;

	private static void clear() {
		// TODO
	}

	private static void updateItemCount() {
		clear();
		print(String.format("Please add %d %s", currentPickup.itemCount, currentPickup.itemName), 1);
	}

	private static void print(@NotNull Object obj, int y) {
		LCD.drawString(obj.toString(), y, 1);
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
		updateJobMenu();
	}

	private static void updateJobMenu() {
		clear();
		print("Executing job: " + job.id, 0);
		for (int i = 0; i < menuItems.length; i++)
			print((selectionIndex == i ? "-> " : "") + menuItems[i], i + 2);
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
