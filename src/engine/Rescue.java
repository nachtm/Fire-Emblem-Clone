package engine;

import common.Utils;

import java.util.function.Predicate;

/**
 * Created by Micah on 4/27/2018.
 */
public class Rescue implements IAction {

    private IGameEngine eng;

    public Rescue(IGameEngine eng){
        this.eng = eng;
    }
    /**
     * We can rescue if selected.aid >= target.constitution + 2
     * for some target next to selected.
     * @param selected the selected character
     * @param current the current state
     * @return
     */
    @Override
    public boolean isValid(IUnit selected, IState current) {
        return !selected.isRescued() &&
                !selected.isRescuing().isPresent() &&
                current.getUnits().stream()
                .filter(unit -> Location.manhattanDistance(unit.getLocation(), selected.getLocation()) == 1)
                .anyMatch(canBeRescuedBy(selected));
    }

    @Override
    public boolean perform(IUnit selected, IState current) {
        Location targetLoc = eng.promptForLocation();
        while(current.getUnits().stream().noneMatch(Utils.isAtLocation(targetLoc))
                && current.getUnits().stream().filter(Utils.isAtLocation(targetLoc)).anyMatch(canBeRescuedBy(selected))){
            targetLoc = eng.promptForLocation();
        }

        IUnit toRescue = current.getUnits().stream()
                .filter(Utils.isAtLocation(targetLoc))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No unit at the selected location."));

        toRescue.setRescued(true);
        selected.setRescuing(toRescue);
        toRescue.setLocation(selected.getLocation());
        return true;
    }

    private Predicate<? super IUnit> canBeRescuedBy(IUnit rescuer){
        return (unit -> rescuer.getStats().getAid() >= unit.getStats().getConstitution() + 2);
    }

}
