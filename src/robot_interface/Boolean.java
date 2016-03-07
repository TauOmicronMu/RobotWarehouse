/**
 * Auxilliary class
 */
public class Boolean
{
	private boolean b;
	private boolean secondary;

	public Boolean(boolean b)
	{
		this.b = b;
		setSecondary(false);
	}

	public Boolean(boolean b, boolean secondary)
	{
		this.b = b;
		this.secondary = secondary;
	}

	public boolean get()
	{
		return b;
	}

	public void set(boolean b)
	{
		this.b = b;
	}

	public boolean getSecondary()
	{
		return secondary;
	}

	public void setSecondary(boolean secondary)
	{
		this.secondary = secondary;
	}
}
