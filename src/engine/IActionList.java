package engine;

import java.util.List;

/**
 * Created by Micah on 4/24/2018.
 */
public interface IActionList {

    /**
     * For a given character, return all of the actions that character can take.
     * @param selected the character performing the action
     * @param current the current state
     * @return a list of actions that the character can perform
     */
    List<IAction> getValidActions(IUnit selected, IState current);

}
