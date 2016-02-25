package warehouse;

/**
 * Created by samtebbs on 22/02/2016.
 * 
 * Modified by Owen on 25/02/2016:
 * 
 * - Added constructor to intialise fields
 * - Made fields private and added get and set methods
 * - Added toString method for debugging
 * - Added javadocs
 */
public class Location {

    private int x, y;
    
    /**
     * Create a location with x and y coordinates.
     * 
     * @param x int for x coordinate
     * @param y int for y coordinate
     */
    public Location(int x, int y){
    	
    	this.x = x;
    	this.y = y;
    }

    /**
     * To String method for debugging.
     */
    public String toString(){
    	
    	return "Location (" + this.x + ", " + this.y + ")"; 
    }
    
    /**
     * Get method for the x coordinate.
     * 
     * @return x coordinate as an int
     */
    public int getX(){
    	
    	return this.x;
    }
    
    /**
     * Get method for the y coordinate.
     *
     * @return y coordinate as an int
     */
    public int getY(){
    	
    	return this.y;
    }
    
    /**
     * Set method for the x coordinate.
     * 
     * @param x the new integer x coordinate
     */
    public void setX(int x){
    	
    	this.x = x;
    }
    
    /**
     * Set method for the y coordinate.
     * 
     * @param y the new integer y coordinate
     */
    public void setY(int y){
    	
    	this.y = y;
    }
}
