package com.clay13chopper.game1.entities.displays;

import com.clay13chopper.game1.entities.cursors.MapCursor;
import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.tiles.Tile;

public class TileInfo extends TextBox {
	
	private Sprite tileSprite;
	private int tileDefense;
	private Sprite unitSprite;
	private MapCursor cursor;
	
	//TODO: Cleanup! 
	public TileInfo(int x, int y, MapCursor c) {
		spriteWidth = Sprite.menuBorderTop.getWidth();
		spriteHeight = Sprite.menuBorderTop.getHeight();
		boxHeight = 2;
		this.x = x - (spriteWidth / 2);
		this.y = y - (spriteHeight / 2);
		cursor = c;
	}
	
	public void update() {
		super.update();
		
		Tile tile = level.getTile(cursor.getXGrid(), cursor.getYGrid());
		tileSprite = tile.getSprite();
		tileDefense = tile.defense();
		
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
		
		if (unitSprite != null) {
			screen.renderSprite(xOffset + spriteWidth, yOffset, unitSprite, false, false);
		}
		
		screen.renderSprite(xOffset, yOffset, tileSprite, false, false);
		for (int i = 0; i < tileDefense; i++) {
			screen.renderSprite(xOffset + ((i % 2) * spriteWidth / 2), yOffset + tileSprite.getHeight() + ((i / 2) * spriteWidth / 2), 
					Sprite.menuTileDefenseSmall, false, false);
		}
		
	}

}
