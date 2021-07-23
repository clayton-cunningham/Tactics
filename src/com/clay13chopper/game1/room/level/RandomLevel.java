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
		activeTeam = Team.NONE;
		generateLevel();
		generateEntities();
		activeTeam = Team.BLUE;
	}
	
	protected void generateLevel() {
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tiles[x + (y * width)] = random.nextInt(6);
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
				if (!getTile(x, y).solid()) {
					int index = x + (y * width);
					generatedEntities[index] = random.nextInt(40);
					if (generatedEntities[index] < 11) checkPossible(x, y);
					addEntityType(generatedEntities[index], iToX(index), iToY(index));
				}
				
			}
		}
		if (focus == null) {focus = new MapCursor(8, 8);
							add(focus);}
		
	}
	
	protected void addEntityType(int entity, int x, int y) {
		if (entity == 1) add(new Soldier(x, y, Team.BLUE));
		else if (entity == 2) add(new Archer(x, y, Team.BLUE));
		else if (entity == 3) add(new Mage(x, y, Team.BLUE));
		else if (entity == 4) add(new Heavy(x, y, Team.BLUE));
		else if (entity == 5) add(new Runner(x, y, Team.BLUE));
		else if (entity == 6) add(new Soldier(x, y, Team.RED));
		else if (entity == 7) add(new Archer(x, y, Team.RED));
		else if (entity == 8) add(new Mage(x, y, Team.RED));
		else if (entity == 9) add(new Heavy(x, y, Team.RED));
		else if (entity == 10) add(new Runner(x, y, Team.RED));
		else if (entity == Entity.colYellowUnit) ;
		else if (entity == Entity.colGreenUnit) ;
	}
	
	protected void reloadEntities() {
		for (int i = 0; i < generatedEntities.length; i++) {
			int e = generatedEntities[i];
			addEntityType(e, iToX(i), iToY(i));
		}
		if (focus == null) {focus = new MapCursor(8, 8);
							add(focus);}
	}

	// Open path to other units, if the new unit is cut off
	protected void checkPossible(int xGrid, int yGrid) {
		int[] path = pathFinder.calcDesiredPath(width * height, 1, 1, xGrid, yGrid);
		// If no other unit exists OR a wall is blocking this one
		if (path[0] == -1 && path[1] == xGrid + (yGrid * height)) {
			
			pathFinder.openWallsEdit();
			path = pathFinder.calcDesiredPath(width * height, 1, 1, xGrid, yGrid);
			pathFinder.closeWallsEdit();
			
			// If another unit exists on the map, open a path to it
			if (path[0] != -1) {
				int xGoal = path[1] % width;
				int yGoal = path[1] / width;
				int xDir = (xGrid < xGoal) ? 1 : -1;
				int yDir = (yGrid < yGoal) ? 1 : -1;
				int x = xGrid;
				while (x != xGoal) {
					x += xDir;
					if (getTile(x, yGrid).solid()) {
						tiles[x + (yGrid * width)] = 6;
					}
					
				}
				int y = yGrid;
				while (y != yGoal ) {
					y += yDir;
					if (getTile(xGoal, y).solid()) {
						tiles[xGoal + (y * width)] = 6;
					}
				}
			}
		}
	}
	
	public void reset() {
		super.reset();
		reloadEntities();
	}
}
