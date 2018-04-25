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

    @Override
    public boolean equals(Object other){
        if(other instanceof Location){
            Location l = (Location) other;
            return x == l.x && y == l.y;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return (7 * x) + (11 * y);
    }

    public static int manhattanDistance(Location from, Location to){
        return Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
    }
}
