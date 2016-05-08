package en.bleamblema.java2dgame.level;

import java.util.ArrayList;
import java.util.List;

import en.bleamblema.java2dgame.entities.Entity;
import en.bleamblema.java2dgame.entities.Player;
import en.bleamblema.java2dgame.gfx.Screen;
import en.bleamblema.java2dgame.tiles.Tile;

public class Level {
	private byte[] tiles;
	public int width;
	public int height;
	public List<Entity> entities = new ArrayList<Entity>();
	
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
		for(Entity e: entities){
			e.tick();
		}
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
	
	public void renderEntities(Screen screen){
		for(Entity e : entities){
			e.render(screen);
		}
	}

	private Tile getTile(int x, int y) {
		if(x<0 || x>width|| y<0 || y>height) return Tile.VOID;
		
		return Tile.tiles[tiles[x+y*width]];
	}

	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}

}
