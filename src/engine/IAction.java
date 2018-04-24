package engine;

/**
 * Created by Micah on 4/24/2018.
 */
public interface IAction {

    /**
     * Can the selected character take the action, given the current state?
     * @param selected the selected character
     * @param current the current state
     * @return True if this is a valid action
     */
    boolean isValid(IUnit selected, IState current);

    /**
     * Have the character perform the current action, possibly with input from the user.
     * @param selected the selected character
     * @param current the current state
     * @return True if the unit still can perform more actions, false otherwise.
     */
    boolean perform(IUnit selected, IState current);

}
