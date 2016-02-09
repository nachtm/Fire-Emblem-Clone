/**
 * TODO: 
 * list of buttons?
 * map buttons to action
 * determine which buttons to display
 * 
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

public class GameMenu extends JPanel{
	private Unit selectedUnit;
	private int selectedAction;
	private JLabel noCharSelected;
	private List<GameAction> validActions;
	private List<GameAction> allActions;
	protected GameEngine game;

	public GameMenu(GameEngine master){
		noCharSelected = new JLabel("Select a character.");
		add(noCharSelected);
		game = master;

		validActions = new ArrayList<GameAction>();
		allActions = new ArrayList<GameAction>();

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
		selectedUnit = curr;
		for(GameAction a : allActions){
			if(a.legalAction()){
				validActions.add(a); //is this line necessary? Do we need a validActions list?
				add(a);
			}
		}

		if(validActions.size() > 0){
			validActions.get(0).highlight();
			selectedAction = 0;
		}
	}

	public void clearMenu(){
		validActions.clear();
		removeAll();
		selectedUnit = null;
		add(noCharSelected);
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

	
}

// select char -> select option -> select target square -> move character