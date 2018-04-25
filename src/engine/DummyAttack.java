package engine;

import java.util.function.Predicate;

/**
 * Created by Micah on 4/24/2018.
 */
public class DummyAttack implements IAction {
    private IGameEngine eng;

    private static final DummyAttack instance = new DummyAttack();

    private DummyAttack(){}

    public static DummyAttack getInstance(IGameEngine eng){
        instance.eng = eng;
        return instance;
    }

    @Override
    public boolean isValid(IUnit selected, IState current) {
        int range = selected.getEquipped().orElse(new DummyWeapon(0)).getRange();
                //weapon equipped
        return  selected.getEquipped().isPresent() &&
                //haven't attacked
                !selected.actionsPerformedThisTurn().contains(this) &&
                //there's an enemy in range
                current.getUnits().stream().anyMatch(unit ->
                        Location.manhattanDistance(
                                unit.getLocation(), selected.getLocation()) <= range &&
                                !unit.equals(selected));
    }

    @Override
    public boolean perform(IUnit selected, IState current) {
        Location targetLoc = eng.promptForLocation();
        while(current.getUnits().stream().noneMatch(isAtLocation(targetLoc))){
            targetLoc = eng.promptForLocation();
        }

        //perform attack
        IUnit targetUnit = current.getUnits().stream().filter(isAtLocation(targetLoc)).findFirst().get();
        int targetDef = targetUnit.getStats().getDefense();
        int targetStr = targetUnit.getStats().getStrength();
        int targetHp = targetUnit.getStats().getHp();
        int selDef = selected.getStats().getDefense();
        int selStr = selected.getStats().getStrength();
        int selHp = selected.getStats().getHp();
        targetUnit.getStats().setHp(targetHp - (selStr - targetDef));
        selected.getStats().setHp(selHp - (targetStr - selDef));

        //register action
        selected.registerAction(this);
        return true;
    }

    private Predicate<? super IUnit> isAtLocation(Location loc){
        return (unit -> unit.getLocation().equals(loc));
    }
}
