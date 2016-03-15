package warehouse.linefollower;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;

public class LineFollower {

	public static final float TRACK_WIDTH = 0.175f, WHEEL_DIAMETER = 0.056f, ROBOT_LENGTH = 0.23f;
	private static int SPEED_CONSTANT = 420;

	public static void main(String[] args) {
		// LEFT SENSOR IS DODGY
		LightSensor left = new LightSensor(SensorPort.S1), right = new LightSensor(SensorPort.S3);
		NXTRegulatedMotor rightMotor = Motor.C, leftMotor = Motor.B;
		while (true) {
			// Get the sensor values
			int rightVal = right.getLightValue(), leftVal = left.getLightValue();
			LCD.drawString("Right: " + rightVal, 4, 0);
			LCD.drawString("Left: " + leftVal, 4, 1);
			int max = Math.max(leftVal, rightVal), min = Math.min(leftVal, rightVal);
			boolean leftIsMax = (max == leftVal);
			// Get percentage difference to decide if a turn is necessary
			double diff = percenDif(max, min);
			LCD.drawString("Diff: " + diff, 4, 2);
			// if the two sensors give a significantly different value, then set the motor speeds proportionally
			if (diff >= 10) {
				if (leftIsMax) {
					rightMotor.setSpeed((int)(SPEED_CONSTANT * 0.1));
				} else {
					leftMotor.setSpeed((int)(SPEED_CONSTANT * 0.1));
				}
			} else {
				rightMotor.setSpeed(SPEED_CONSTANT);
				leftMotor.setSpeed(SPEED_CONSTANT);
			}
			rightMotor.forward();
			leftMotor.forward();
		}
	}

	/**
	 * Returns the percentage difference between two values
	 * @param max
	 * @param min
	 * @return
	 */
	private static double percenDif(int max, int min) {
		return (max - min) / (double) max * 100d;
	}

}
