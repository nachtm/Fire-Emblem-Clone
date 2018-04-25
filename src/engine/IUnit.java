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

    /**
     * @return a list of all the actions that this character has performed during this turn.
     */
    List<IAction> actionsPerformedThisTurn();

    /**
     * Register performed as having happened this turn.
     * @param performed the action performed.
     */
    void registerAction(IAction performed);

    Location getLocation();

    void setLocation(Location newSpot);

    void endTurn();

    List<IItem> getInventory();

    void addToInventory(IItem toAdd);

    void equip(int index);

    Optional<IWeapon> getEquipped();
}
