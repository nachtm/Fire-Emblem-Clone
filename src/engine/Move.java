package engine;

import java.util.function.Predicate;

/**
 * Created by Micah on 4/24/2018.
 */
public class Move implements IAction {

    private IGameEngine eng;

    //We only want one Move instance at a time
    private static final Move instance = new Move();
    public Move(){}

    public static Move getInstance(IGameEngine eng){
        instance.eng = eng;
        return instance;
    }

    @Override
    public boolean isValid(IUnit selected, IState current) {
        return !selected.isRescued() &&
                !selected.hasMoved();
    }

    @Override
    public boolean perform(IUnit selected, IState current) {
        Location target = eng.promptForLocation();
        //TODO: don't allow moving on top of each other.
        while(getMoveDistance(selected.getLocation(), target) >= selected.getStats().getMove() ||
                current.getUnits().stream().anyMatch(hasSameLocation(target))){
            target = eng.promptForLocation();
        }
        selected.setLocation(target);
        if(selected.isRescuing().isPresent()){
            selected.isRescuing().get().setLocation(selected.getLocation());
        }
        selected.setMovedTrue();
        return true;
    }

    private Predicate<? super IUnit> hasSameLocation(Location targetLocation){
        return (unit -> unit.getLocation().equals(targetLocation));
    }

    //this'll get more complicated once we add in terrain
    private int getMoveDistance(Location from, Location to){
        return Location.manhattanDistance(from, to);
    }
}
