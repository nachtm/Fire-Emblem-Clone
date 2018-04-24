package engine;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Micah on 4/24/2018.
 */
public class Unit implements IUnit {

    private IStats stats;
    private List<IAction> actionsThisTurn;
    private Location location;

    public Unit(IStats stats, Location location){
        this.stats = stats;
        this.actionsThisTurn = new LinkedList<>();
        this.location = location;
    }

    @Override
    public IStats getStats() {
        return stats;
    }

    @Override
    public List<IAction> actionsPerformedThisTurn() {
        return actionsThisTurn;
    }

    @Override
    public void registerAction(IAction performed) {
        actionsThisTurn.add(performed);
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location newSpot) {
        this.location = newSpot;
    }

    @Override
    public void endTurn() {
        actionsThisTurn.clear();
    }

}
