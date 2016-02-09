import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class StatSheet extends JPanel{
	private static final int STAT_OFFSET = 2; //Stats object contains 2 fewer fields
											 //it is important to the functionality
											 //that both have the same fields after
											 //the first 2 entries.
	private Unit selected;
	private JLabel name;
	private JLabel level;
	private JLabel role;
	private JLabel exp;
	private JLabel hp;
	private JLabel maxhp;
	private JLabel strength;
	private JLabel skill;
	private JLabel speed;
	private JLabel luck;
	private JLabel defense;
	private JLabel resistance;
	private JLabel constitution;
	private JLabel move;
	private JLabel[] labels;

	// private GroupLayout.Group horizNames;
	// private GroupLayout.Group horizUses;
	// private GroupLayout.Group vertical;
	private List<ItemLabel> invenLabels;
	//private JPanel charStats;
	//add in aid, travel, affinity and condition

	public StatSheet(){
		BoxLayout container = new BoxLayout(this, BoxLayout.Y_AXIS);

		JPanel charStats = new JPanel();

		name = new JLabel("Nobody Selected");
		level = new JLabel("Level:  ");
		role = new JLabel("Class:  ");
		exp = new JLabel("Exp: ");
		hp = new JLabel("HP: ");
		maxhp = new JLabel("/ ");
		strength = new JLabel("Str: ");
		skill = new JLabel("Skl: ");
		speed = new JLabel("Spd: ");
		luck = new JLabel("Lck: ");
		defense = new JLabel("Def: ");
		resistance = new JLabel("Res: ");
		constitution = new JLabel("Con: ");
		move = new JLabel("Mov: ");
		JLabel[] temp = {name,role,level,exp,hp,maxhp,strength,
				  		skill,speed,luck,defense,resistance,
				  		constitution,move};
		labels = temp;

		GroupLayout statsLayout = new GroupLayout(charStats);
		statsLayout.setAutoCreateGaps(true);
		statsLayout.setAutoCreateContainerGaps(true);
		statsLayout.setHorizontalGroup(
			statsLayout.createSequentialGroup()
				.addGroup(statsLayout.createParallelGroup()
					.addComponent(role)
					.addComponent(hp)
					.addComponent(strength)
					.addComponent(luck)
					.addComponent(move))
				.addGroup(statsLayout.createParallelGroup()
					.addComponent(name)
					.addComponent(level)
					.addComponent(maxhp)
					.addComponent(skill)
					.addComponent(defense)
					.addComponent(constitution))
				.addGroup(statsLayout.createParallelGroup()
					.addComponent(exp)
					.addComponent(speed)
					.addComponent(resistance))
		);

		statsLayout.setVerticalGroup(
			statsLayout.createSequentialGroup()
				.addComponent(name)
				.addGroup(statsLayout.createParallelGroup()
					.addComponent(role)
					.addComponent(level)
					.addComponent(exp))
				.addGroup(statsLayout.createParallelGroup()
					.addComponent(hp)
					.addComponent(maxhp))
				.addGroup(statsLayout.createParallelGroup()
					.addComponent(strength)
					.addComponent(skill)
					.addComponent(speed))
				.addGroup(statsLayout.createParallelGroup()
					.addComponent(luck)
					.addComponent(defense)
					.addComponent(resistance))
				.addGroup(statsLayout.createParallelGroup()
					.addComponent(move)
					.addComponent(constitution))
		);

		charStats.setLayout(statsLayout);
		add(charStats);

		JPanel inventory = new JPanel();
		JLabel nameLabel = new JLabel("Item");
		JLabel usesLabel = new JLabel("Uses Remaining");

		GroupLayout invenLayout = new GroupLayout(inventory);
		invenLayout.setAutoCreateGaps(true);
		invenLayout.setAutoCreateContainerGaps(true);
		GroupLayout.Group horizontal = invenLayout.createSequentialGroup();
		GroupLayout.Group horizNames = invenLayout.createParallelGroup().addComponent(nameLabel);
		GroupLayout.Group horizUses = invenLayout.createParallelGroup().addComponent(usesLabel);
		horizontal.addGroup(horizNames)
				.addGroup(horizUses);
		GroupLayout.Group vertical = invenLayout.createSequentialGroup()
										.addGroup(invenLayout.createParallelGroup()
											.addComponent(nameLabel)
											.addComponent(usesLabel));
		invenLabels = new ArrayList<ItemLabel>();

		for(int i = 0; i < Unit.MAX_INVENTORY; i++){
			ItemLabel a = new ItemLabel();
			horizNames.addComponent(a.getTitle());
			horizUses.addComponent(a.getUses());
			vertical.addGroup(
					invenLayout.createParallelGroup()
					.addComponent(a.getTitle())
					.addComponent(a.getUses())
			);
			invenLabels.add(a);
		}
		invenLayout.setHorizontalGroup(horizontal);
		invenLayout.setVerticalGroup(vertical);
		inventory.setLayout(invenLayout);
		add(inventory);

		setLayout(container);
	}	

	public void changeUnit(Unit u){
		resetFields();
		selected = u;
		if(selected!=null)	{
			name.setText(u.getName());
			role.setText("Placeholder");
			for(int i = STAT_OFFSET; i < labels.length; i++){
				labels[i].setText(labels[i].getText()
					+u.getStat(Stats.STAT_LOCATION[i-STAT_OFFSET]));
			}
			List<Item> inventory = u.getInventory();
			for(int i = 0; i < inventory.size(); i++){
				System.out.println("Inventory at "+i+ ": "+ inventory.get(i));

				invenLabels.get(i).setTitle(inventory.get(i).getName());
				invenLabels.get(i).setUses(inventory.get(i).getUses());

				System.out.println("Label: "+ invenLabels.get(i));
			}
		}
		repaint();
	}

	public void resetFields(){
		name.setText("Nobody Selected");
		level.setText("Level: ");
		role.setText("Class: ");
		exp.setText("Exp: ");
		hp.setText("HP: ");
		maxhp.setText("/ ");
		strength.setText("Str: ");
		skill.setText("Skl: ");
		speed.setText("Spd: ");
		luck.setText("Lck: ");
		defense.setText("Def: ");
		resistance.setText("Res: ");
		constitution.setText("Con: ");
		move.setText("Mov: ");

		for(int i = 0; i < Unit.MAX_INVENTORY; i++){
			invenLabels.get(i).setTitle("");
			invenLabels.get(i).setUses(0);
		}

		repaint();
	}	

	class ItemLabel extends JPanel{
		JLabel title;
		JLabel uses;

		public ItemLabel(){
			title = new JLabel();
			uses = new JLabel();
		}
		public ItemLabel(String title, int uses){
			this.title = new JLabel(title);
			this.uses = new JLabel(""+uses);
		}

		public JLabel getTitle(){
			return title;
		}

		public JLabel getUses(){
			return uses;
		}

		public void setTitle(String s){
			title.setText(s);
		}

		public void setUses(int i){
			if(i > 0){
				uses.setText("" + i);
			} else{
				uses.setText("");
			}
		}

		public String toString(){
			return "" + title.getText() + " " + uses.getText();
		}
	}	
}