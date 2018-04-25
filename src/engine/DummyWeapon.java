package engine;

/**
 * Created by Micah on 4/24/2018.
 */
public class DummyWeapon implements IWeapon {

    private int range;

    public DummyWeapon(int range){
        this.range = range;
    }

    @Override
    public int getRange() {
        return this.range;
    }
}
