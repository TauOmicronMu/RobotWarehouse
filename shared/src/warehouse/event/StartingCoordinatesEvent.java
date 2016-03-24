package warehouse.event;

import warehouse.util.Direction;

public class StartingCoordinatesEvent {
<<<<<<< HEAD
	
	private int x, y;
	private Direction startingDirection;
	
	public StartingCoordinatesEvent(int x, int y, Direction d)
	{
		this.x = x;
		this.y = y;
		this.startingDirection = d;
	}
	
	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}
	
	public Direction getStartingDirection()
	{
		return startingDirection;
	}
=======

    private int x, y;
    private Direction startingDirection;

    public StartingCoordinatesEvent(int x, int y, Direction d)
    {
        this.x = x;
        this.y = y;
        this.startingDirection = d;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public Direction getStartingDirection()
    {
        return startingDirection;
    }
>>>>>>> f9880e34f9ec67cde3372b76601c80975d4624f6
}
