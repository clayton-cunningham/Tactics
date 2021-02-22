package com.clay13chopper.game1.graphics;

import com.clay13chopper.game1.entities.mob.Unit;

public class Sprite {

	private final int x, y;
	private int width;
	private final int height;
	private final int[] pixels;
	private final SpriteSheet sheet;
	
	//Tiles
	public static Sprite grass0 = new Sprite(16, 16, 0, 0, SpriteSheet.background_tiles);
	public static Sprite grass1 = new Sprite(16, 16,  1, 0, SpriteSheet.background_tiles);
	public static Sprite grass2 = new Sprite(16, 16,  2, 0, SpriteSheet.background_tiles);
	public static Sprite snow = new Sprite(16, 16,  3, 0, SpriteSheet.background_tiles);
	public static Sprite ice = new Sprite(16, 16,  4, 0, SpriteSheet.background_tiles);
	public static Sprite mountain = new Sprite(16, 16,  5, 0, SpriteSheet.background_tiles);
	public static Sprite water = new Sprite(16, 16,  6, 0, SpriteSheet.background_tiles);
	public static Sprite sand = new Sprite(16, 16,  7, 0, SpriteSheet.background_tiles);

	public static Sprite moveSpace = new Sprite(16, 16,  0, 7, SpriteSheet.background_tiles);
	public static Sprite attackSpace = new Sprite(16, 16,  1, 7, SpriteSheet.background_tiles);

	//Mobs
	public static Sprite blueSoldierDown0 = new Sprite(16, 16,  0, 0, SpriteSheet.example_player);
	public static Sprite blueSoldierDown1 = new Sprite(16, 16,  1, 0, SpriteSheet.example_player);
	public static Sprite blueSoldierDown2 = new Sprite(16, 16,  2, 0, SpriteSheet.example_player);
	public static Sprite blueSoldierRight0 = new Sprite(16, 16,  3, 2, SpriteSheet.example_player);
	public static Sprite blueSoldierRight1 = new Sprite(16, 16,  4, 2, SpriteSheet.example_player);
	public static Sprite blueSoldierRight2 = new Sprite(16, 16,  5, 2, SpriteSheet.example_player);
	public static Sprite blueSoldierUp0 = new Sprite(16, 16,  3, 3, SpriteSheet.example_player);
	public static Sprite blueSoldierUp1 = new Sprite(16, 16,  4, 3, SpriteSheet.example_player);
	public static Sprite blueSoldierUp2 = new Sprite(16, 16,  5, 3, SpriteSheet.example_player);

	public static Sprite blueSoldierDown0Done = new Sprite(16, 16,  0, 0, SpriteSheet.example_player_done);
	public static Sprite blueSoldierDown1Done = new Sprite(16, 16,  1, 0, SpriteSheet.example_player_done);
	public static Sprite blueSoldierDown2Done = new Sprite(16, 16,  2, 0, SpriteSheet.example_player_done);
	
	public static Sprite redSoldierDown0 = new Sprite(16, 16,  3, 0, SpriteSheet.example_player);
	public static Sprite redSoldierDown1 = new Sprite(16, 16,  4, 0, SpriteSheet.example_player);
	public static Sprite redSoldierDown2 = new Sprite(16, 16,  5, 0, SpriteSheet.example_player);
	public static Sprite redSoldierRight0 = new Sprite(16, 16,  0, 2, SpriteSheet.example_player);
	public static Sprite redSoldierRight1 = new Sprite(16, 16,  1, 2, SpriteSheet.example_player);
	public static Sprite redSoldierRight2 = new Sprite(16, 16,  2, 2, SpriteSheet.example_player);
	public static Sprite redSoldierUp0 = new Sprite(16, 16,  0, 3, SpriteSheet.example_player);
	public static Sprite redSoldierUp1 = new Sprite(16, 16,  1, 3, SpriteSheet.example_player);
	public static Sprite redSoldierUp2 = new Sprite(16, 16,  2, 3, SpriteSheet.example_player);

	public static Sprite redSoldierDown0Done = new Sprite(16, 16,  3, 0, SpriteSheet.example_player_done);
	public static Sprite redSoldierDown1Done = new Sprite(16, 16,  4, 0, SpriteSheet.example_player_done);
	public static Sprite redSoldierDown2Done = new Sprite(16, 16,  5, 0, SpriteSheet.example_player_done);
	
