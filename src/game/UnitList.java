package game;

import common.Location;
import game.Unit;

import java.util.*;

public class UnitList{
	private List<Unit> player;
	private List<Unit> enemy;
	private List<Unit> ally;
	private java.util.Map<Location, Unit> occupiedSquares;
	private List<List<Unit>> unitList;

	public UnitList(){
		player = new ArrayList<Unit>();
		enemy = new ArrayList<Unit>();
		ally = new ArrayList<Unit>();
		occupiedSquares = new HashMap<>();
		unitList = new ArrayList<List<Unit>>();
		unitList.add(Unit.PLAYER, player);
		unitList.add(Unit.ENEMY, enemy);
		unitList.add(Unit.ALLY, ally);
	}

	public void addUnit(Unit u){
		unitList.get(u.getAlliegance()).add(u);
		occupiedSquares.put(u.getLocation(), u);
	}

	public List<List<Unit>> getAllUnits(){
		return unitList;
	}
	public List<Unit> getUnitsByAllieg(int alliegance){
		return unitList.get(alliegance);
	}

	public Unit getUnit(Location l){
		return occupiedSquares.get(l);
	}

	public boolean squareOccupied(Location l){
		return occupiedSquares.containsKey(l);
	}

	public void moveUnit(Unit u, Location target){
		if(!occupiedSquares.containsKey(target)){
			occupiedSquares.remove(u.getLocation());
			u.move(target.getX(), target.getY());
			occupiedSquares.put(target, u);
		}
	}
}