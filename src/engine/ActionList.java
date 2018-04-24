package engine;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Micah on 4/24/2018.
 */
public class ActionList implements IActionList {

    private List<IAction> allActions;

    public ActionList(List<IAction> allActions){
        this.allActions = allActions;
    }

    @Override
    public List<IAction> getValidActions(IUnit selected, IState current) {
        return allActions.stream().filter(action -> action.isValid(selected, current)).collect(Collectors.toList());
    }
}
