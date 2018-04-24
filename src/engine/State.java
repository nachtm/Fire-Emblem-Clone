package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Micah on 4/24/2018.
 */
public class State implements IState {
    private List<IUnit> allChars;
    private List<IUnit> movedThisTurn;
    private ITerrainMap map;

    public State(List<IUnit> units, ITerrainMap map){
        this.allChars = units;
        this.map = map;
        this.movedThisTurn = new ArrayList<>();
    }

    @Override
    public List<IUnit> getUnits() {
        return allChars;
    }

    @Override
    public List<IUnit> getUnmovedUnits() {
        return allChars.stream().filter(u -> !movedThisTurn.contains(u)).collect(Collectors.toList());
    }

    @Override
    public void registerMove(IUnit moved) {
        if(!movedThisTurn.contains(moved)){
            movedThisTurn.add(moved);
        }
    }

    @Override
    public void endTurn() {
        movedThisTurn.clear();
        for(IUnit u : allChars){
            u.endTurn();
        }
    }

    @Override
    public ITerrainMap getMap() {
        return map;
    }
}
