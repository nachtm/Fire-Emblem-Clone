package engine;

/**
 * Created by Micah on 4/24/2018.
 */
public class Location {
    public final int x;
    public final int y;

    public Location(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString(){
        return "<" + x + ", " + y + ">";
    }
}
