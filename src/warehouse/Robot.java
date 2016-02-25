package warehouse;

/**
 * Created by samtebbs on 22/02/2016.
 */
public class Robot {

    private String robotName;
    private Location position;
    private boolean busy;
    
    public Robot(String name){
    	
    	this.robotName = name;
    	this.busy = false;
    }
    
    public String getName(){
    	
    	return this.robotName;
    }
    
    public Location getPosition(){
    	
    	return this.position;
    }
    
    public boolean getBusy(){
    	
    	return this.busy;
    }
    
    public void setBusy(boolean bool){
    	
    	this.busy = bool;
    }
}
