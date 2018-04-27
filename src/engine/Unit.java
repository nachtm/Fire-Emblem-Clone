package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Micah on 4/24/2018.
 */
public class Unit implements IUnit {

    private IStats stats;
    private Location location;
    private List<IItem> items;
    private Optional<IWeapon> equipped;
    private boolean hasMoved;
    private boolean isRescued;
    private boolean hasAttacked;
    private Optional<IUnit> rescuing;

    public Unit(IStats stats, Location location){
        this.stats = stats;
        this.location = location;
        this.items = new ArrayList<>();
        this.equipped = Optional.empty();
        this.hasMoved = false;
        this.isRescued = false;
        this.hasAttacked = false;
        this.rescuing = Optional.empty();
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
    public Optional<IUnit> isRescuing() {
        return rescuing;
    }

    @Override
    public void setRescuing(IUnit unit) {
        this.rescuing = Optional.of(unit);
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
        this.hasMoved = false;
        this.hasAttacked = false;
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
