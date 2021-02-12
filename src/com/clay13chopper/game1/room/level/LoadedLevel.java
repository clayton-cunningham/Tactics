package com.clay13chopper.game1.room.level;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.clay13chopper.game1.entities.cursors.MapCursor;
import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.entities.mob.Unit.Team;
import com.clay13chopper.game1.entities.mob.player_units.Soldier;
import com.clay13chopper.game1.processors.PathFinder;
import com.clay13chopper.game1.tiles.Tile;

public class LoadedLevel extends Level {
	
	public LoadedLevel(String path) {
		super();
		loadLevel(path);
		locations = new Unit[width * height];
		pathFinder= new PathFinder(width, height, this);
		scale = 5;
		loadEntities();
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
	
	public void loadEntities() {
		add(new Soldier(24, 24, Team.BLUE));
		add(new Soldier(56, 88, Team.BLUE));
		add(new Soldier(88, 56, Team.BLUE));
		add(new Soldier(168, 136, Team.RED));
		add(new Soldier(56, 24, Team.RED));
		add(new Soldier(136, 56, Team.RED));
		focus = new MapCursor(8,8);
		add(focus);
	}
	
	public void reset() {
		super.reset();
		loadEntities();
	}

}
