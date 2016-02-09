import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;

public class MapTest extends JFrame{

	public static final int TILE_SIZE = 64;
	
	public MapTest(String name){
		super(name);
		setResizable(false);
	}

	public static void main(String[] args){
		
		//build a test map
		int[][] map = new int[5][10];
		for(int i = 0; i<map.length; i++){
			for(int j = i%2; j < map[i].length; j+=2){
				map[i][j] = 1;
			}
		}

		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				createAndDisplayGUI(map);
			}
		});
	}

	/**
	 * Builds and displays the GUI based on the 2d array representing the map.
	 */
	private static void createAndDisplayGUI(int[][] mapArray){
		MapTest f = new MapTest("Map");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		BufferedImage tiles = null;
			try {
			    tiles = ImageIO.read(new File("Tiles/FE7/OverworldRegular.png"));
			} catch (IOException e) {
				System.out.println("Error loading image");
			}
		f.addButtons(f.getContentPane(), mapArray, tiles);
		f.pack();
		f.setVisible(true);
	}

	/**
	 * Creates a button for each tile and adds it to the game. All buttons are contained in
	 * a JPanel called mapDisplay. Eventually we'll dynamically load tiles depending on what
	 * the tile should be (instead of directly coding 0,15,0,15, etc).
	 */
	public void addButtons(Container pane, int[][] map, Image tileSet){
		JPanel mapDisplay = new JPanel();
		int width = map[0].length;
		int height = map.length;
		//System.out.println("WIDTH:"+ width+" HEIGHT:"+height);
		mapDisplay.setLayout(new GridLayout(height, width));
		mapDisplay.setPreferredSize(new Dimension(width*TILE_SIZE, height*TILE_SIZE));
		for(int i=0;i<height;i++){
			for(int j=0; j<width; j++){
				//ImageIcon tree = new ImageIcon("tree.png");
				JButton tile;
				if(map[i][j] == 1){
					tile = new JButton(new ImageIcon(getTile(tileSet,0,15,0,15)));
				} else{
					tile = new JButton(new ImageIcon(getTile(tileSet,0,15,32,47)));
				}
				tile.setMargin(new Insets(0,0,0,0));
				//tile.setBorderPainted(false);
				mapDisplay.add(tile);
				//System.out.println("accessed " + i + " " + j);
			}
		}
		pane.add(mapDisplay);
	}

	/**
	 * Uses a ImageFilter to return a cropped version of the image. Hopefully
	 * useful when handling tilesets.
	 */
	public Image getTile(Image tileset, int x1, int x2, int y1, int y2){

		//Crop tileset to grab tile
		ImageFilter cropper = new CropImageFilter(x1, y1, x2-x1, y2-y1);
		Image cropped = createImage(new FilteredImageSource(tileset.getSource(), cropper));

		//Scale cropped image to proper size
		BufferedImage result = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_RGB); 
		Graphics g = result.createGraphics();
		g.drawImage(cropped, 0, 0, TILE_SIZE, TILE_SIZE, null);
		g.dispose();
		return result;
	}
}