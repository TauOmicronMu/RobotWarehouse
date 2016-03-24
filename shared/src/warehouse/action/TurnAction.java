package warehouse.action;

public class TurnAction extends Action {

    public double angle;
    
    public TurnAction(double angle) {
        this.angle = angle;
    }

    @Override
    public String toPacketString() {
        return "Turn" + "," + angle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TurnAction that = (TurnAction) o;

        return Double.compare(that.angle, angle) == 0;

    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(angle);
        return (int) (temp ^ (temp >>> 32));
    }
}
