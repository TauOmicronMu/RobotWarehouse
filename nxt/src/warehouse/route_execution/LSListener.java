package warehouse.route_execution;

import lejos.nxt.LightSensor;


/**Class that updates the values that the 2 sensors read
 * @author Gabriel Iuriciuc
 *
 */
public class LSListener extends Thread {

	private LineFollow robotUnit;
	private LightSensor right;
	private LightSensor left;
	public static boolean stop = true;

	public LSListener(LineFollow robotUnit, LightSensor right, LightSensor left) {
		this.robotUnit = robotUnit;
		this.right = right;
		this.left = left;
	}

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