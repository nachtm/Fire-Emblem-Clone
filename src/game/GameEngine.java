package game;

import common.Location;
import common.Tile;
import common.Map;
import common.Utils;
import mapbuilder.MapBuilder;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class GameEngine{

	private static final int DISPLAY_SCALE = 2;
	private static final int TILE_SIZE = Tile.TILE_SIZE;
	private final File mapFile;
	private common.Map map;
	private GamePanel canvas;
	private StatSheet charSheet;
	private AttackResults atkResults;
	private GameMenu menu;
	private UnitList units;
	private Unit selected = null;
	private PaletteLoader palettes;

	public GameEngine(File mapFile){
		this.mapFile = mapFile;
		palettes = new PaletteLoader();
		units = new UnitList();
		try{
			this.map = loadMap(mapFile);
		} catch (FileNotFoundException e){
			System.out.println("Map file not found.");
			this.map = null;
			System.exit(0);
		}
	}

	public static void main(String[] args){
		
		File mapFile = new File("a.txt");
		GameEngine engine = new GameEngine(mapFile);

		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				engine.BuildAndDisplayGUI();
			}
		});
	}

	public void BuildAndDisplayGUI(){
		JFrame f = new JFrame("Fire Emblem Java Clone");
		JPanel content = new JPanel();
		canvas = new GamePanel(map.getWidth(), map.getHeight());
		//we'll make special classes for each of these panels eventually
		menu = new GameMenu(canvas);
		//menu.add(new JLabel("menu"));
		// menu.setBackground(Color.BLACK);
		charSheet = new StatSheet();
		// charSheet.setBackground(Color.BLACK);
		atkResults = new AttackResults();
		//atkResults.add(new JLabel("attack Results"));
		// atkResults.setBackground(Color.BLACK);
		// content.add(canvas);
		// content.add(menu);
		// content.add(charSheet);
		// content.add(atkResults);

		GroupLayout layout = new GroupLayout(content);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
					.addComponent(canvas)
					.addComponent(menu))
				.addGroup(layout.createParallelGroup()
					.addComponent(charSheet)
					.addComponent(atkResults))
		);

		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
					.addComponent(canvas)
					.addComponent(charSheet))
				.addGroup(layout.createParallelGroup()
					.addComponent(menu)
					.addComponent(atkResults))
		);
		content.setLayout(layout);

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//f.add(canvas);
		f.add(content);
		f.pack();
		f.setVisible(true);
		f.setFocusable(false);
		canvas.setFocusable(true);
		canvas.drawMap();
		canvas.requestFocusInWindow();
	}

	public Map loadMap(File input) throws FileNotFoundException {
		Scanner s = new Scanner(input);
		
		//read map information section
		String tileDir = s.nextLine();
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
					img = new ImageIcon(MapBuilder.getTile(tileDir, picLocation[0],
									picLocation[1], DISPLAY_SCALE));
				} catch(IOException e){
					System.out.println("Could not find image. " + tileDir +picLocation[0]+picLocation[1]);
					img = new ImageIcon(new BufferedImage(16*DISPLAY_SCALE, 
						16*DISPLAY_SCALE, BufferedImage.TYPE_INT_RGB));
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
				t.setMaximumSize(new Dimension(16*DISPLAY_SCALE,16*DISPLAY_SCALE));
				t.setPreferredSize(new Dimension(16*DISPLAY_SCALE, 16*DISPLAY_SCALE));
			} 
			toReturn.addRow(tList);
		}

		//read unit information section
		loadUnits(s);
		return toReturn;
	}

	public void loadUnits(Scanner s){
		File f = new File("Sprites/Units/Thief.png");
		File e = new File("Sprites/Units/Pirate.png");
		Stats testStats = new Stats(1,0,20,3,1,1,0,0,0,6,7);
		Stats growthRates = new Stats(0,0,80,50,30,20,25,12,15,0,0);
		units.addUnit(new Unit("Joe",testStats,growthRates,5,5,f,
			DISPLAY_SCALE, Unit.PLAYER, palettes));
		units.addUnit(new Unit("Jon",testStats,growthRates,9,9,f,
			DISPLAY_SCALE, Unit.PLAYER, palettes));
		units.addUnit(new Unit("Jack",testStats,growthRates,4,4,f,
			DISPLAY_SCALE, Unit.ALLY, palettes));
		units.addUnit(new Unit("Ed",testStats,growthRates,8,8,e,
			DISPLAY_SCALE, Unit.ENEMY, palettes));
		for(Unit u: units.getUnitsByAllieg(Unit.PLAYER)){
			u.giveItem(new Item("Apple Pie", "E", 1, 5,5,90,0,46));
			u.equipItem(0);
		}
	}

	public Set<Location> getMoveCandidates(Unit u){
		//System.out.println("called");
		Set<Location> toReturn = new HashSet<Location>();
		Queue<Step> targets = new ArrayDeque<Step>();
		Location start = u.getLocation();
		for(Location l: map.getNeighbors(start)){
			if(units.getUnit(l)==null && u.getStat("move")-1 !=0){
				targets.add(new Step(l, u.getStat("move")-1));	
			}
		} 
		while(!targets.isEmpty()){
			Step next = targets.remove();
			//if target square is empty:
			if(!units.squareOccupied(next.getLocation())){ //change this to look at all kinds of terrain hopefully
				if(!toReturn.contains(next.getLocation())){
					toReturn.add(next.getLocation());
				}
				if(next.getSteps()!=0){
					//may check locations twice
					for(Location l:map.getNeighbors(next.getLocation())){
						if(!toReturn.contains(l)){
							targets.add(new Step(l, next.getSteps()-1));
						}
					}
				} //if ally inhabits target square, add neighbor squares to targets but not
				  //the square itself
			} else if(units.getUnit(next.getLocation()).getAlliegance() == u.getAlliegance()){
				if(next.getSteps()!=0){
					//may check locations twice
					for(Location l:map.getNeighbors(next.getLocation())){
						if(!toReturn.contains(l)){
							targets.add(new Step(l, next.getSteps()-1));
						}
					}
				}
			} 
		}
		return toReturn;
	}

	//modify this to check weapon's range
	public Set<Location> getAttackCandidates(Unit u){
		Set<Location> toReturn = map.getNeighbors(u.getLocation());
		Set<Location> toRemove = new HashSet<Location>();
		for(Location l : toReturn){
			if(!units.squareOccupied(l)){
				toRemove.add(l);
			}
		} for(Location l : toRemove){
			toReturn.remove(l);
		}
		//System.out.println("Length of atkCandidates: " + toReturn.size());
		return toReturn;
	}

	private void deselect(){
		selected = null;
		canvas.drawMap();
		canvas.requestFocusInWindow();
		menu.clearMenu();
		canvas.setAction(GamePanel.NONE);
		charSheet.resetFields();	
	}

	class Step{
		private Location l;
		private int stepsLeft;
		public Step(Location l, int steps){
			this.l = l;
			this.stepsLeft=steps;
		}
		public Location getLocation(){
			return l;
		}
		public int getSteps(){
			return stepsLeft;
		}
		public boolean equals(Object o){
			if(o instanceof Step){
				return l.equals(((Step)o).getLocation());
			} return false;
		}
	}

	class GamePanel extends JPanel{
		public static final int NONE = 0;
		public static final int MOVE = 1;
		public static final int ATTACK = 2;
		private final int WIDTH;
		private final int HEIGHT;
		private BufferedImage img;
		private Cursor c;
		private Image moveSquare = null;
		private Image atkSquare;
		private Set<Location> moveTargets = null;
		private Set<Location> atkTargets;
		private boolean showMoveCandidates = false;
		private boolean showAtkCandidates = false;
		private int currAction = 0;

		public GamePanel(int width, int height){
			setBackground(Color.WHITE);

			WIDTH = width*TILE_SIZE*DISPLAY_SCALE;
			HEIGHT = height*TILE_SIZE*DISPLAY_SCALE;
			img = new BufferedImage(
				WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

			c = new Cursor(DISPLAY_SCALE);

			try{
				moveSquare = ImageIO.read(new File("Sprites/BlueSquare.png"));
				moveSquare = Utils.scaleImage(moveSquare,GameEngine.DISPLAY_SCALE);
				atkSquare = ImageIO.read(new File("Sprites/RedSquare.png"));
				atkSquare = Utils.scaleImage(atkSquare,GameEngine.DISPLAY_SCALE);
			} catch(IOException e){
				System.err.println(e);
				System.exit(1);
			}

			String[] actions = {"up","down","left","right", "space"};

			for(int i = 0; i < actions.length; i++){
				getInputMap().put(KeyStroke.getKeyStroke(
								actions[i].toUpperCase()), actions[i]);
				getActionMap().put(actions[i], new MoveCursor(actions[i]));
			}
		}

		class MoveCursor extends AbstractAction{
			String direction;

			public MoveCursor(String d){
				direction = d;
			}

			public void actionPerformed(ActionEvent e){
				switch(direction){
					case "up":		if(c.getY() != 0){
										c.setY(c.getY()-1);
									}
									break;
					case "down":	if(c.getY() != map.getHeight()-1){
										c.setY(c.getY()+1);
									}
									break;
					case "left":	if(c.getX() != 0){
										c.setX(c.getX()-1);
									}	
									break;	
					case "right":	if(c.getX() != map.getWidth()-1){
										c.setX(c.getX()+1);
									}
									break;
					case "space":	switch(currAction){
										case NONE:		if(selected == null){ 
															selected = units.getUnit(new Location(
																c.getX(), c.getY()));
															if(selected != null){ //cursor is on a character that hasn't moved yet.
																moveTargets = getMoveCandidates(selected);
																charSheet.changeUnit(selected);

																//probably modify this so we only take actions
																//on our own characters...
																menu.requestFocusInWindow();
																menu.presentOptions(selected);
															}
														}
														break;	
										case MOVE:		Location target = new Location(c.getX(), c.getY());
														if(moveTargets.contains(target)){
															units.moveUnit(selected, target);
															//GameEngine.this.deselect();
															setAction(NONE);
															moveTargets = null;
															setMoveVisibility(false);	
															menu.requestFocusInWindow();
															menu.presentOptions(selected);
														} else{
															GameEngine.this.deselect();
														}
														break;
										case ATTACK:	target = new Location(c.getX(), c.getY());

														//System.out.println("Attacking...");
														if(getAttackCandidates(selected).contains(target)){
															System.out.println("Success!");
															atkResults.showResults(selected, units.getUnit(target));

															setAction(NONE);
															menu.requestFocusInWindow();
															menu.presentOptions(selected);
														}
									}
									break;	
				}
				drawMap();
			}
		}

		public Dimension getPreferredSize(){
			if(isPreferredSizeSet()){
				return super.getPreferredSize();
			} return new Dimension(WIDTH, HEIGHT);
		}

		protected void paintComponent(Graphics g){
			super.paintComponent(g);

			g.drawImage(img,0,0,null);
		}

		public void setMoveVisibility(boolean b){
			showMoveCandidates = b;
		}

		public void setAtkVisibility(boolean b){
			showAtkCandidates = b;
		}

		public void drawMap(){
			//JPanel canvas = new JPanel();
			Graphics2D g = (Graphics2D) img.getGraphics();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			for(int y = 0; y < map.getHeight(); y++){
				for(int x = 0; x < map.getWidth(); x++){
					g.drawImage(map.getTileObject(x,y).getImage(), x*TILE_SIZE*DISPLAY_SCALE,
						y*TILE_SIZE*DISPLAY_SCALE, null);
				}
			}

			g.drawImage(c.getImage(), c.getX()*TILE_SIZE*DISPLAY_SCALE, c.getY()*TILE_SIZE*DISPLAY_SCALE, null);
			
			//draw the units
			for(List<Unit> uList: units.getAllUnits()){
				for(Unit unit: uList){
					g.drawImage(unit.getImage(), unit.getX()*TILE_SIZE*DISPLAY_SCALE, unit.getY()*TILE_SIZE*DISPLAY_SCALE, null);
				}
			}

			//if optimization is an issue, look here! finding movecands every repaint = bad
			//draw move candidates
			if(selected != null){
				float opacity = 0.5f;
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
															opacity));
				if(showMoveCandidates){
					//System.out.println("showing moveCands");
					for(Location l:getMoveCandidates(selected)){
						g.drawImage(moveSquare, l.getX()*TILE_SIZE*DISPLAY_SCALE, l.getY()*TILE_SIZE*DISPLAY_SCALE, null);
						//System.out.println(l);
					}
				} if(showAtkCandidates){
					//System.out.println("Showing atkCands");
					for(Location l:getAttackCandidates(selected)){
						g.drawImage(atkSquare, l.getX()*TILE_SIZE*DISPLAY_SCALE, l.getY()*TILE_SIZE*DISPLAY_SCALE, null);
					}
				}
			}

			canvas.repaint();
		}	

		public void setAction(int i){
			currAction = i;
		}
	}

	class GameMenu extends JPanel{
		//private Unit selectedUnit;
		private int selectedAction;
		private JLabel noCharSelected;
		private List<GameAction> validActions;
		private List<GameAction> allActions;
		private GamePanel panel;

		public GameMenu(GamePanel panel){
			noCharSelected = new JLabel("Select a character.");
			add(noCharSelected);
			this.panel = panel;

			validActions = new ArrayList<>();
			allActions = new ArrayList<>();
			allActions.add(new Move(panel));
			allActions.add(new Attack(panel));
			allActions.add(new EndTurn(panel));
			allActions.add(new Back(panel));

			String[] actions = {"left","right","space"};

			for(String action : actions){
				getInputMap().put(KeyStroke.getKeyStroke(action.toUpperCase()),
								action);
				getActionMap().put(action, new SelectOption(action));
			}
		}

		public void presentOptions(Unit curr){
			validActions.clear();
			removeAll();
			//selectedUnit = curr;
			for(GameAction a : allActions){
				if(a.legalAction()){
					validActions.add(a); //is this line necessary? Do we need a validActions list?
					add(a);
				}
			}
			System.out.println(validActions.size());
			System.out.println(selectedAction);

			if(validActions.size() > 0){
				validActions.get(0).highlight();
				selectedAction = 0;
			} else{
				selectedAction = -1;
			}
			repaint();
		}

		public void clearMenu(){
			for(GameAction g:validActions){
				g.unHighlight();
			}
			validActions.clear();
			removeAll();
			selected = null;
			add(noCharSelected);
			repaint();
		}

		private void switchSelection(int index){
			
			if(index < validActions.size() && index >= 0){
				validActions.get(selectedAction).unHighlight();
				validActions.get(index).highlight();
				selectedAction = index;
				repaint();
			}
		}

		class SelectOption extends AbstractAction{
			String action;
			
			public SelectOption(String s){
				action = s;
			}

			public void actionPerformed(ActionEvent e){
				switch(action){
					case "left":	switchSelection(selectedAction-1);
									break;
					case "right":	switchSelection(selectedAction+1);
									break;
					case "space":	validActions.get(selectedAction).desiredAction();
									break;
				}
			}
		}	
	
		//a JLabel plus a way to determine if a. the action is valid and b. what the
		//result of selecting it should be
		abstract class GameAction extends JLabel{
			private String name;
			private GamePanel panel;

			GameAction(String name, GamePanel panel){
				super(name);
				setBackground(Color.RED);
				this.panel = panel;
			}

			void highlight(){
				setOpaque(true);
			}

			void unHighlight(){
				setOpaque(false);
			}

			abstract boolean legalAction(); //it is expected that legalAction returns true iff selected.available() is true EXCEPT for Back,
											//which is used to exit selection of unavailable characters.

			abstract void desiredAction();
		}

		class Attack extends GameAction{
			Attack(GamePanel p){
				super("Attack", p);
			}

			boolean legalAction(){
				return selected.isAvailable() && !selected.hasAttacked();
			}

			//modify this as well.
			void desiredAction(){
				System.out.println("Attack!");
				panel.setAtkVisibility(true);
				panel.setAction(GamePanel.ATTACK);
				panel.requestFocusInWindow();
				panel.drawMap();
			}
		}

		class Move extends GameAction{
			private Set<Location> moveTargets;

			Move(GamePanel p){
				super("Move", p);	
				moveTargets = null;
			}

			boolean legalAction(){
				System.out.println(selected == null);
				return selected.isAvailable() && !selected.hasMoved();
			}

			//modify
			void desiredAction(){
				System.out.println("Move!");
				panel.setMoveVisibility(true);
				panel.setAction(GamePanel.MOVE);
				panel.requestFocusInWindow();
				panel.drawMap();	
			}
		}

		class EndTurn extends GameAction{
			EndTurn(GamePanel p){
				super("End Turn", p);
			}

			boolean legalAction(){
				return selected.isAvailable();
			}

			void desiredAction(){
				System.out.println("End Turn!");
				selected.setAvailability(false);
				GameEngine.this.deselect();
			}
		}

		class Back extends GameAction{
			Back(GamePanel p){
				super("Back", p);
			}

			boolean legalAction(){
				return !selected.isAvailable();
			}

			void desiredAction(){
				GameEngine.this.deselect();
			}
		}
	}

	class AttackResults extends JPanel{
		Unit attacker;
		Unit defender;
		JLabel atkName;
		JLabel defName;
		JLabel atkHp;
		JLabel defHp;
		JLabel atkMight;
		JLabel defMight;
		JLabel atkHit;
		JLabel defHit;
		JLabel atkCrit;
		JLabel defCrit;
		private String HP = "HP: ";
		private String MT = "Mt: ";
		private String HIT = "Ht: ";
		private String CRIT = "Crt: ";

		public AttackResults(){
			atkName = new JLabel();
			defName = new JLabel();
			atkHp = new JLabel();
			defHp = new JLabel();
			atkMight = new JLabel();
			defMight = new JLabel();
			atkHit = new JLabel();
			defHit = new JLabel();
			atkCrit = new JLabel();
			defCrit = new JLabel();
			resetFields();

			GroupLayout gl = new GroupLayout(this);
			gl.setAutoCreateGaps(true);
			gl.setAutoCreateContainerGaps(true);
			
			gl.setHorizontalGroup(
				gl.createSequentialGroup()
					.addGroup(gl.createParallelGroup()
						.addComponent(atkName)
						.addComponent(atkHp)
						.addComponent(atkMight)
						.addComponent(atkHit)
						.addComponent(atkCrit))
					.addGroup(gl.createParallelGroup()
						.addComponent(defName)
						.addComponent(defHp)
						.addComponent(defMight)
						.addComponent(defHit)
						.addComponent(defCrit))
			);

			gl.setVerticalGroup(
				gl.createSequentialGroup()
					.addGroup(gl.createParallelGroup()
						.addComponent(atkName)
						.addComponent(defName))
					.addGroup(gl.createParallelGroup()
						.addComponent(atkHp)
						.addComponent(defHp))
					.addGroup(gl.createParallelGroup()
						.addComponent(atkMight)
						.addComponent(defMight))
					.addGroup(gl.createParallelGroup()
						.addComponent(atkHit)
						.addComponent(defHit))
					.addGroup(gl.createParallelGroup()
						.addComponent(atkCrit)
						.addComponent(defCrit))
			);
			setLayout(gl);
		} 

		public void resetFields(){
			atkName.setText("Name");
			defName.setText("Name");

			atkHp.setText(HP);
			defHp.setText(HP);

			atkMight.setText(MT);
			defMight.setText(MT);

			atkHit.setText(HIT);
			defHit.setText(HIT);

			atkCrit.setText(CRIT);
			defCrit.setText(CRIT);
		}

		public void showResults(Unit attacker, Unit defender){
			Item nullItem  = new Item("", "", 0,0,0,0,0,0);
			Optional<Item> atkItem = attacker.getEquipped();
			Optional<Item> defItem = defender.getEquipped();
			
			int atkSpeed = attacker.getStat("speed") - Math.max(0, atkItem.orElse(nullItem).getWeight() - attacker.getStat("constitution"));
			int defSpeed = defender.getStat("speed") - Math.max(0, defItem.orElse(nullItem).getWeight() - defender.getStat("constitution"));

			atkName.setText(attacker.getName());
			defName.setText(defender.getName());

			atkHp.setText(HP+attacker.getStat("hp"));
			defHp.setText(HP+defender.getStat("hp"));
			if(atkItem.isPresent()){
				atkMight.setText(MT+ (attacker.getStat("strength") + atkItem.get().getMight()));
				atkHit.setText(HIT + Math.min(100, attacker.getStat("skill") * 2 
						+ attacker.getStat("luck") / 2 + atkItem.get().getHit()
						- (defSpeed * 2 + defender.getStat("luck"))));	
			}if(defItem.isPresent()){
				defMight.setText(MT+ (defender.getStat("strength") + defItem.get().getMight()));
				defHit.setText(HIT + Math.min(100, defender.getStat("skill") * 2 
						+ defender.getStat("luck") / 2 + defItem.get().getHit()
						- (atkSpeed * 2 + attacker.getStat("luck"))));	
			}	
		}
	}
}