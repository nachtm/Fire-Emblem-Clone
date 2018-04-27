package engine;

import java.util.List;
import java.util.Optional;

/**
 * Created by Micah on 4/24/2018.
 */
public interface IUnit {

    /**
     * Gets the stats for this character
     * @return an IStats object corresponding to this character
     */
    IStats getStats();

    boolean hasMoved();

    void setMovedTrue();

    boolean hasAttacked();

    void setAttackedTrue();

    boolean isRescued();

    void setRescued(boolean val);

    Location getLocation();

    void setLocation(Location newSpot);

    void endTurn();

    List<IItem> getInventory();

    void addToInventory(IItem toAdd);

    void equip(int index);

    Optional<IWeapon> getEquipped();
}
