package engine;

import java.util.List;

/**
 * Created by Micah on 4/24/2018.
 */
public interface IState {

    List<IUnit> getUnits();

    /**
     * @return a list of characters that haven't moved yet this turn.
     */
    List<IUnit> getUnmovedUnits();

    /**
     * Register a unit as having taken actions this turn.
     */
    void registerMove(IUnit moved);

    void endTurn();

    ITerrainMap getMap();
}
