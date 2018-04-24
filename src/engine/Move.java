package engine;

/**
 * Created by Micah on 4/24/2018.
 */
public class Move implements IAction {

    private IGameEngine eng;

    //We only want one Move instance at a time
    private static final Move instance = new Move();
    private Move(){}

    public static Move getInstance(IGameEngine eng){
        instance.eng = eng;
        return instance;
    }

    @Override
    public boolean isValid(IUnit selected, IState current) {
        return !selected.actionsPerformedThisTurn().contains(this);
    }

    @Override
    public boolean perform(IUnit selected, IState current) {
        Location target = eng.promptForLocation();
        while(getDistance(selected.getLocation(), target) >= selected.getStats().getMove()){
            target = eng.promptForLocation();
        }
        selected.setLocation(target);
        selected.registerAction(this);
        return true;
    }

    //this'll get more complicated once we add in terrain
    private int getDistance(Location from, Location to){
        return Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
    }
}
