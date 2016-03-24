package warehouse.util;

/**
 * Created by samtebbs on 22/02/2016.
 */
public class ItemPickup {

    public String itemName;
    public Location location;
    public int itemCount;
    public double reward, weight;

    public ItemPickup(String itemName, Location location, int itemCount, double reward, double weight) {
        this.itemName = itemName;
        this.location = location;
        this.itemCount = itemCount;
        this.reward = reward;
        this.weight = weight;
    }

    public ItemPickup(String itemName, Location location, int itemCount) {
        this.itemName = itemName;
        this.location = location;
        this.itemCount = itemCount;
    }

    @Override
    public Object clone() {
        return new ItemPickup(itemName, location, itemCount, reward, weight);
    }

    @Override
    public String toString() {
        return "{" +
                "itemName='" + itemName + '\'' +
                ", location=" + location +
                ", itemCount=" + itemCount +
                ", reward=" + reward +
                ", weight=" + weight +
                '}';
    }

    public String toPacketString() {
        String s = "";
        s += itemName;
        s += ",";
        s += location.toPacketString();
        s += ",";
        s += itemCount;
        return s;
    }
}
