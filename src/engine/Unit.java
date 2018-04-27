package engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Micah on 4/24/2018.
 */
public class Unit implements IUnit {

    private IStats stats;
    private List<IAction> actionsThisTurn;
    private Location location;
    private List<IItem> items;
    private Optional<IWeapon> equipped;
    private boolean hasMoved;
    private boolean isRescued;
    private boolean hasAttacked;

    public Unit(IStats stats, Location location){
        this.stats = stats;
        this.actionsThisTurn = new LinkedList<>();
        this.location = location;
        this.equipped = Optional.empty();
        this.items = new ArrayList<>();
    }

    @Override
    public IStats getStats() {
        return stats;
    }

    @Override
    public boolean hasMoved() {
        return hasMoved;
    }

    @Override
    public void setMovedTrue() {
        hasMoved = true;
    }

    @Override
    public boolean hasAttacked() {
        return hasAttacked;
    }

    @Override
    public void setAttackedTrue() {
        hasAttacked = true;
    }

    @Override
    public boolean isRescued() {
        return isRescued;
    }

    @Override
    public void setRescued(boolean val) {
        isRescued  = val;
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

    @Override
    public List<IItem> getInventory() {
        return items;
    }

    @Override
    public void addToInventory(IItem toAdd) {
        items.add(toAdd);
    }

    @Override
    public void equip(int index) {
        if(items.get(index) instanceof IWeapon){
            equipped = Optional.of((IWeapon) items.get(index));
        } else {
            throw new IllegalArgumentException("Cannot equip an item that isn't a weapon!");
        }
    }

    @Override
    public Optional<IWeapon> getEquipped() {
        return equipped;
    }

    @Override
    public String toString(){
        return location + " " + this.stats.getHp() + "/" + this.stats.getMaxhp() + (isRescued() ? " (R)" : "");
    }

}
