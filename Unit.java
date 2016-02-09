import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

public class Unit{
	public static final int PLAYER = 0;
	public static final int ENEMY = 1;
	public static final int ALLY = 2; 
	public static final int MAX_INVENTORY = 5;	//right now nothing actually limits
												//inventory size, should be fixed
	// private static Color[] ENEMY_PALETTE = {new Color(104,72,96),new Color(192,168,184),
	// 	new Color(224,224,224),new Color(112,96,96),new Color(176,144,88),new Color(248,248,208),
	// 	new Color(96,40,32),new Color(168,48,40),new Color(224,16,16),new Color(248,80,72),
	// 	new Color(56,208,48),new Color(248,248,64),new Color(128,136,112),new Color(248,248,248),
	// 	new Color(64,56,56)};
	// public static final Color[] ALLY_PALETTE = {new Color(88,72,120),new Color(144,184,232),
	// 	new Color(216,232,240),new Color(112,96,96),new Color(176,144,88),new Color(248,248,208),
	// 	new Color(56,56,144),new Color(56,80,224),new Color(40,160,248),new Color(24,240,248),
	// 	new Color(232,16,24),new Color(248,248,64),new Color(128,136,112),new Color(248,248,248),
	// 	new Color(64,56,56)};
	private PaletteLoader paletteLoader;
	private Color[] defaultPalette;
	private Stats stats;
	private Stats growthRates;
	private Image battleSprite;
	private int x;
	private int y;
	private boolean moved = false;
	private boolean attacked = false;
	private String name;
	private int alliegance;
	private boolean available = true;
	private List<Item> inventory;
	private Optional<Item> equipped;
	//private String role;

	


	public Unit(String name, Stats stats, Stats growthRates, int x, int y, 
		File battleSprite, int scale, int alliegance, PaletteLoader palette){
		this.name = name;
		try{
			BufferedImage temp = ImageIO.read(battleSprite);
			this.battleSprite = MapBuilder.scaleImage(temp, scale);
		} catch (IOException e){
			//System.out.println(name);
		}
		this.x = x;
		this.y = y;
		this.stats = stats;
		this.growthRates = growthRates;
		paletteLoader = palette;
		defaultPalette = palette.getPlayer();
		setAlliegance(alliegance);
		inventory = new ArrayList<Item>();
		equipped = Optional.empty();
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public Location getLocation(){
		return new Location(x,y);
	}

	public void move(int x, int y){
		this.x = x;
		this.y = y;
		//swapColors((BufferedImage) battleSprite, getColorMap(paletteLoader.getGray()));
		moved = true;
	}

	public Image getImage(){
		return battleSprite;
	}

	public void setImage(Image img){
		battleSprite = img;	
	}

	public void setName(String s){
		name = s;
	}

	public String getName(){
		return name;
	}

	public String toString(){
		return name;
	}

	public int getStat(String stat){
		return stats.getStat(stat);
	}

	public void setStat(String stat, int value){
		stats.setStat(stat, value);
	}

	public void setAlliegance(int alliegance){
		this.alliegance = alliegance;
		if(alliegance != PLAYER){
			HashMap<Integer,Integer> map;
			if(alliegance == ENEMY){
				map = getColorMap(paletteLoader.getEnemy());
				defaultPalette = paletteLoader.getEnemy();
			} else if(alliegance == ALLY){
				map = getColorMap(paletteLoader.getAlly());
				defaultPalette = paletteLoader.getAlly();
			} else{
				map = new HashMap<Integer,Integer>();
				System.out.println("unrecognized alliegance");
			}
			swapColors((BufferedImage) battleSprite, map);
		} 
	}

	private HashMap<Integer,Integer> getColorMap(Color[] target){
		return getColorMap(defaultPalette, target);
	}

	private HashMap<Integer,Integer> getColorMap(Color[] from, Color[] to){
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for(int i = 0; i < to.length; i++){
			map.put(from[i].getRGB(),to[i].getRGB());
		}
		return map;
	}

	//adapted from kritzikratzi from stackoverflow: 
	//http://stackoverflow.com/questions/15213259/palette-swap-an-bufferedimage-in-java
	public static void swapColors(BufferedImage img, HashMap<Integer,Integer> map ){
		if(img == null){
			System.out.println("Whoops...");
		}
        int[] pixels = img.getRGB( 0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth() );
        
        for( int i = 0; i < pixels.length; i++ ){
            if( map.containsKey( pixels[i] ) ){
                pixels[i] = map.get( pixels[i] );
            } 
        }
        img.setRGB( 0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth() );  
    }

	public int getAlliegance(){
		return alliegance;
	}

	public boolean hasMoved(){
		return moved;
	}

	public void setAttacked(boolean b){
		attacked = b;
	}

	public boolean hasAttacked(){
		return attacked;
	}

	public boolean isAvailable(){
		return available;
	}

	public void setAvailability(boolean b){
		available = b;
		if(!b){
			swapColors((BufferedImage) battleSprite, getColorMap(paletteLoader.getGray()));
		} else{
			swapColors((BufferedImage) battleSprite, getColorMap(paletteLoader.getGray(), defaultPalette));
		}
	}

	public void giveItem(Item i){
		inventory.add(i);
	}

	public List<Item> getInventory(){
		return inventory;
	}

	public void equipItem(int index) throws IndexOutOfBoundsException{
		equipped = Optional.ofNullable(inventory.get(index));
	}

	public Optional<Item> getEquipped(){
		return equipped;
	}
}