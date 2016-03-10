package route_execution;

import java.util.ArrayList;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

public class RouteExecution {

	private DifferentialPilot pilot;
	private int delay;
	private ArrayList<Integer> direction;
	private ArrayList<Integer> moves;
	public int xCoord;
	public int yCoord;
	public int xStart;
	public int yStart;
	public static final int STRAIGHT = 2;
	public static final int RIGHT = 0;
	public static final int LEFT = 1;

	
	public RouteExecution(DifferentialPilot pilot, int xStart , int yStart) {
		this.pilot = pilot;
		direction = new ArrayList<Integer>();
		this.xStart = xStart;
		this.yStart = yStart;
		this.xCoord = xStart;
		this.yCoord = yStart;
		
	}

	public void run() {
		while (!Button.ESCAPE.isDown()) {
			pilot.setTravelSpeed(0.2);
			if (direction.get(0) == RIGHT)
			{
				pilot.rotate(90);
				pilot.travel(0.1);
				moves.add(RIGHT);
			} 
			else
			{
					if (direction.get(0) == LEFT)
					{
						pilot.rotate(-90);
						pilot.travel(0.1);
						moves.add(LEFT);
					}
					else
					{
							pilot.travel(0.1);
							moves.add(STRAIGHT);
					}
					
			}
			direction.remove(0);
			Delay.msDelay(delay);
	}
	
}
	
	public void addDirection(int move) 
	{
		direction.add(move);
	}
	
	public int lastMove()
	{
		return moves.get(moves.size());
	}
		
	public void revertMoves()
	{
		xCoord = xStart;
		yCoord = yStart;
		
		while(moves.get(0) != null)
		{
			if (moves.get(0) == RIGHT)
			{
				pilot.travel(-0.1);
				pilot.rotate(-90);
			} 
			else
			{
					if (moves.get(0) == LEFT)
					{
						pilot.travel(-0.1);
						pilot.rotate(90);
					}
					else
					{
							pilot.travel(-0.1);
					}
					
			}
			moves.remove(0);
			Delay.msDelay(delay);
		}
	}

	public static void main(String[] args) {
		RouteExecution robot = new RouteExecution(new DifferentialPilot(0.056, 0.12, Motor.B, Motor.C), 0, 0);
		robot.addDirection(RouteExecution.STRAIGHT);
		robot.addDirection(RouteExecution.STRAIGHT);
		robot.addDirection(RouteExecution.LEFT);
		robot.addDirection(RouteExecution.LEFT);
		robot.addDirection(RouteExecution.RIGHT);
		robot.addDirection(RouteExecution.RIGHT);
		robot.run();
	}

}
