import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

public class Map{
	private List<List<Tile>> mapArray;
	private int width;
	private int height;
	private ImageIcon testPic = null;
	private boolean testing = false;
	private String tilesetPath;

	public Map(int width, int height, String tilesetPath){
		mapArray = new ArrayList<List<Tile>>();
		this.width = width;
		this.height = 0; //when we add a row it'll automatically update height.
		this.tilesetPath = tilesetPath;
	}

	// public Map(int width, int height, ImageIcon test){
	// 	this(width, height);
	// 	testPic = test;
	// 	testing = true;
	// }

	// public List<List<Tile>> getMap(){
	// 	return mapArray;
	// }

	public int getWidth(){
		return width;
	}

	public int getHeight(){
		return height;
	}

	public void addRow(List<Tile> tList) throws UnsupportedOperationException{
		if(tList.size() != width){
			throw new UnsupportedOperationException("Width of new row doesn't match dimensions of map. mapW:"+width+" newWidth:"+tList.size());
		}
		mapArray.add(tList);
		height++;
	}

	public void addColumn(List<Tile> tList) throws UnsupportedOperationException{
		if(tList.size() != height){
			throw new UnsupportedOperationException("Height of new column doesn't match dimensions of map");
		}
		for(int i = 0; i < mapArray.size(); i++){
			mapArray.get(i).add(tList.get(i));
		}
		width++;
	}

	public void removeRow(int row) throws IndexOutOfBoundsException{
		if(row >= height || row < 0){
			throw new IndexOutOfBoundsException("That row doesn't exist.");
		}
		mapArray.remove(row);
		height--;
	}

	public void removeColumn(int column)throws IndexOutOfBoundsException{
		if(column >= width || column < 0){
			throw new IndexOutOfBoundsException("That column doesn't exist");
		}
		for(int i = 0; i < mapArray.size(); i++){
			mapArray.get(i).remove(column);
		}
		width--;
	}

	public Tile getTile(int x, int y){
		return mapArray.get(y).get(x);
	}
	
	public void setTestImage(ImageIcon test){
		testPic = test;
	}

	public String toString(){
		if(!testing){
			StringBuilder toReturn = new StringBuilder();
			toReturn.append(tilesetPath+"\n");
			toReturn.append(width+" " + height+"\n");
			for(int y = 0; y < mapArray.size(); y++){
				for(int x = 0; x < mapArray.get(y).size(); x++){
					toReturn.append(mapArray.get(y).get(x).toString());
					toReturn.append(" ");
				} toReturn.append("\n");
			}
			return toReturn.toString();		
		} else{
			StringBuilder toReturn = new StringBuilder();
			for(int y = 0; y < mapArray.size(); y++){
				for(int x = 0; x < mapArray.get(y).size(); x++){
					if(mapArray.get(y).get(x).getIcon().equals(testPic)){
						toReturn.append("O ");
					} else{
						toReturn.append("X ");
					} 
				} toReturn.append("\n");
			}
			return toReturn.toString();
		}
	}

	public Set<Location> getNeighbors(Location l){
		Set<Location> toReturn = new HashSet<Location>();
		int x = l.getX();
		int y = l.getY();
		if(x > 0){
			toReturn.add(new Location(x-1,y));
		} if(x < width - 1){
			toReturn.add(new Location(x+1,y));
		} if(y > 0){
			toReturn.add(new Location(x,y-1));
		} if(y < height - 1){
			toReturn.add(new Location(x,y+1));
		} return toReturn;
	}
}