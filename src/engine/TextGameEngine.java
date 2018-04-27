package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Micah on 4/24/2018.
 */
public class TextGameEngine implements IGameEngine {

    IActionList actionList;
    IState state;
    Scanner userIn;

    public TextGameEngine(){
        this.state = initializeState();
        this.actionList = createActionList();
        userIn = new Scanner(System.in);
    }

    private IActionList createActionList(){
        List<IAction> alToBe = new ArrayList<>();
        alToBe.add(Move.getInstance(this));
        alToBe.add(DummyAttack.getInstance(this));
        alToBe.add(EndUnitTurn.getInstance());
        alToBe.add(new Rescue(this));
        return new ActionList(alToBe);
    }

    private IState initializeState(){
        List<IUnit> units = new ArrayList<>();
        IStats unitStats = new Stats(1, 0, 20, 20, 5,
                0, 2, 0, 1, 0, 5, 10, 7);
        units.add(new Unit(unitStats, new Location(0, 0)));
        units.add(new Unit(unitStats, new Location(5,5)));

        //give and equip weapons
        units.stream().forEach(unit -> {
            unit.addToInventory(new DummyWeapon(1));
            unit.equip(0);
        });
        return new State(units, new TerrainMap());
    }

    @Override
    public Location promptForLocation() {
        System.out.print("X? ");
        int x = userIn.nextInt();
        System.out.print("Y? ");
        int y = userIn.nextInt();
        return new Location(x, y);
    }

    public void run() {
        System.out.println("Starting game.");
        int turnCounter = 0;

        //game loop
        while(true) {
            System.out.println("Turn " + turnCounter);
            turnCounter++;
            //turn loop
            while (true) {
                System.out.println("Select a character, or type n to end turn:");
                List<IUnit> unitChoices = state.getUnmovedUnits();
                for (int i = 0; i < unitChoices.size(); i++) {
                    System.out.println(i + " " + unitChoices.get(i));
                }
                String selection = userIn.next();
                if (selection.equalsIgnoreCase("n")) {
                    break;
                }
                IUnit selected = unitChoices.get(Integer.parseInt(selection));
                boolean selectedCanMove = true;

                //unit action loop
                while (selectedCanMove) {
                    List<IAction> validActions = actionList.getValidActions(selected, state);
                    System.out.println("Select an action:");
                    for (int i = 0; i < validActions.size(); i++) {
                        System.out.println(i + " " + validActions.get(i));
                    }
                    selectedCanMove = validActions.get(userIn.nextInt()).perform(selected, state);

                    System.out.println("Action completed.");
                }
            }

            state.endTurn();
        }
    }

    public static void main(String[] args){
        IGameEngine eng = new TextGameEngine();
        eng.run();
    }
}
