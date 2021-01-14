package com.clay13chopper.game1.room.level;

import java.util.Random;

import com.clay13chopper.game1.tiles.Tile;

public class RandomLevel extends Level {

	private static Random random = new Random();

	public RandomLevel(int w, int h) {
		super();
		width = w;
		height = h;
		tiles = new int[width * height];
		generateLevel();
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
		if (tiles[x + (y * width)] == 0) return Tile.grass0;
		if (tiles[x + (y * width)] == 1) return Tile.grass1;
		if (tiles[x + (y * width)] == 2) return Tile.grass2;
		if (tiles[x + (y * width)] == 3) return Tile.snow;
		if (tiles[x + (y * width)] == 4) return Tile.ice;
		if (tiles[x + (y * width)] == 5) return Tile.mountain;
		if (tiles[x + (y * width)] == 6) return Tile.water;
		return Tile.voidTile;
	}
}
