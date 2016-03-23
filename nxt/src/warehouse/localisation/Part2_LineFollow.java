package warehouse.localisation;
import lejos.nxt.LightSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

// TODO: Auto-generated Javadoc
/**
 * The Class Part2_LineFollow for following the line behaviour
 */
public class Part2_LineFollow extends Thread {

	/** The pilot. */
	DifferentialPilot pilot;
	
	/** The left light sensor. */
	private LightSensor left;
	
	/** The right light senosr. */
	private LightSensor right;
	
	/**constant*/
	private final float constant = -7f;

	/**
	 * Instantiates a new part2_ line follow.
	 *
	 * @param pilot the pilot
	 * @param left the left light sensor
	 * @param right the right light sensor
	 */
	public Part2_LineFollow(DifferentialPilot pilot, LightSensor left, LightSensor right) {
		super();
		this.pilot = pilot;
		this.left = left;
		this.right = right;
		left.setHigh(400);
		left.setLow(150);
		right.setHigh(400);
		right.setLow(150);
		pilot.setTravelSpeed(5);
		pilot.setRotateSpeed(300);
		

	}

	

	/* action method of the class
	 * 
	 */
	@Override
	public void run() {
		//settings

		pilot.travel(0.5f, true);
		while ((!(left.readValue() < 75) && !(right.readValue() < 75))) {

			//error
			int error2 = left.readValue() - right.readValue();
			
			//travelling based on error
			pilot.steer(constant * error2);
			Delay.msDelay(25);

		}
		pilot.stop();
	}

}