	public static Sprite blueArcherDown0 = new Sprite(16, 16,  0, 5, SpriteSheet.example_player);
	public static Sprite blueArcherDown1 = new Sprite(16, 16,  1, 5, SpriteSheet.example_player);
	public static Sprite blueArcherDown2 = new Sprite(16, 16,  2, 5, SpriteSheet.example_player);
	public static Sprite blueArcherDown0Done = new Sprite(16, 16,  0, 5, SpriteSheet.example_player_done);
	public static Sprite blueArcherDown1Done = new Sprite(16, 16,  1, 5, SpriteSheet.example_player_done);
	public static Sprite blueArcherDown2Done = new Sprite(16, 16,  2, 5, SpriteSheet.example_player_done);
	
	public static Sprite redArcherDown0 = new Sprite(16, 16,  3, 5, SpriteSheet.example_player);
	public static Sprite redArcherDown1 = new Sprite(16, 16,  4, 5, SpriteSheet.example_player);
	public static Sprite redArcherDown2 = new Sprite(16, 16,  5, 5, SpriteSheet.example_player);
	public static Sprite redArcherDown0Done = new Sprite(16, 16,  3, 5, SpriteSheet.example_player_done);
	public static Sprite redArcherDown1Done = new Sprite(16, 16,  4, 5, SpriteSheet.example_player_done);
	public static Sprite redArcherDown2Done = new Sprite(16, 16,  5, 5, SpriteSheet.example_player_done);
	
	//Projectiles
	public static Sprite wizardProjectile1 = new Sprite(16, 16, 0, 0, SpriteSheet.projectiles1);
	
	//Particles
	public static Sprite particle1 = new Sprite(1, 1, 0, 0, SpriteSheet.background_tiles);
	
	//Cursors
	public static Sprite cursor1 = new Sprite(16, 16, 0, 0, SpriteSheet.cursors);
	public static Sprite cursor2 = new Sprite(16, 16, 1, 0, SpriteSheet.cursors);
	public static Sprite cursorError1 = new Sprite(16, 16, 0, 1, SpriteSheet.cursors);
	public static Sprite cursorError2 = new Sprite(16, 16, 1, 1, SpriteSheet.cursors);
	
	public static Sprite menuCursorLarge = new Sprite(80, 80, 0, 2, SpriteSheet.cursorsLarge);
	
	//UI
	public static Sprite healthBar = new Sprite(16, 16, 0, 0, SpriteSheet.UI);
	public static Sprite health = new Sprite(16, 1, 1, 0, SpriteSheet.UI);
	public static Sprite win = new Sprite(16, 16, 0, 1, SpriteSheet.UI);
	public static Sprite lose = new Sprite(16, 16, 1, 1, SpriteSheet.UI);
	public static Sprite level1 = new Sprite(320, 141, 0, 0, SpriteSheet.startMenu);
	public static Sprite level2 = new Sprite(320, 141, 0, 1, SpriteSheet.startMenu);
	public static Sprite level3 = new Sprite(320, 141, 0, 2, SpriteSheet.startMenu);
	
	//Void
	public static Sprite voidSprite = new Sprite(16, 16, 6, 7, SpriteSheet.background_tiles);
	public static Sprite emptySprite = new Sprite(0, 0, 0, 0, SpriteSheet.background_tiles);
	
	//Backgrounds
	public static Sprite background_start = new Sprite(1920, 1080, 0, 0, SpriteSheet.background_start);
	
	public Sprite(int w, int h, int x, int y, SpriteSheet sheet) {
		width = w;
		height = h;
		this.x = x * width;
		this.y = y * height;
		this.sheet = sheet;
		pixels = new int[w * h];
		load();
	}

	//Used once to get the sprite
	private void load() {
		for (int yy = 0; yy < height; yy++) {
			for (int xx = 0; xx < width; xx++) {
				pixels[xx + yy * width] = sheet.getPixel((xx + this.x) + (yy + this.y) * sheet.getWidth());
			}
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getPixel(int location) {
		return pixels[location];
	}
	
	public static Sprite makeHealth(Unit u) {
		health.width = (u.getHealthPercent() * 12) / 100;
		return health;
	}

}
