import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

public class PaletteLoader{
	private static final int PLAYER = 0;
	private static final int ENEMY = 1;
	private static final int ALLY = 2;
	private static final int GRAY = 3;
	private static final int[] INDICES = {PLAYER, ENEMY, ALLY, GRAY};
	private static final String[] FILEPATHS = {"Sprites/Palettes/Player.png", 
		"Sprites/Palettes/Enemy.png", "Sprites/Palettes/Ally.png", 
		"Sprites/Palettes/Gray.png"};
	private Color[] playerPalette = new Color[15];
	private Color[] enemyPalette = new Color[15];
	private Color[] allyPalette = new Color[15];
	private Color[] grayPalette = new Color[15];
	private List<Color[]> palettes = new ArrayList<Color[]>();

	public PaletteLoader(){
		palettes.add(playerPalette);
		palettes.add(enemyPalette);
		palettes.add(allyPalette);
		palettes.add(grayPalette);

		try{
			for(int i = 0; i < FILEPATHS.length; i++){
				BufferedImage temp = ImageIO.read(new File(FILEPATHS[i]));
				for(int x = 0; x < temp.getWidth() / 8; x++){
					Color c = new Color(temp.getRGB(x*8, 0));
					palettes.get(i)[x] = c;
					//System.out.println(c);
				}
			}
		} catch(IOException e){
			System.out.println(e);
			System.out.println("while trying to load the palettes.");
		}
	}

	public Color[] getPlayer(){
		return playerPalette;
	}

	public Color[] getEnemy(){
		return enemyPalette;
	}

	public Color[] getAlly(){
		return allyPalette;
	}

	public Color[] getGray(){
		return grayPalette;
	}

	public static void main(String[] args){
		PaletteLoader p = new PaletteLoader();
		System.out.println("Success...?");
	}
}