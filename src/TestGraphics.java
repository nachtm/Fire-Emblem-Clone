import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TestGraphics{
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI(){
		System.out.println("Created GUI on EDT? " +
				SwingUtilities.isEventDispatchThread());
		JFrame f = new JFrame("Graphics Experiments");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MyPanel p = new MyPanel();
		f.add(p);
		f.pack();
		f.setVisible(true);
	}
}

class MyPanel extends JPanel {

	RedSquare square = new RedSquare();

	public MyPanel() {
		setBorder(BorderFactory.createLineBorder(Color.black));

		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				moveSquare(e.getX(),e.getY());
			}
		});

		addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e){
				moveSquare(e.getX(),e.getY());
			}
		});

	}

	private void moveSquare(int x, int y) {
		final int CURR_X = square.getX();
		final int CURR_Y = square.getY();
		final int CURR_W = square.getWidth();
		final int CURR_H = square.getHeight();
		final int OFFSET = 2;

		if((CURR_X != x) || (CURR_Y!=y)){
			repaint(CURR_X,CURR_Y,CURR_W+OFFSET, CURR_H+OFFSET);
			square.setX(x);
			square.setY(y);
			repaint(x,y,CURR_W+OFFSET, CURR_H+OFFSET);
		}
	}
	public Dimension getPreferredSize(){
		return new Dimension(250,200);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);

		g.drawString("This is my custom Panel!", 10,20);

		square.paintSquare(g);
	}

	class RedSquare{

		private int xPos = 50;
		private int yPos = 50;
		private int width = 20;
		private int height = 20;

		public void setX(int xPos){
			this.xPos = xPos;
		}
		public int getX(){
	        return xPos;
	    }

	    public void setY(int yPos){
	        this.yPos = yPos;
	    }

	    public int getY(){
	        return yPos;
	    }

	    public int getWidth(){
	        return width;
	    } 

	    public int getHeight(){
	        return height;
	    }

	    public void paintSquare(Graphics g){
	    	g.setColor(Color.RED);
	    	g.fillRect(xPos,yPos,width,height);
	    	g.setColor(Color.BLACK);
	    	g.drawRect(xPos,yPos,width,height);

	    }
	}
}