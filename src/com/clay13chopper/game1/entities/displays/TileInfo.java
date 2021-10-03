package com.clay13chopper.game1.entities.displays;

import com.clay13chopper.game1.entities.cursors.MapCursor;
import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.tiles.Tile;

/**
 * Shows info about the tile and unit currently hovered over
 * @author Clayton Cunningham
 *
 */
public class TileInfo extends TextBox {
	
	private Sprite tileSprite;
	private int tileDefense;
	private Sprite unitSprite;
	private MapCursor cursor;
	
	public TileInfo(int x, int y, MapCursor c) {
		spriteWidth = Sprite.menuBorderTop.getWidth();
		spriteHeight = Sprite.menuBorderTop.getHeight();
		boxHeight = 2;
		this.x = x - (spriteWidth / 2);
		this.y = y - (spriteHeight / 2);
		cursor = c;
	}
	
	// TODO: make TileInfo move if cursor goes near it
	
	public void update() {
		super.update();
		
		// TODO: Should we have tile and unit be global?
		
		// Retrieve the current tile's data
		Tile tile = level.getTile(cursor.getXGrid(), cursor.getYGrid());
		tileSprite = tile.getSprite();
		tileDefense = tile.defense();
		
		// Retrieve the current unit's data
		Unit unit = level.getUnit(cursor.getXGrid(), cursor.getYGrid());
		if (unit != null) {
			unitSprite = unit.getSprite();
			boxWidth = 2;
		}
		else {
			unitSprite = null;
			boxWidth = 1;
		}

		fillBoxSprites();
	}
	
	public void render(Screen screen) {
		int xOffset = x + Screen.getXOffset();
		int yOffset = y + Screen.getYOffset();

		renderBox(screen, xOffset, yOffset);
		
		// Display unit
		if (unitSprite != null) {
			screen.renderSprite(xOffset + spriteWidth, yOffset, unitSprite, false, false);
		}
		
		// Display tile and tile's defense values
		screen.renderSprite(xOffset, yOffset, tileSprite, false, false);
		for (int i = 0; i < tileDefense; i++) {
			int xi = xOffset 							+ ((i % 2) * spriteWidth / 2);
			int yi = yOffset + tileSprite.getHeight() 	+ ((i / 2) * spriteWidth / 2);
			screen.renderSprite(xi, yi, Sprite.menuTileDefenseSmall, false, false);
		}
		
	}

}
