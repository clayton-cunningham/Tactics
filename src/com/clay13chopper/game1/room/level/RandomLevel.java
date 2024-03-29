package com.clay13chopper.game1.room.level;

import java.util.Random;

import com.clay13chopper.game1.entities.Entity;
import com.clay13chopper.game1.entities.cursors.MapCursor;
import com.clay13chopper.game1.entities.displays.TileInfo;
import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.entities.mob.Unit.Team;
import com.clay13chopper.game1.entities.mob.player_units.Archer;
import com.clay13chopper.game1.entities.mob.player_units.Heavy;
import com.clay13chopper.game1.entities.mob.player_units.Mage;
import com.clay13chopper.game1.entities.mob.player_units.Runner;
import com.clay13chopper.game1.entities.mob.player_units.Soldier;
import com.clay13chopper.game1.graphics.PathDisplay;
import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.processors.PathFinder;
import com.clay13chopper.game1.tiles.Tile;

/**
 * Creates a level with semi-randomly generated tiles and entities
 * Further development is planned to allow random levels to be more controlled/recognizable
 * @author Clayton Cunningham
 *
 */
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
	
	/**
	 * Generates a level's layout
	 * Current modifications: tiles are likely to repeat adjacent tiles
	 */
	protected void generateLevel() {
		
		for (int yG = 0; yG < height; yG++) {
			for (int xG = 0; xG < width; xG++) {
				int chance = random.nextInt(3);
				if (chance == 0 && xG > 0) tiles[xG + (yG * width)] = tiles[(xG - 1) + (yG * width)];
				else if (chance == 1 && yG > 0) tiles[xG + (yG * width)] = tiles[xG + ((yG - 1) * width)];
				else tiles[xG + (yG * width)] = random.nextInt(7);
			}
		}
		
	}

	/**
	 * Returns the tile type at the input address
	 * @param xGrid	address in grid format
	 * @param yGrid address in grid format
	 * @returns Tile type at address
	 */
	public Tile getTile(int xGrid, int yGrid) {
		//This will prevent the map from being rendered out of bounds
		if (xGrid < 0 || yGrid < 0 || xGrid >= width || yGrid >= height) return Tile.voidTile;
		int index = xGrid + (yGrid * width);
		if (tiles[index] == 0) return Tile.grass0;
		if (tiles[index] == 1) return Tile.grass1;
		if (tiles[index] == 2) return Tile.grass2;
		if (tiles[index] == 3) return Tile.snow;
		if (tiles[index] == 4) return Tile.sand;
		if (tiles[index] == 5) return Tile.ice;
		if (tiles[index] == 6) return Tile.mountain;
		if (tiles[index] == 7) return Tile.water;
		return Tile.voidTile;
	}
	
	/**
	 * Generates entities randomly throughout the level
	 * Also destroys walls in the level as necessary to make sure it is playable
	 */
	protected void generateEntities() {
		
		for (int yG = 0; yG < height; yG++) {
			for (int xG = 0; xG < width; xG++) {
				if (!getTile(xG, yG).solid()) {
					int index = xG + (yG * width);
					generatedEntities[index] = random.nextInt(40);
					if (generatedEntities[index] < 11) {
						checkPossible(xG, yG);
						addEntityType(generatedEntities[index], iToX(index), iToY(index));
					}
				}
				
			}
		}
		if (focus == null) {focus = new MapCursor(8, 8);
							add(focus);}
		
	}
	
	/**
	 * Adds a specified entity to the level at the input address
	 * @param entity	integer representing the entity to add
	 * @param x	address NOT in grid format
	 * @param y address NOT in grid format
	 */
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
	
	/**
	 * Reloads entities in the level
	 */
	protected void reloadEntities() {
		for (int i = 0; i < generatedEntities.length; i++) {
			int e = generatedEntities[i];
			addEntityType(e, iToX(i), iToY(i));
		}
		if (focus == null) {focus = new MapCursor(8, 8);
							add(focus);}
	}

	/**
	 * Open path to other units, if the new unit is cut off
	 * @param xGrid	address in grid format
	 * @param yGrid	address in grid format
	 */
	protected void checkPossible(int xGrid, int yGrid) {
		int[] path = pathFinder.calcDesiredPath(width * height, 1, 1, xGrid, yGrid, Team.NONE);
		// If no other unit exists OR a wall is blocking this one
		if (path[0] == -1 && path[1] == xGrid + (yGrid * height)) {
			
			pathFinder.openWallsEdit();
			path = pathFinder.calcDesiredPath(width * height, 1, 1, xGrid, yGrid, Team.NONE);
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
						tiles[x + (yGrid * width)] = 7; // TODO: find better way to make paths
					}
					
				}
				int y = yGrid;
				while (y != yGoal ) {
					y += yDir;
					if (getTile(xGoal, y).solid()) {
						tiles[xGoal + (y * width)] = 7;
					}
				}
			}
		}
	}
	
	/**
	 * Reset the level
	 */
	public void reset() {
		super.reset();
		reloadEntities();
	}

	/**
	 * Prepares the level with anything that cannot be done during creation
	 * 		Right now that involves creating the TileInfo object, which requires the Screen to be set for the level
	 * 		TODO: Should this include anything done to start the level i.e. adding units?
	 */
	public void prep() {
		add(new TileInfo(TILE_SIZE, Screen.getHeight() - (TILE_SIZE * 2), (MapCursor) focus));
	}
}
