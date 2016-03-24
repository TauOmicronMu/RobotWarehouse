package warehouse.localisation;

import lejos.nxt.LightSensor;

public class LSlListenerTest extends Thread {

	private LineFollowTest robotUnit;
	private LightSensor right;
	private LightSensor left;
	public static boolean stop = true;

	public LSlListenerTest(LineFollowTest robotUnit, LightSensor right, LightSensor left) {
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
