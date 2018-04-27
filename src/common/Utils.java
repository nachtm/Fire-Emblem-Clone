package common;

import engine.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.util.function.Predicate;

/**
 * Created by Micah on 2/18/2017.
 */
public class Utils {
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

	/**
	 * Uses a ImageFilter to return a cropped version of the image. Hopefully
	 * useful when handling tilesets.
	 */
	public static Image getTileImage(Image tileset, int x1, int x2, int y1, int y2, int scale){
		JPanel producer = new JPanel();
		//Crop tileset to grab tile
		ImageFilter cropper = new CropImageFilter(x1, y1, x2-x1, y2-y1);
		Image cropped = producer.createImage(new FilteredImageSource(tileset.getSource(), cropper));

		return scaleImage(cropped, scale);
	}

	public static Predicate<? super IUnit> isAtLocation(engine.Location loc){
		return (unit -> unit.getLocation().equals(loc));
	}

}
