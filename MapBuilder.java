/**
 * GUI to build maps out of tiles. Takes either one argument (to open a pre-existing file)
 * or two (to create a new file). In either case the first will be the filename, and the second
 * is the directory path to the tileset the map will use.
 *
 * TODO: 
 * -loading progress bar???
 * -ruler (even just numbers every 5 tiles or so)
 * -clean up code
 */

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class MapBuilder extends JFrame{

	public static final int MAX_TILESET_SIZE = 512;
	public static final int DISPLAY_SCALE = 2;
	private static final int DEFAULT_HEIGHT = 10;
	private static final int DEFAULT_WIDTH = 10;
	private static final int TILE_SIZE = TileGen.TILE_SIZE;
	private JLabel currTileDisplay;
	private ImageIcon currTileImg;
	private String currTileLoc;
	private JPanel map;
	private JScrollPane mapScroll;
	private JPanel displayRefresher;
	private JPanel displaySizeRegulator;
	private JPanel parentPanel;
	private JTextField widthField;
	private JTextField heightField;
	private Map backEnd;

	private int mapWidth;
	private int mapHeight;
	private String tileDir;
	private final File input;
	private File output;

	public MapBuilder(String name, File toLoad, File toSave, String tileDir){
		super(name);
		currTileImg = null;
		currTileLoc = "";
		try{
			System.out.println(tileDir);
			currTileImg = new ImageIcon(getTile(tileDir, 0,0,DISPLAY_SCALE));
			currTileLoc = "0_0";
		} catch (IOException e){
			System.out.println("Generating current tile failed.");
			System.out.println(e);
			System.exit(0);
		}
		currTileDisplay = new JLabel(new ImageIcon(scaleImage(
										currTileImg.getImage(), 2)));
		this.input = toLoad;
		output = toSave;
		this.tileDir = tileDir;
		if(toLoad!= null){
			try{
				backEnd = loadMap(toLoad);
			} catch(FileNotFoundException e){
				System.err.println("Could not find input file.");
				System.exit(0);
			}
		} else{
			backEnd = emptyMap(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		}
		mapWidth = backEnd.getWidth();
		mapHeight = backEnd.getHeight();
	}

	public static void main(String[] args){
		//read arguments and respond correctly
		File in;
		File out;
		String tileDirectory;
		if(args.length==0 || args.length>2){
			in = null;
			out = null;
			tileDirectory = "";
			System.err.println("Incorrect number of arguments. Required: Filename (tileset path)");
			System.exit(0);
		} else if(args.length==1){ //load old file
			in = new File(args[0]);
			out = in;
			Scanner s = null;
			try{
				s = new Scanner(in);
			} catch (FileNotFoundException e){
				System.out.println("Could not find input file.");
				System.exit(0);
			}
			tileDirectory = s.nextLine(); 
		} else{
			in = null;
			out = new File(args[0]);
			tileDirectory = args[1];
			try{
				File f = new File(tileDirectory);
				if(!f.isDirectory()){
					throw new IOException("Tileset does not exist");
				}
			} catch(IOException e){
				System.err.println(e);
				System.out.println("This error is likely thanks to an invalid tileset path");
				System.exit(0);
			}
		}

		MapBuilder test = new MapBuilder("Map Editor", in, out, tileDirectory);

		//Build GUI
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				test.CreateAndDisplayGUI();
			}
		});
	}

	private void CreateAndDisplayGUI(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		//buildOptionsMenu() needs to come first because it initializes 
		//currTileImg which buildDisplay uses.	
		JPanel buttonArea = buildOptionsMenu();

		JPanel topHalf = new JPanel();
		topHalf.setLayout(new BoxLayout(topHalf, BoxLayout.X_AXIS));

		JPanel palette = loadTileset();
		
		JComponent display = buildDisplay();
		
		topHalf.add(display);
		topHalf.add(palette);
		topHalf.add(new JPanel());
		content.add(topHalf);
		content.add(buttonArea);

		this.add(content);
		parentPanel = content;
		this.pack();
		this.setVisible(true);
	}

	/**
	 * Constructs a panel containing an options menu, then returns said panel.
	 */
	public JPanel buildOptionsMenu(){
		JPanel options = new JPanel();

		//Create the height/width section
		JPanel dimensions = new JPanel();
		dimensions.setLayout(new BoxLayout(dimensions, BoxLayout.Y_AXIS));

		JLabel widthLabel = new JLabel("X:");
		JTextField widthBox = new JTextField(10);
		widthField = widthBox;
		JPanel width = new JPanel();
		width.add(widthLabel);
		width.add(widthBox);

		JLabel heightLabel = new JLabel("Y:");
		JTextField heightBox = new JTextField(10);
		heightField = heightBox;
		JPanel height = new JPanel();
		height.add(heightLabel);
		height.add(heightBox);
	
		dimensions.add(width);
		dimensions.add(height);

		//"Apply changes" button
		JButton apply = new JButton("Apply Changes");
		apply.addActionListener(new ApplyHWChanges());

		//Fill option
		JButton fill = new JButton("Fill with current tile");
		fill.addActionListener(new FillButton());
		options.add(fill);

		//Current selected tile
		JPanel tileArea = new JPanel();
		tileArea.setLayout(new BoxLayout(tileArea, BoxLayout.Y_AXIS));
		tileArea.add(new JLabel("Current Tile"));
		
		tileArea.add(currTileDisplay);

		//remove once testing is done
		backEnd.setTestImage(currTileImg);

		//Save button
		JButton save = new JButton("Save");
		save.addActionListener(new SaveButton());

		options.add(dimensions);
		options.add(apply);
		options.add(tileArea);
		options.add(save);
		return options;
	}

	/**
	 * ActionListener that reads widthField and heightField, then changes the width
	 * of the map accordingly. I should add a warning message if the user will crop
	 * off parts of their map.
	 */
	class ApplyHWChanges implements ActionListener{
		public void actionPerformed(ActionEvent e){
			int tempW = mapWidth;
			int tempH = mapHeight;
			int targetW;
			int targetH;
			try{
				targetW = Integer.parseInt(widthField.getText());
				targetH = Integer.parseInt(heightField.getText());
			
				if(targetH <= 0 || targetW <=0){
					JOptionPane.showMessageDialog(null, "Both x and y must be above 0.");
				} else {

					//shrink width if necessary
					while(targetW < mapWidth){				
						for(int i = mapHeight*mapWidth-1; i >= 0; i-= mapWidth){
							map.remove(i);
						}
						mapWidth--;
						mapScroll.revalidate();
						map.repaint();

						backEnd.removeColumn(backEnd.getWidth()-1);
					}

					//shrink height if necessary
					while(targetH < mapHeight){
						for(int i = 1; i <= mapWidth; i++){
							map.remove(mapHeight*mapWidth - i);
						}
						mapHeight--;
						//map.setLayout(new GridLayout(mapHeight,mapWidth));
						map.repaint();
						backEnd.removeRow(backEnd.getHeight()-1);
					}

					//Grow if necessary
					if(targetW > mapWidth || targetH > mapHeight){
						//add new rows and columns, then rebuild and re-add display
						int[] mov = {1,1,1,1,1,1,1,1,1,1,1};
						while(targetW > mapWidth){
							//add new column to backEnd
							List<Tile> tList = new ArrayList<Tile>();
							for(int i = 0; i < mapHeight; i++){
								Tile t = new Tile(currTileImg, "Test", 0, 0, mov,"none",true, currTileLoc);
								t.setPreferredSize(new Dimension(TILE_SIZE*DISPLAY_SCALE, TILE_SIZE*DISPLAY_SCALE));
								t.addMouseListener(new MapButtonListener());
								tList.add(t);
							}
							backEnd.addColumn(tList);
							mapWidth++;
						}
						while(targetH > mapHeight){
							//add new row to backEnd
							List<Tile> tList = new ArrayList<Tile>();
							for(int i = 0; i < mapWidth; i++){
								Tile t = new Tile(currTileImg, "Test", 0, 0, mov,"none",true, currTileLoc);
								t.setPreferredSize(new Dimension(TILE_SIZE*DISPLAY_SCALE, TILE_SIZE*DISPLAY_SCALE));
								t.addMouseListener(new MapButtonListener());
								tList.add(t);
							}
							backEnd.addRow(tList);
							mapHeight++;
						}

						GridBagConstraints gbc = new GridBagConstraints();
		
						for(int i = 0; i < mapHeight; i++){
							gbc.gridy = i;
							for(int j = 0; j < mapWidth; j++){
								gbc.gridx = j;
								map.add(backEnd.getTile(j, i), gbc);
							}
						}
						map.revalidate();
						map.repaint();
						parentPanel.revalidate();
						parentPanel.repaint();
						((MapBuilder) SwingUtilities.getWindowAncestor(parentPanel)).pack();
					}

				}
			} catch(NumberFormatException f){
				JOptionPane.showMessageDialog(null, "Both x and y must be valid integers.");
			} 
		}
	}

	/**
	 * ActionListener that fills the entire map with the tile currently selected. 
	 */
	class FillButton implements ActionListener{
		public void actionPerformed(ActionEvent e){
			for(int x = 0; x < mapWidth; x++){
				for(int y = 0; y < mapHeight; y++){
					Tile t = backEnd.getTile(x,y);
					t.setIcon(new ImageIcon(scaleImage(currTileImg.getImage(), DISPLAY_SCALE)));
					//System.out.println("fillButton actionListener");
					t.setSource(currTileLoc);
				}
			}
		}
	}

	//For now just outputs map's toString to console.
	//Later will actually save the file.
	class SaveButton implements ActionListener{
		public void actionPerformed(ActionEvent e){
			System.out.println(backEnd);
			
			try{
				FileWriter f = new FileWriter(output);
				f.write(backEnd.toString());
				f.close();
			} catch(IOException a){
				JOptionPane.showMessageDialog(null, "Error loading file to save to.");
			}
		}
	}

	/**
	 * Loads the tileset defined in the constructor into a JPanel which it then
	 * returns. Makes no assumptions about height or width, which means it needs
	 * to read an extra 64 tiles at worst (32 down to check height and 32
	 * across for width), since the maximum height/width is 32 tiles.
	 */
	public JPanel loadTileset(){
		int height = MAX_TILESET_SIZE;
		int width = MAX_TILESET_SIZE;

		boolean maxHeight = false;
		boolean maxWidth = false;

		//find width
		int j = 0;
		while(!maxWidth){
			try{
				File f = new File(tileDir+"/"+j+"_"+0+".png");
				ImageIO.read(f);
			} catch (IOException e){
				width = j;
				maxWidth = true;
			} j += TILE_SIZE;
		}

		//find height
		int i = 0;
		while(!maxHeight){
			try{
				File f = new File(tileDir+"/"+0+"_"+i+".png");
				ImageIO.read(f);
			} catch (IOException e){
				height = i;
				maxHeight = true;
			} i += TILE_SIZE;
		}

		JPanel tileDisplay = new JPanel();
		tileDisplay.setLayout(new GridLayout(height/TILE_SIZE, width/TILE_SIZE));
		tileDisplay.setMinimumSize(new Dimension(width, height));
		tileDisplay.setPreferredSize(new Dimension(width, height));
		tileDisplay.setMaximumSize(new Dimension(width, height));

		for(i = 0; i < height; i+= TILE_SIZE){
			for(j = 0; j < width; j+= TILE_SIZE){
				String fPath = tileDir+"/"+j+"_"+i;
				try{
					int[] mov = {1,1,1,1,1,1,1,1,1,1,1};

					Image icon = getTile(tileDir, j, i, 1);
					Tile tile = new Tile(new ImageIcon(icon), "palette", 0,0,mov,
						"none", false, ""+j+"_"+i);
					tile.addMouseListener(new PaletteButtonListener());
					tile.setMaximumSize(new Dimension(TILE_SIZE,TILE_SIZE));
					tile.setPreferredSize(new Dimension(TILE_SIZE,TILE_SIZE));
					tile.setMinimumSize(new Dimension(TILE_SIZE,TILE_SIZE));
					tileDisplay.add(tile);
				} catch(IOException e){
				}
			}
		}
		return tileDisplay;
	}

	/**
	 * ActionListener to be assigned to JButtons in the palette. Assigns the icon
	 * of the JButton to currTileImg.
	 */
	class PaletteButtonListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			Tile t = (Tile) e.getSource();
			ImageIcon temp = (ImageIcon) t.getIcon();
			currTileImg = new ImageIcon(scaleImage(temp.getImage(), DISPLAY_SCALE));
			currTileDisplay.setIcon(new ImageIcon(scaleImage(temp.getImage(), DISPLAY_SCALE*2)));
			currTileLoc = t.getSource();
		}
	}

	/**
	 * Returns a map with the specified dimensions filled with the currTile image.
	 * The image should be the upper-left tile of a given tileset if this is called from
	 * the constructor.
	 */
	public Map emptyMap(int width, int height){
		Map toReturn = new Map(width, height, tileDir);
		int[] mov = {1,1,1,1,1,1,1,1,1,1,1};
		for(int i = 0; i < height; i++){
			List<Tile> tList = new ArrayList<Tile>();
			for(int j = 0; j < width; j++){
				Tile t = new Tile(currTileImg, "Test", 0, 0, mov,"none",true, currTileLoc);
				tList.add(t);
				//t.setMargin(new Insets(0,0,0,0));
				t.setMaximumSize(new Dimension(TILE_SIZE*DISPLAY_SCALE,TILE_SIZE*DISPLAY_SCALE));
				t.setPreferredSize(new Dimension(TILE_SIZE*DISPLAY_SCALE, TILE_SIZE*DISPLAY_SCALE));
				//tile.setPreferredSize(new Dimension(TILE_SIZE*DISPLAY_SCALE, TILE_SIZE*DISPLAY_SCALE));
				t.addMouseListener(new MapButtonListener());
			}
			toReturn.addRow(tList);
		}
		return toReturn;
	}

	public Map loadMap(File input) throws FileNotFoundException {
		Scanner s = new Scanner(input);
		tileDir = s.nextLine();
		int width = s.nextInt();
		int height = s.nextInt();
		Map toReturn = new Map(width, height, tileDir);
		s.nextLine(); //eat up rest of line.
		for(int y = 0; y < height; y++){
			String line = s.nextLine();
			Scanner lineReader = new Scanner(line);
			List<Tile> tList = new ArrayList<Tile>();
			for(int x = 0; x < width; x++){
				String[] values = lineReader.next().split("/");
				String name = values[0];
				int[] picLocation = new int[2];
				for(int i = 0; i < picLocation.length; i++){
					picLocation[i] = Integer.parseInt(values[1].split("_")[i]);
				}
				ImageIcon img = null;
				try{
					img = new ImageIcon(getTile(tileDir, picLocation[0], 
									picLocation[1], DISPLAY_SCALE));
				} catch(IOException e){
					System.out.println("Could not find image.");
					img = new ImageIcon(new BufferedImage(TILE_SIZE*DISPLAY_SCALE, 
						TILE_SIZE*DISPLAY_SCALE, BufferedImage.TYPE_INT_RGB));
				}
				int avoid = Integer.parseInt(values[2]);
				int def = Integer.parseInt(values[3]);
				String[] movString = values[4].split(",");
				int[] moveCost = new int[movString.length];
				for(int i = 0; i < moveCost.length; i++){
					moveCost[i] = Integer.parseInt(movString[i]);
				}
				String special = values[5];

				Tile t = new Tile(img, name, avoid, def, moveCost, special, true, 
								"" + picLocation[0]+"_"+picLocation[1]);
				tList.add(t);
				t.setMaximumSize(new Dimension(TILE_SIZE*DISPLAY_SCALE,TILE_SIZE*DISPLAY_SCALE));
				t.setPreferredSize(new Dimension(TILE_SIZE*DISPLAY_SCALE, TILE_SIZE*DISPLAY_SCALE));
				t.addMouseListener(new MapButtonListener());
			} 
			toReturn.addRow(tList);
		}
		return toReturn;
	}

	/**
	 * Builds a blank display. Eventually I'll have to add loading from a map,
	 * though right now it builds the map itself. I should separate that out.
	 */
	public JComponent buildDisplay(){
		JPanel holder = new JPanel(new GridBagLayout());
		map = holder;

		//holder.setLayout(new GridLayout(mapHeight,mapWidth));
		// holder.setPreferredSize(new Dimension(mapWidth * TILE_SIZE * DISPLAY_SCALE, 
		// 									mapHeight * TILE_SIZE * DISPLAY_SCALE));
		// holder.setMaximumSize(new Dimension(mapWidth * TILE_SIZE * DISPLAY_SCALE, 
		//  									mapHeight * TILE_SIZE * DISPLAY_SCALE));
		//backEnd = emptyMap(mapWidth, mapHeight);		

		GridBagConstraints gbc = new GridBagConstraints();

		// for(int i = 0; i < mapHeight; i++){
		// 	List<Tile> tList = new ArrayList<Tile>();
		// 	gbc.gridy = i;
		// 	for(int j = 0; j < mapWidth; j++){
		// 		gbc.gridx = j;
		// 		Tile t = new Tile(currTileImg, "Test", 0, 0, mov,"none",true, "0_0");
		// 		tList.add(t);
		// 		//t.setMargin(new Insets(0,0,0,0));
		// 		t.setMaximumSize(new Dimension(TILE_SIZE*DISPLAY_SCALE,TILE_SIZE*DISPLAY_SCALE));
		// 		t.setPreferredSize(new Dimension(TILE_SIZE*DISPLAY_SCALE, TILE_SIZE*DISPLAY_SCALE));
		// 		//tile.setPreferredSize(new Dimension(TILE_SIZE*DISPLAY_SCALE, TILE_SIZE*DISPLAY_SCALE));
		// 		t.addMouseListener(new MapButtonListener());
		// 		holder.add(t,gbc);

		// 	}
		// 	backEnd.addRow(tList);
		// }

		for(int i = 0; i < mapHeight; i++){
			gbc.gridy = i;
			for(int j = 0; j < mapWidth; j++){
				gbc.gridx = j;
				holder.add(backEnd.getTile(j,i),gbc);
			}
		}
		// gbc.gridy = 0;
		// gbc.gridx = mapWidth;
		// gbc.gridheight = GridBagConstraints.REMAINDER;
		// gbc.weightx = 1;
		// gbc.weighty = 1;
		// gbc.fill = GridBagConstraints.BOTH;
		// holder.add(new JPanel(), gbc);
		// gbc.gridx = 0;
		// gbc.gridy = mapHeight;
		// gbc.gridheight = 1;
		// gbc.gridwidth = GridBagConstraints.REMAINDER;
		// holder.add(new JPanel(), gbc);
		//System.out.println(backEnd);

		//Container panel to prevent stretching 
		JPanel outer = new JPanel();
		outer.add(holder);

		//BoxLayout ensures that maxSize is honored
		// JPanel displaySizeRegulator = new JPanel();
		// displaySizeRegulator.setLayout(new BoxLayout(displaySizeRegulator, BoxLayout.X_AXIS));
		
		JScrollPane displayScroll = new JScrollPane(outer, 
		 		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		mapScroll = displayScroll;

		// displaySizeRegulator.add(displayScroll);
		// displayRefresher = displaySizeRegulator;
		return displayScroll;
	}
	
	/**
	 * Should only be assigned to JButton objects. Sets the icon of the clicked 
	 * button to be the current image.
	 */
	class MapButtonListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			Tile t = (Tile) e.getSource();
			t.setIcon(new ImageIcon(scaleImage(currTileImg.getImage(), 1)));
			t.setSource(currTileLoc);
		}
	}	

	/**
	 * Uses a ImageFilter to return a cropped version of the image. Hopefully
	 * useful when handling tilesets.
	 */
	public static Image getTile(Image tileset, int x1, int x2, int y1, int y2, int scale){
		JPanel producer = new JPanel(); 
		//Crop tileset to grab tile
		ImageFilter cropper = new CropImageFilter(x1, y1, x2-x1, y2-y1);
		Image cropped = producer.createImage(new FilteredImageSource(tileset.getSource(), cropper));

		return scaleImage(cropped, scale);
	}

	/**
	 * Takes a filepath to a tileset folder, x, y coordinates and a scale and returns
	 * the desired tile.
	 */
	public static Image getTile(String filepath, int x, int y, int scale) throws IOException{
		//System.out.println(filepath+"/"+x+"_"+y);
		try{
			return scaleImage(ImageIO.read(new File(filepath+"/"+x+"_"+y+".png")), scale);
		} catch(IOException e){
			return scaleImage(ImageIO.read(new File(filepath+"/"+x+"_"+y)), scale);
		}	
	}

	/**
	 * Scales an image. To be used for tiles. Probably not the most efficient 
	 * scaling method.
	 */
	public static Image scaleImage(Image orig, int scale){
		Image result;
		if(scale != 1){	
			int width = orig.getWidth(null);
			int height = orig.getHeight(null);
			//Scale cropped image to proper size
			result = new BufferedImage(width*scale, height*scale, BufferedImage.TYPE_INT_ARGB); 
			Graphics g = ((BufferedImage) result).createGraphics();
			g.drawImage(orig, 0, 0, width*scale, height*scale, 0,0,width-1,height-1,null);
			g.dispose();
		} else{
			return orig;
		}
		return result;	
	}
}