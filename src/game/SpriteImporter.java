package game;

import common.Utils;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

public class SpriteImporter{
	public static final Color ALPHA = new Color(128,160,128);
	public static final Color TRANS = new Color(0,0,0,0);
	public static final String DIR = "Sprites/Units";
	public static void main(String[] args){
		File file = new File("Sprites/mapSprites.png");
		BufferedImage bigSprites = null;
		try{
			bigSprites = ImageIO.read(file);
		} catch(IOException e){
			System.out.println("hi");
		}
		List<BufferedImage> testItems = new ArrayList<BufferedImage>();

		for(int y = 42; y < 4600; y += 133){
			//if(y < 2965 || y > 3200){	
			BufferedImage current = Utils.toBufferedImage(Utils.getTileImage(
									bigSprites, 189, 220, y-1, y+25, 1));
			HashMap<Integer, Integer> toTransparent = new HashMap<Integer,Integer>();
			toTransparent.put(ALPHA.getRGB(), TRANS.getRGB());
			Unit.swapColors(current, toTransparent);
			current = trimImage(current);
			testItems.add(current);
			try{
				ImageIO.write(current, "png", new File(DIR+"/"+y+".png"));
			} catch(IOException e){
				System.out.println("whoops");
			}
			//}
		}
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				test(testItems);
			}
		});
	}


	public static BufferedImage trimImage(BufferedImage img){
		int width = img.getWidth();
		int height = img.getHeight();
		int leftWidth = -1;
		int topHeight = -1;
		int bottomHeight = -1;
		int rightWidth = -1;
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				if(topHeight == -1 && (img.getRGB(x,y) & 0xFF000000) != 0){
					topHeight = y; 
				}
			}
		}

		for(int y = height-1; y >=0; y--){
			for(int x = 0; x < width; x++){
				if(bottomHeight == -1 && (img.getRGB(x,y) & 0xFF000000) != 0){
					bottomHeight = y; 
				}
			}
		}

		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				if(leftWidth == -1 && (img.getRGB(x,y) & 0xFF000000) != 0){
					leftWidth = x; 
				}
			}
		}

		for(int x = width -1; x >= 0; x--){
			for(int y = 0; y < height; y++){
				if(rightWidth == -1 && (img.getRGB(x,y) & 0xFF000000) != 0){
					rightWidth = x; 
				}
			}
		}
		System.out.println(leftWidth+" "+rightWidth+" "+topHeight+" "+bottomHeight);
		Image toReturn = Utils.getTileImage(img, leftWidth, rightWidth, topHeight, bottomHeight, 1);
		return Utils.toBufferedImage(toReturn);
	}

	public static void test(List<BufferedImage> list){
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel content = new JPanel();
		for(int i=0; i < list.size(); i++){
			JLabel l = new JLabel(new ImageIcon(list.get(i)));
			l.setBorder(BorderFactory.createLineBorder(Color.black));
			content.add(l);
		}
		f.add(content);
		f.pack();
		f.setVisible(true);

	}
}