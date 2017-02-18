package mapbuilder;

import common.Tile;
import common.Utils;

import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.awt.*;

public class TileGen{

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

		if(width% Tile.TILE_SIZE!=0 || height% Tile.TILE_SIZE!=0){
			System.out.println("Something's up. Tileset size doesn't match expectations.");
		}

		for(int i = 0; i < height; i+= Tile.TILE_SIZE){
			for(int j = 0; j < width; j+= Tile.TILE_SIZE){
				try{
					Image curr = Utils.getTileImage(localTileSet, j, j+ Tile.TILE_SIZE, i, i+ Tile.TILE_SIZE, 1);
					File output = new File(location+"/"+j+"_"+i+".png");
					ImageIO.write(Utils.toBufferedImage(curr),"png", output);

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

}