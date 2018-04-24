package engine;

/**
 * Created by Micah on 4/24/2018.
 */
public class EndUnitTurn implements IAction {

    private static final EndUnitTurn instance = new EndUnitTurn();
    private EndUnitTurn(){}

    public static EndUnitTurn getInstance(){
        return instance;
    }

    @Override
    public boolean isValid(IUnit selected, IState current) {
        return current.getUnmovedUnits().contains(selected);
    }

    @Override
    public boolean perform(IUnit selected, IState current) {
        current.registerMove(selected);
        return false;
    }
}
