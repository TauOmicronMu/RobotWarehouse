package rp.assignments.ex2;

import java.util.Random;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import rp.config.WheeledRobotConfiguration;
import rp.systems.WheeledRobotSystem;

public class LineFollower {

	private static final boolean isRandomWalk = false; 
	
	private static WheeledRobotSystem robot;
	private static final float TRACK_WIDTH = 0.175f, WHEEL_DIAMETER = 0.056f, ROBOT_LENGTH = 0.23f;
	private static int SPEED_CONSTANT = 350;
	private static int JUNCTION = 41, LEFT = 0, RIGHT = 1, FORWARDS = 2, BACKWARDS = 3;
	private static double SENSOR_WHEEL_DIST = .10;

	public static void main(String[] args) {
		WheeledRobotConfiguration config = new WheeledRobotConfiguration(WHEEL_DIAMETER, TRACK_WIDTH, ROBOT_LENGTH,
				Motor.B, Motor.C);
		robot = new WheeledRobotSystem(config);
		LightSensor left = new LightSensor(SensorPort.S1), right = new LightSensor(SensorPort.S3);
		NXTRegulatedMotor rightMotor = Motor.C, leftMotor = Motor.B;
		
		Button.waitForAnyPress();
		
		while (true) {
			int rightVal = right.getLightValue(), leftVal = left.getLightValue();
			LCD.drawString("Right: " + rightVal, 4, 0);
			LCD.drawString("Left: " + leftVal, 4, 1);
			int max = Math.max(leftVal, rightVal), min = Math.min(leftVal, rightVal);
			boolean leftIsMax = (max == leftVal);
			double diff = percenDif(max, min);
			LCD.drawString("Diff: " + diff, 4, 2);
			
			boolean junction = isRandomWalk ? randomWalk(diff, leftVal, rightVal) : userDecides(diff, leftVal, rightVal);
			// if the two sensors give a significantly different value
			if (!junction && diff >= 10) {
				if (leftIsMax) {
					rightMotor.setSpeed((int) (SPEED_CONSTANT * 0.1));
				} else {
					leftMotor.setSpeed((int) (SPEED_CONSTANT * 0.1));

				}
			} else {
				rightMotor.setSpeed(SPEED_CONSTANT);
				leftMotor.setSpeed(SPEED_CONSTANT);
			}
			rightMotor.forward();
			leftMotor.forward();
		}
	}
	
	private static boolean randomWalk(double diff, int leftVal, int rightVal)
	{
		// if both the sensors are over a line (at a junction)
					if (diff <= 10 && (leftVal < JUNCTION || rightVal < JUNCTION)) {
						robot.getPilot().stop();
						//Delay.msDelay(100);
						LCD.drawString("Junction", 4, 3);
						// ask where to go / decide randomly
						int random = new Random().nextInt(4);
						
						if (random == LEFT)
						{
							robot.getPilot().travel(SENSOR_WHEEL_DIST);
							robot.getPilot().rotate(90);
							robot.getPilot().forward();
						}
						else if (random == RIGHT)
						{
							robot.getPilot().travel(SENSOR_WHEEL_DIST);
							robot.getPilot().rotate(-90);
							robot.getPilot().forward();
						}
						else if (random == FORWARDS)
						{
							robot.getPilot().travel(SENSOR_WHEEL_DIST);
							robot.getPilot().forward();
						}
						// TODO add  backwards too?
						else if (random == BACKWARDS)
						{
							robot.getPilot().travel(SENSOR_WHEEL_DIST);
							robot.getPilot().rotate(-180);
							robot.getPilot().forward();
						}
						LCD.clear();
						return true;
					}
					return false;
	}
	
	private static boolean userDecides(double diff, int leftVal, int rightVal)
	{
		if (diff <= 10 && (leftVal < JUNCTION || rightVal < JUNCTION)) {
			robot.getPilot().stop();
			//Delay.msDelay(100);
			LCD.drawString("Junction", 4, 3);
			Button.waitForAnyPress();				
			if (Button.readButtons() == Button.ID_LEFT)
			{
				robot.getPilot().travel(SENSOR_WHEEL_DIST);
				robot.getPilot().rotate(90);
				robot.getPilot().forward();
			}
			else if (Button.readButtons() == Button.ID_RIGHT)
			{
				robot.getPilot().travel(SENSOR_WHEEL_DIST);
				robot.getPilot().rotate(-90);
				robot.getPilot().forward();
			}
			else if (Button.readButtons() == Button.ID_ENTER)
			{
				robot.getPilot().travel(SENSOR_WHEEL_DIST);
				robot.getPilot().forward();
			}
			else if (Button.readButtons() == Button.ID_ESCAPE)
			{
				robot.getPilot().travel(SENSOR_WHEEL_DIST);
				robot.getPilot().rotate(-180);
				robot.getPilot().forward();
			}
			LCD.clear();
			return true;
		}
		return false;
	}

	/**
	 * Returns the percentage difference between two values
	 * 
	 * @param max
	 * @param min
	 * @return
	 */
	private static double percenDif(int max, int min) {
		return (max - min) / (double) max * 100d;
	}

}
