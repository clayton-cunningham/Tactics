package com.clay13chopper.game1.graphics;

import com.clay13chopper.game1.entities.mob.Unit;

/**
 * This class holds all sprites for reference during runtime
 * @author Clayton Cunningham
 *
 */
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
	public static Sprite enemyMoveSpace = new Sprite(16, 16,  2, 7, SpriteSheet.background_tiles);
	public static Sprite enemyAttackSpace = new Sprite(16, 16,  3, 7, SpriteSheet.background_tiles);
	public static Sprite enemyAttackSpace2 = new Sprite(16, 16,  3, 6, SpriteSheet.background_tiles);

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
	public static Sprite blueArcherDown2 = new Sprite(16, 16,  2, 5, SpriteSheet.example_player);
	public static Sprite blueArcherDown0Done = new Sprite(16, 16,  0, 5, SpriteSheet.example_player_done);
	public static Sprite blueArcherDown2Done = new Sprite(16, 16,  2, 5, SpriteSheet.example_player_done);
	
	public static Sprite redArcherDown0 = new Sprite(16, 16,  3, 5, SpriteSheet.example_player);
	public static Sprite redArcherDown2 = new Sprite(16, 16,  5, 5, SpriteSheet.example_player);
	public static Sprite redArcherDown0Done = new Sprite(16, 16,  3, 5, SpriteSheet.example_player_done);
	public static Sprite redArcherDown2Done = new Sprite(16, 16,  5, 5, SpriteSheet.example_player_done);

	public static Sprite blueMageDown0 = new Sprite(16, 16,  0, 1, SpriteSheet.example_player);
	public static Sprite blueMageDown2 = new Sprite(16, 16,  2, 1, SpriteSheet.example_player);
	public static Sprite blueMageDown0Done = new Sprite(16, 16,  0, 1, SpriteSheet.example_player_done);
	public static Sprite blueMageDown2Done = new Sprite(16, 16,  2, 1, SpriteSheet.example_player_done);
	
	public static Sprite redMageDown0 = new Sprite(16, 16,  3, 1, SpriteSheet.example_player);
	public static Sprite redMageDown2 = new Sprite(16, 16,  5, 1, SpriteSheet.example_player);
	public static Sprite redMageDown0Done = new Sprite(16, 16,  3, 1, SpriteSheet.example_player_done);
	public static Sprite redMageDown2Done = new Sprite(16, 16,  5, 1, SpriteSheet.example_player_done);

	public static Sprite blueHeavyDown0 = new Sprite(16, 16,  0, 4, SpriteSheet.example_player);
	public static Sprite blueHeavyDown2 = new Sprite(16, 16,  2, 4, SpriteSheet.example_player);
	public static Sprite blueHeavyDown0Done = new Sprite(16, 16,  0, 4, SpriteSheet.example_player_done);
	public static Sprite blueHeavyDown2Done = new Sprite(16, 16,  2, 4, SpriteSheet.example_player_done);
	
	public static Sprite redHeavyDown0 = new Sprite(16, 16,  3, 4, SpriteSheet.example_player);
	public static Sprite redHeavyDown2 = new Sprite(16, 16,  5, 4, SpriteSheet.example_player);
	public static Sprite redHeavyDown0Done = new Sprite(16, 16,  3, 4, SpriteSheet.example_player_done);
	public static Sprite redHeavyDown2Done = new Sprite(16, 16,  5, 4, SpriteSheet.example_player_done);

	public static Sprite blueRunnerDown0 = new Sprite(16, 16,  0, 6, SpriteSheet.example_player);
	public static Sprite blueRunnerDown2 = new Sprite(16, 16,  2, 6, SpriteSheet.example_player);
	public static Sprite blueRunnerDown0Done = new Sprite(16, 16,  0, 6, SpriteSheet.example_player_done);
	public static Sprite blueRunnerDown2Done = new Sprite(16, 16,  2, 6, SpriteSheet.example_player_done);
	
	public static Sprite redRunnerDown0 = new Sprite(16, 16,  3, 6, SpriteSheet.example_player);
	public static Sprite redRunnerDown2 = new Sprite(16, 16,  5, 6, SpriteSheet.example_player);
	public static Sprite redRunnerDown0Done = new Sprite(16, 16,  3, 6, SpriteSheet.example_player_done);
	public static Sprite redRunnerDown2Done = new Sprite(16, 16,  5, 6, SpriteSheet.example_player_done);
	
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
	
	//Paths
	public static Sprite pathBlueStartUp = new Sprite(16, 16, 0, 0, SpriteSheet.paths);
	public static Sprite pathBlueStartDown = new Sprite(16, 16, 1, 0, SpriteSheet.paths);
	public static Sprite pathBlueStartLeft = new Sprite(16, 16, 2, 0, SpriteSheet.paths);
	public static Sprite pathBlueStartRight = new Sprite(16, 16, 3, 0, SpriteSheet.paths);
	public static Sprite pathBlueHorizontal = new Sprite(16, 16, 6, 0, SpriteSheet.paths);
	public static Sprite pathBlueVertical = new Sprite(16, 16, 7, 0, SpriteSheet.paths);
	public static Sprite pathBlueCornerDownRight = new Sprite(16, 16, 4, 0, SpriteSheet.paths);
	public static Sprite pathBlueCornerDownLeft = new Sprite(16, 16, 5, 0, SpriteSheet.paths);
	public static Sprite pathBlueCornerUpRight = new Sprite(16, 16, 4, 1, SpriteSheet.paths);
	public static Sprite pathBlueCornerUpLeft = new Sprite(16, 16, 5, 1, SpriteSheet.paths);
	public static Sprite pathBlueEndUp = new Sprite(16, 16, 0, 1, SpriteSheet.paths);
	public static Sprite pathBlueEndDown = new Sprite(16, 16, 1, 1, SpriteSheet.paths);
	public static Sprite pathBlueEndLeft = new Sprite(16, 16, 2, 1, SpriteSheet.paths);
	public static Sprite pathBlueEndRight = new Sprite(16, 16, 3, 1, SpriteSheet.paths);
	public static Sprite pathBlueStart = new Sprite(16, 16, 6, 1, SpriteSheet.paths);
	
	
	//UI
	public static Sprite healthBar = new Sprite(16, 3, 0, 0, SpriteSheet.health_UI);
	public static Sprite healthHigh = new Sprite(16, 1, 1, 0, SpriteSheet.health_UI);
	public static Sprite healthMid = new Sprite(16, 1, 2, 0, SpriteSheet.health_UI);
	public static Sprite healthLow = new Sprite(16, 1, 3, 0, SpriteSheet.health_UI);
	public static Sprite win = new Sprite(48, 16, 0, 0, SpriteSheet.gameEnd_UI);
	public static Sprite lose = new Sprite(48, 16, 1, 0, SpriteSheet.gameEnd_UI);
	public static Sprite winInstructions = new Sprite(48, 16, 0, 1, SpriteSheet.gameEnd_UI);
	public static Sprite loseInstructions = new Sprite(48, 16, 1, 1, SpriteSheet.gameEnd_UI);
	public static Sprite level1 = new Sprite(320, 141, 0, 0, SpriteSheet.startMenu);
	public static Sprite level2 = new Sprite(320, 141, 0, 1, SpriteSheet.startMenu);
	public static Sprite level3 = new Sprite(320, 141, 0, 2, SpriteSheet.startMenu);
	public static Sprite levelTest = new Sprite(320, 141, 0, 3, SpriteSheet.startMenu);
	public static Sprite controls = new Sprite(600, 600, 0, 0, SpriteSheet.controls);

	public static Sprite menuBorderTopLeft = new Sprite(16, 14, 2, 0, SpriteSheet.menu_UI);
	public static Sprite menuBorderTop = new Sprite(16, 14, 3, 0, SpriteSheet.menu_UI);
	public static Sprite menuBorderTopRight = new Sprite(16, 14, 6, 0, SpriteSheet.menu_UI);
	public static Sprite menuBorderLeft = new Sprite(16, 14, 2, 1, SpriteSheet.menu_UI);
	public static Sprite menuBorderRight = new Sprite(16, 14, 6, 1, SpriteSheet.menu_UI);
	public static Sprite menuBorderBottomLeft = new Sprite(16, 14, 2, 2, SpriteSheet.menu_UI);
	public static Sprite menuBorderBottom = new Sprite(16, 14, 3, 2, SpriteSheet.menu_UI);
	public static Sprite menuBorderBottomRight = new Sprite(16, 14, 6, 2, SpriteSheet.menu_UI);
	public static Sprite menuSelector = new Sprite(48, 14, 0, 3, SpriteSheet.menu_UI);
	public static Sprite menuBlank = new Sprite(16, 14, 3, 3, SpriteSheet.menu_UI);
	public static Sprite menuEndTurn = new Sprite(48, 14, 1, 1, SpriteSheet.menu_UI);
	public static Sprite menuQuit = new Sprite(48, 14, 2, 3, SpriteSheet.menu_UI);
	public static Sprite menuAttack = new Sprite(48, 14, 0, 4, SpriteSheet.menu_UI);
	public static Sprite menuWait = new Sprite(48, 14, 1, 4, SpriteSheet.menu_UI);
	public static Sprite menuCancel = new Sprite(48, 14, 2, 4, SpriteSheet.menu_UI);
	public static Sprite menuTileDefenseSmall = new Sprite(8, 14, 0, 5, SpriteSheet.menu_UI);
	public static Sprite menuTileDefense = new Sprite(16, 14, 3, 5, SpriteSheet.menu_UI);
	public static Sprite menuUnitHealth = new Sprite(16, 14, 4, 5, SpriteSheet.menu_UI);
	public static Sprite menuUnitAttack = new Sprite(16, 14, 5, 5, SpriteSheet.menu_UI);
	
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

	/**
	 * Used once at load time to load the sprite from its sprite sheet
	 */
	private void load() {
		for (int yy = 0; yy < height; yy++) {
			for (int xx = 0; xx < width; xx++) {
				pixels[xx + yy * width] = sheet.getPixel((xx + this.x) + (yy + this.y) * sheet.getWidth());
			}
		}
	}
	
	/**
	 * Designs a health bar to be displayed for the input Unit
	 * @param u		Unit in need of a health bar
	 * @return		A custom health bar based on the Unit's health
	 */
	public static Sprite showHealth(Unit u) {

		int healthPercent = u.getHealthPercent();
		int healthWidth = (healthPercent * 12) / 100;
		if (healthPercent > 66) {
			healthHigh.width = healthWidth;
			return healthHigh;
		}
		else if (healthPercent > 33) {
			healthMid.width = healthWidth;
			return healthMid;
		}
		else {
			healthLow.width = healthWidth;
			return healthLow;
		}
	}
	
	// Get methods
	public int getWidth() 				{	return width;				}
	public int getHeight() 				{	return height;				}
	public int getPixel(int location) 	{	return pixels[location];	}

}
