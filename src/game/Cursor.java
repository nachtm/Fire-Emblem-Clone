package game;

import common.Location;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Cursor{
	public static String FILEPATH = "Sprites/Cursor.png";
	private Image img;
	private int x;
	private int y;

	public Cursor(int scale){
		try{
			//img = MapBuilder.scaleImage(ImageIO.read(new File(FILEPATH)), scale);
			BufferedImage temp = ImageIO.read(new File(FILEPATH));
			img = temp.getScaledInstance(temp.getWidth()*scale, temp.getHeight()*scale, 
				Image.SCALE_SMOOTH);
		} catch (IOException e){
		}
		x = 0;
		y = 0;
	}

	public Location getLocation(){
		return new Location(x,y);
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	public void setLocation(Location l){
		x = l.getX();
		y = l.getY();
	}

	public Image getImage(){
		return img;
	}
}
