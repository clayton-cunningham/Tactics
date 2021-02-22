package com.clay13chopper.game1.room.level;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.clay13chopper.game1.entities.Entity;
import com.clay13chopper.game1.entities.cursors.MapCursor;
import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.entities.mob.Unit.Team;
import com.clay13chopper.game1.entities.mob.player_units.Archer;
import com.clay13chopper.game1.entities.mob.player_units.Soldier;
import com.clay13chopper.game1.processors.PathFinder;
import com.clay13chopper.game1.tiles.Tile;

public class LoadedLevel extends Level {
	
	private int[] loadedEntities = new int[0];
	
	public LoadedLevel(String path, String pathE) {
		super();
		loadLevel(path);
		locations = new Unit[width * height];
		pathFinder= new PathFinder(width, height, this);
		scale = 5;
		loadEntities(pathE);
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
		if (tiles[x + (y * width)] == Tile.colGrass) return Tile.grass0;
		else if (tiles[x + (y * width)] == Tile.colSnow) return Tile.snow;
		else if (tiles[x + (y * width)] == Tile.colIce) return Tile.ice;
		else if (tiles[x + (y * width)] == Tile.colMountain) return Tile.mountain;
		else if (tiles[x + (y * width)] == Tile.colSand) return Tile.sand;
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
			if (e == Entity.colBlueSoldierUnit) add(new Soldier((i%16)*16 + 8, (i/16)*16 + 8, Team.BLUE));
			else if (e == Entity.colBlueArcherUnit) add(new Archer((i%16)*16 + 8, (i/16)*16 + 8, Team.BLUE));
			else if (e == Entity.colRedSoldierUnit) add(new Soldier((i%16)*16 + 8, (i/16)*16 + 8, Team.RED)) ;
			else if (e == Entity.colRedArcherUnit) add(new Archer((i%16)*16 + 8, (i/16)*16 + 8, Team.RED)) ;
			else if (e == Entity.colYellowUnit) ;
			else if (e == Entity.colGreenUnit) ;
			else if (e == Entity.colCursor) {
				focus = new MapCursor((i%16)*16 + 8, (i/16)*16 + 8);
				add(focus);
			}
		}
	}
	
	public void reset() {
		super.reset();
		getEntityTypes(loadedEntities);
	}

}
