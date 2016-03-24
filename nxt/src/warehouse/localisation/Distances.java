package warehouse.localisation;



import rp.robotics.mapping.GridMap;
import rp.robotics.navigation.Heading;

/**
 * The Class Distances to represent distances from each point of the grid
 *
 * @author jokLiu
 */
public class Distances {
	
	//height and width of the map
	private int height, width;
	
	
	//the size of the single cell
	private float cellSize;
	

	//the actual map
	private GridMap map;
	
	/** The map distances when robot is facing in plus x direction. */
	private float[][] mapDistancesPlusX;
	
	/** The map distances when robot is facing in minus x direction. */
	private float[][] mapDistancesMinusX;
	
	/** The map distances when robot is facing in plus y direction. */
	private float[][] mapDistancesPlusY;
	
	/** The map distances when robot is facing in minus y direction. */
	private float[][] mapDistancesMinusY;
	
	/**
	 * Instantiates a new distances.
	 *
	 * @param map the map
	 */
	public Distances(GridMap map)
	{
		this.map = map;
		this.height = map.getYSize();
		this.width = map.getXSize();
		this.cellSize = map.getCellSize();
		mapDistancesPlusX = new float[map.getXSize()][map.getYSize()];
		mapDistancesMinusX =new float[map.getXSize()][map.getYSize()];
		mapDistancesPlusY = new float[map.getXSize()][map.getYSize()];
		mapDistancesMinusY= new float[map.getXSize()][map.getYSize()];
		setDistancesPlusX();
	}
	
	/**
	 * Sets the distances to all the directions
	 */
	private void setDistancesPlusX()
	{
		for(int j=0; j< map.getYSize(); j++)
		
		{
			for(int i=0; i<map.getXSize(); i++)
			{
				
				mapDistancesPlusX[i][j] = map.rangeToObstacleFromGridPosition(i, j, Heading.toDegrees(Heading.PLUS_X));
				 mapDistancesMinusX[i][j] = map.rangeToObstacleFromGridPosition(i, j, Heading.toDegrees(Heading.MINUS_X));
				 mapDistancesPlusY[i][j] = map.rangeToObstacleFromGridPosition(i, j, Heading.toDegrees(Heading.PLUS_Y));
				 mapDistancesMinusY[i][j] = map.rangeToObstacleFromGridPosition(i, j, Heading.toDegrees(Heading.MINUS_Y));
				
			}
			
		}
	}
	
	/**
	 * Gets the plus x direction distance
	 *
	 * @param x the x
	 * @param y the y
	 * @return the plus x
	 */
	public float getPlusX(int x, int y)
	{
		return mapDistancesPlusX[x][y];
	}
	
	/**
	 * Gets the minus x direction distance
	 *
	 * @param x the x
	 * @param y the y
	 * @return the minus x
	 */
	public float getMinusX(int x, int y)
	{
		return mapDistancesMinusX[x][y];
	}
	
	/**
	 * Gets the plus y direction distance
	 *
	 * @param x the x
	 * @param y the y
	 * @return the plus y
	 */
	public float getPlusY(int x, int y)
	{
		return mapDistancesPlusY[x][y];
	}
	
	/**
	 * Gets the minus y direction distance
	 *
	 * @param x the x
	 * @param y the y
	 * @return the minus y
	 */
	public float getMinusY(int x, int y)
	{
		return mapDistancesMinusY[x][y];
	}


}
