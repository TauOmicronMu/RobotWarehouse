package warehouse;

/**
 * Created by samtebbs on 22/02/2016.
 */
public class Location {

    private int x, y;

    public String toString(){
    	
    	return "Location (" + this.x + ", " + this.y + ")"; 
    }
    
    public int getX(){
    	
    	return this.x;
    }
    
    public int getY(){
    	
    	return this.y;
    }
    
    public void setX(int x){
    	
    	this.x = x;
    }
    
    public void setY(int y){
    	
    	this.y = y;
    }
}
