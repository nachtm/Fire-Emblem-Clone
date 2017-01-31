#Fire-Emblem-Clone

Fire-Emblem-Clone is a project to create a working version of Fire Emblem, written entirely in Java. It was written mostly in the summer of 2015, when I had nothing else CS-related to do (though I only uploaded it to github the next winter). The Map Builder is currently fully functional, but the game engine itself is still in the development process. 

##MapBuilder

MapBuilder is the tile editor, which is complete and should be relatively intuitive. I'm no GUI expert and there are clearly tools that would be useful to have (fill a certain rectangle, undo, redo, etc) but a functional GUI was enough for me at the time.

###Initialization
MapBuilder has two command-line usages:

java MapBuilder filename.txt
	loads a previously generated map (maps are saved as text files)

java MapBuilder filename.txt TileDir
	creates a new map using a given tile directory. Directories are located inside the "Tiles" folder. For example, If I wanted to use the OverworldRegular tileset from FE7, I would call java MapBuilder map.txt Tiles/FE7/OverworldRegular from the command line.

a.txt and b.txt are two files I used for testing so feel free to take a look at them to see a previously generated file.

###Usage Guide

On the left side of the screen is a the map you're editing. On the right is the tileset palette. One thing I'd do in the future is clearly denote where one tile begins and another ends. 

To select a tile to add to your map, click a tile from the palette and notice that the "current tile" at the bottom changes to the tile you selected. 
Then, go ahead and click anywhere on the map, and you should see that the tile on the map changes to match!

####Other options
Fill with current tile fills the entire map with a tile. I'd recommend that you fill the entire map with a grass or forest tile before you get started. 
The X and Y textboxes let you redefine the dimensions of the map you're working with. Be careful, MapBuilder doesn't save tiles that are removed through this method! That is, if you shrink the map you'll lose information. 
Dimension changes don't take effect until you click "Apply Changes".
As mentioned above, Current Tile displays the tile you have currently selected. Finally, Save both prints the textfile to the command line (for debugging purposes) and writes the data to the file specified upon initialization.

##GameEngine
Currently not functional. As a matter of fact, GameEngine.java doesn't compile at the moment. I'm working on that. 