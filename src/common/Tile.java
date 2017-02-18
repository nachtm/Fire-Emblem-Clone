package common;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.event.*;

//we'll have to clean up reduntant methods -- I made tile extend JButton after
//designing most of the class... :-/
public class Tile extends JLabel{
	public static final String[] MOVE_DICT = {"Foot","Armor","Knight 1",
					"Knight 2", "Nomad", "Nomad Trooper", "Fighter",
					"Mage", "Flier", "Bandit", "Pirate"};
	public static final int TILE_SIZE = 16;

	private String name;
	private String imgPath;
	private int avoidMod;
	private int defMod;
	private int[] moveCost;
	private String special;
	private boolean editor;

	/**
	 * Please ensure that editor is true if we're working inside the map editor and 
	 * false otherwise.
	 */
	public Tile(ImageIcon picture, String name, int avoid, int def, int[] movement,
					String special, boolean editor, String imgPath){
		super(picture);
		if(movement.length == MOVE_DICT.length){
			this.name = name;
			this.avoidMod = avoid;
			this.defMod = def;
			this.moveCost = movement;
			this.special = special;
			this.editor = editor; 
			this.setPreferredSize(new Dimension(16,16));
			this.imgPath = imgPath;
			this.setBackground(Color.RED);
		} else{
			System.out.println("Did not create tile. Array moveCost is wrong length.");
		}
	}

	public int getAvoid(){
		return avoidMod;
	}

	public int getDef(){
		return defMod;
	}

	public int getMove(String classType){
		for(int i = 0; i < MOVE_DICT.length; i++){
			if(classType.equals(MOVE_DICT[i])){
				return moveCost[i];
			}
		} return -1;
	}

	public int getMove(int i){
		return moveCost[i];
	}

	public String getSpecial(){
		return special;
	}

	public void setSource(String s){
		imgPath = s;
	}

	public String getSource(){
		return imgPath;
	}
	
	public Image getImage(){
		return ((ImageIcon) this.getIcon()).getImage();
	}	

	public String toString(){
		StringBuilder toReturn = new StringBuilder();
		toReturn.append(name+"/"+imgPath+"/"+avoidMod+"/"+defMod+"/"+moveCost[0]);
		for(int i = 1; i < moveCost.length; i++){
			toReturn.append(","+moveCost[i]);
		}
		toReturn.append("/"+special);
		return toReturn.toString();
	}
}