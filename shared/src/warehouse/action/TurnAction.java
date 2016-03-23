package warehouse.action;

public class TurnAction extends Action {

    public double angle;
    
    public TurnAction(double angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "Turn";
    }
}
