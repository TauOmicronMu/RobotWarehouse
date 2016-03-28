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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemPickup that = (ItemPickup) o;

        if (itemCount != that.itemCount) return false;
        if (Double.compare(that.reward, reward) != 0) return false;
        if (Double.compare(that.weight, weight) != 0) return false;
        if (!itemName.equals(that.itemName)) return false;
        return !(location != null ? !location.equals(that.location) : that.location != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = itemName.hashCode();
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + itemCount;
        temp = Double.doubleToLongBits(reward);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
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
