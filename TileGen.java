import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.awt.*;

public class TileGen{
	public static final int TILE_SIZE = 16;

	public static void main(String[] args){
		// if(args.length != 1){
		// 	System.err.println("Need exactly one filepath as an argument.");
		// 	System.exit(0);
		// }
		for(int i = 0; i < args.length; i++){
			System.out.print(args[i]);
			makeDir("Tiles/FE7/"+args[i]+".png");
			System.out.println("\t Done");
		}

	}

	public static void makeDir(String filepath){
		BufferedImage localTileSet = null;
		try{
			localTileSet = ImageIO.read(new File(filepath));
		} catch(IOException e){
			System.err.println(e);
			System.out.println("This error is likely thanks to an invalid tileset path");
			System.out.println("Problem with:" +filepath);
			System.exit(0);
		}
		String location = getDir(filepath);

		//make a directory to store individual png files.
		new File(location).mkdir();

		int width = localTileSet.getWidth();
		int height = localTileSet.getHeight();

		if(width%TILE_SIZE!=0 || height%TILE_SIZE!=0){
			System.out.println("Something's up. Tileset size doesn't match expectations.");
		}

		for(int i = 0; i < height; i+= TILE_SIZE){
			for(int j = 0; j < width; j+= TILE_SIZE){
				try{
					Image curr = MapBuilder.getTile(localTileSet, j, j+TILE_SIZE, i, i+TILE_SIZE, 1);
					File output = new File(location+"/"+j+"_"+i+".png");
					ImageIO.write(toBufferedImage(curr),"png", output);

				} catch(IOException e){
					System.out.println("whoops");
				}
			}
		}
	}

	public static String getDir(String filepath){
		StringBuilder s = new StringBuilder();
		StringBuilder temp = new StringBuilder();
		for(int i = 0; i < filepath.length(); i++){
			if(filepath.charAt(i)=='.'){
				s.append(temp);
			} else{
				temp.append(filepath.charAt(i));
			}
		}
		return s.toString();
	}

	public static BufferedImage toBufferedImage(Image img){
	    if (img instanceof BufferedImage){
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
}