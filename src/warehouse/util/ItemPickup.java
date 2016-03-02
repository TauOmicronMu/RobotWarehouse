package warehouse.util;

/**
 * Created by samtebbs on 22/02/2016.
 */
public class ItemPickup {

    public String itemName;
    public Location location;
    public int itemCount;
    public double reward, weight;

    public ItemPickup(String itemName, Location location, int itemCount) {
        this.itemName = itemName;
        this.location = location;
        this.itemCount = itemCount;
    }
}
