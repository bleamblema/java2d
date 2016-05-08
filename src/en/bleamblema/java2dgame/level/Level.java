package en.bleamblema.java2dgame.level;

import en.bleamblema.java2dgame.gfx.Screen;
import en.bleamblema.java2dgame.tiles.Tile;

public class Level {
	private byte[] tiles;
	public int width;
	public int height;
	
	public Level(int width, int height){
		tiles = new byte[width*height];
		this.width = width;
		this.height = height;
		
		this.generateLevel();
	}

	public void generateLevel() {
		// set Data to byte[] tiles
		// generate data
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				if( x * y % 10 < 5){
					tiles[x+y*this.width] = Tile.GRASS.getId();
				}else{
					tiles[x+y*this.width] = Tile.STONE.getId();
				}
			}
		}	
	}
	
	public void tick() {
		 
	}
	
	
	public void renderTiles(Screen screen, int xOffset, int yOffset){
		//safe guard
		if(xOffset < 0) xOffset = 0;
		if(xOffset > (this.width << 3) - screen.width)
			xOffset = (this.width << 3) - screen.width;
		if(yOffset < 0) yOffset = 0;
		if(yOffset > (this.height << 3) - screen.height)
			yOffset = (this.height << 3) - screen.height;
		
		//set screen according to Level
		screen.setOffset(xOffset, yOffset);
		
		for(int y=0; y < this.height; y++){
			for(int x=0; x < this.width; x++){
				getTile(x,y).render(screen, this, x << 3, y <<3);
			}
		}
	}

	private Tile getTile(int x, int y) {
		if(x<0 || x>width|| y<0 || y>height) return Tile.VOID;
		
		return Tile.tiles[tiles[x+y*width]];
	}

}
