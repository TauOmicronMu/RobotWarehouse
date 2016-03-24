package warehouse.localisation;
	
import lejos.nxt.LightSensor;

/**
 * The Class LSlListenerTest to listen for the light sensors to be on the black tape
 */
public class LSlListenerTest extends Thread {

	/** The robot unit. */
	private LineFollowTest robotUnit;
	
	/** The right. */
	private LightSensor right;
	
	/** The left. */
	private LightSensor left;
	
	/** The stop. */
	public static boolean stop = true;

	/**
	 * Instantiates a new listener 
	 *
	 * @param robotUnit the robot unit
	 * @param right the right
	 * @param left the left
	 */
	public LSlListenerTest(LineFollowTest robotUnit, LightSensor right, LightSensor left) {
		this.robotUnit = robotUnit;
		this.right = right;
		this.left = left;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		int rightlightValue;
		int leftlightValue;
		while(stop) {
			rightlightValue = right.getLightValue();
			leftlightValue = left.getLightValue();
			robotUnit.leftSensorValue = leftlightValue;
			robotUnit.rightSensorValue = rightlightValue;
		}
	}
}
