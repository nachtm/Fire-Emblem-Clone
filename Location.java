public class Location{
	private int x;
	private int y;

	public Location(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public boolean equals(Object other){
		if(other instanceof Location){
			return (x == ((Location)other).getX() && y == ((Location)other).getY());
		} return false;
	}

	public int hashCode(){
		return x * 31 + y;
	}

	public String toString(){
		return ""+x+" "+y;
	}
}