package com.clay13chopper.game1.room.level;

import java.util.Random;

import com.clay13chopper.game1.entities.Entity;
import com.clay13chopper.game1.entities.cursors.MapCursor;
import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.entities.mob.Unit.Team;
import com.clay13chopper.game1.entities.mob.player_units.Archer;
import com.clay13chopper.game1.entities.mob.player_units.Heavy;
import com.clay13chopper.game1.entities.mob.player_units.Mage;
import com.clay13chopper.game1.entities.mob.player_units.Runner;
import com.clay13chopper.game1.entities.mob.player_units.Soldier;
import com.clay13chopper.game1.graphics.PathDisplay;
import com.clay13chopper.game1.processors.PathFinder;
import com.clay13chopper.game1.tiles.Tile;

public class RandomLevel extends Level {

	private int[] generatedEntities = new int[0];
	private static Random random = new Random();

	public RandomLevel(int w, int h) {
		super();
		width = w;
		height = h;
		tiles = new int[width * height];
		generatedEntities = new int[width * height];
		locations = new Unit[width * height];
		pathFinder= new PathFinder(width, height, this);
		pathDisplay = new PathDisplay(width);
		generateLevel();
		generateEntities();
	}
	
	protected void generateLevel() {
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tiles[x + (y * width)] = random.nextInt(7);
			}
		}
		
	}
	
	public Tile getTile(int x, int y) {
		//This will prevent the map from being rendered out of bounds
		if (x < 0 || y < 0 || x >= width || y >= height) return Tile.voidTile;
		int index = x + (y * width);
		if (tiles[index] == 0) return Tile.grass0;
		if (tiles[index] == 1) return Tile.grass1;
		if (tiles[index] == 2) return Tile.grass2;
		if (tiles[index] == 3) return Tile.snow;
		if (tiles[index] == 4) return Tile.ice;
		if (tiles[index] == 5) return Tile.mountain;
		if (tiles[index] == 6) return Tile.water;
		return Tile.voidTile;
	}
	
	protected void generateEntities() {
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (!getTile(x, y).solid())
					generatedEntities[x + (y * width)] = random.nextInt(11);
			}
		}
		getEntityTypes(generatedEntities);
		
	}
	
	protected void getEntityTypes(int[] entities) {
		
		for (int i = 0; i < entities.length; i++) {
			int e = entities[i];
			if (e == 1) add(new Soldier(iToX(i), iToY(i), Team.BLUE));
			else if (e == 2) add(new Archer(iToX(i), iToY(i), Team.BLUE));
			else if (e == 3) add(new Mage(iToX(i), iToY(i), Team.BLUE));
			else if (e == 4) add(new Heavy(iToX(i), iToY(i), Team.BLUE));
			else if (e == 5) add(new Runner(iToX(i), iToY(i), Team.BLUE));
			else if (e == 6) add(new Soldier(iToX(i), iToY(i), Team.RED));
			else if (e == 7) add(new Archer(iToX(i), iToY(i), Team.RED));
			else if (e == 8) add(new Mage(iToX(i), iToY(i), Team.RED));
			else if (e == 9) add(new Heavy(iToX(i), iToY(i), Team.RED));
			else if (e == 10) add(new Runner(iToX(i), iToY(i), Team.RED));
			else if (e == Entity.colYellowUnit) ;
			else if (e == Entity.colGreenUnit) ;
			else if (e == Entity.colCursor) {
				focus = new MapCursor(iToX(i), iToY(i));
				add(focus);
			}
		}
		if (focus == null) {focus = new MapCursor(8, 8);
							add(focus);}
	}
}
