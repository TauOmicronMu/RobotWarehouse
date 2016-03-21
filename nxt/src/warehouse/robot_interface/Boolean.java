package warehouse.robot_interface;


public class Boolean
{
	private boolean b;

	public Boolean(boolean b)
	{
		this.b = b;
	}

	public Boolean(boolean b, boolean secondary)
	{
		this.b = b;
	}

	public boolean get()
	{
		return b;
	}

	public void set(boolean b)
	{
		this.b = b;
	}
}
