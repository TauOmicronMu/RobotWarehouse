package warehouse.localisation;


import rp.robotics.mapping.GridMap;
import rp.robotics.navigation.Heading;

/**
 * 
 * @author jokLiu
 *
 */
public class Distances {
	
	//height and width of the map
	private int height, width;
	
	//the size of the single cell
	private float cellSize;
	
	//the map
	private GridMap map;
	
	private float[][] mapDistancesPlusX;
	private float[][] mapDistancesMinusX;
	private float[][] mapDistancesPlusY;
	private float[][] mapDistancesMinusY;
	
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
	
	public float getPlusX(int x, int y)
	{
		return mapDistancesPlusX[x][y];
	}
	
	public float getMinusX(int x, int y)
	{
		return mapDistancesMinusX[x][y];
	}
	
	public float getPlusY(int x, int y)
	{
		System.out.println(mapDistancesPlusY[map.getXSize()-1][y]);
		return mapDistancesPlusY[x][y];
	}
	
	public float getMinusY(int x, int y)
	{
		return mapDistancesMinusY[x][y];
	}


}
