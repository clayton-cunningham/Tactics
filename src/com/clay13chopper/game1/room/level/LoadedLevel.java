package com.clay13chopper.game1.room.level;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

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

public class LoadedLevel extends Level {
	
	private int[] loadedEntities = new int[0];
	
	public LoadedLevel(String path, String pathE) {
		super();
		loadLevel(path);
		locations = new Unit[width * height];
		pathFinder= new PathFinder(width, height, this);
		pathDisplay = new PathDisplay(width);
		scale = 6;
		TILE_SIZE_SHIFT = 4;
		TILE_SIZE = (int) Math.pow(2, TILE_SIZE_SHIFT);
		loadEntities(pathE);
		if (focus == null) {focus = new MapCursor(8, 8);
							add(focus);}
	}
	
	protected void loadLevel(String path) {
		try {
			System.out.print("Trying to load: " + path + "...");
			BufferedImage image = ImageIO.read(LoadedLevel.class.getResource(path));
			width = image.getWidth();
			height = image.getHeight();
//			tiles = new Tile[width * height];
			tiles = new int[width * height];
			image.getRGB(0, 0, width, height, tiles, 0, width);
			System.out.println(" succeeded!");
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println(" failed!");
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(" failed!");
		}
	}
	
	public Tile getTile(int x, int y) {
		//This will prevent the map from being rendered out of bounds
		if (x < 0 || y < 0 || x >= width || y >= height) return Tile.voidTile;
		
		int index = x + (y * width);
		if (tiles[index] == Tile.colGrass) return Tile.grass0;
		else if (tiles[index] == Tile.colSnow) return Tile.snow;
		else if (tiles[index] == Tile.colIce) return Tile.ice;
		else if (tiles[index] == Tile.colMountain) return Tile.mountain;
		else if (tiles[index] == Tile.colSand) return Tile.sand;
		return Tile.voidTile;
	}
	
	protected void loadEntities(String path) {
		try {
			System.out.print("Trying to load: " + path + "...");
			BufferedImage image = ImageIO.read(LoadedLevel.class.getResource(path));
			width = image.getWidth();
			height = image.getHeight();
			loadedEntities = new int[width * height];
			image.getRGB(0, 0, width, height, loadedEntities, 0, width);
			System.out.println(" succeeded!");
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println(" failed!");
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(" failed!");
		}
		
		getEntityTypes(loadedEntities);
	}
	
	public void getEntityTypes(int[] loadedEntities) {
		
		for (int i = 0; i < loadedEntities.length; i++) {
			int e = loadedEntities[i];
			if (e == Entity.colBlueSoldierUnit) add(new Soldier(iToX(i), iToY(i), Team.BLUE));
			else if (e == Entity.colBlueArcherUnit) add(new Archer(iToX(i), iToY(i), Team.BLUE));
			else if (e == Entity.colBlueMageUnit) add(new Mage(iToX(i), iToY(i), Team.BLUE));
			else if (e == Entity.colBlueHeavyUnit) add(new Heavy(iToX(i), iToY(i), Team.BLUE));
			else if (e == Entity.colBlueRunnerUnit) add(new Runner(iToX(i), iToY(i), Team.BLUE));
			else if (e == Entity.colRedSoldierUnit) add(new Soldier(iToX(i), iToY(i), Team.RED));
			else if (e == Entity.colRedArcherUnit) add(new Archer(iToX(i), iToY(i), Team.RED));
			else if (e == Entity.colRedMageUnit) add(new Mage(iToX(i), iToY(i), Team.RED));
			else if (e == Entity.colRedHeavyUnit) add(new Heavy(iToX(i), iToY(i), Team.RED));
			else if (e == Entity.colRedRunnerUnit) add(new Runner(iToX(i), iToY(i), Team.RED));
			else if (e == Entity.colYellowUnit) ;
			else if (e == Entity.colGreenUnit) ;
			else if (e == Entity.colCursor) {
				focus = new MapCursor(iToX(i), iToY(i));
				add(focus);
			}
		}
	}
	
	private int iToX(int i) {
		return (i % width) * TILE_SIZE + (TILE_SIZE / 2);
	}
	
	private int iToY(int i) {
		return (i / width) * TILE_SIZE + (TILE_SIZE / 2);
	}
	
	public void reset() {
		super.reset();
		getEntityTypes(loadedEntities);
	}

}
