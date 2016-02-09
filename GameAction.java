import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;


//a JLabel plus a way to determine if a. the action is valid and b. what the
	//result of selecting it should be
abstract class GameAction extends JLabel{
	private String name;

	GameAction(String name){
		super(name);
		setBackground(Color.RED);
	}

	void highlight(){
		setOpaque(true);
	}

	void unHighlight(){
		setOpaque(false);
	}

	abstract boolean legalAction();

	abstract void desiredAction();
	}
