package com.clay13chopper.game1.entities.displays;

import java.util.ArrayList;
import java.util.List;

import com.clay13chopper.game1.entities.Entity;
import com.clay13chopper.game1.entities.cursors.MapCursor;
import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.tiles.Tile;

public class TileInfo extends Entity {
	
	private List<Sprite> sprites = new ArrayList<Sprite>();
	private Sprite tileSprite;
	private int tileDefense;
	private Sprite unitSprite;
	private int spriteWidth;
	private int borderHeight;
	
	//TODO: Cleanup! 
	public TileInfo(int x, int y) {
		sprite = Sprite.voidSprite;
		spriteWidth = sprite.getWidth();
		borderHeight = Sprite.menuBorderTop.getHeight();
		this.x = x;
		this.y = y;
	}
	
	public void update() {
		super.update();
		
		MapCursor cursor = (MapCursor) level.getFocus();
		Tile tile = level.getTile(cursor.getX() / level.getTileSize(), cursor.getY() / level.getTileSize());
		tileSprite = tile.getSprite();
		tileDefense = tile.defense();
		
		Unit unit = level.getUnit(cursor.getX() / level.getTileSize(), cursor.getY() / level.getTileSize());
		if (unit != null) unitSprite = unit.getSprite();
		else unitSprite = null;
	}
	
	public void render(Screen screen) {
		int xOffset = x + Screen.getXOffset() - (sprite.getWidth() / 2);
		int yOffset = y + Screen.getYOffset() - (sprite.getHeight() * 3 / 2);

		screen.renderSprite(xOffset - spriteWidth, yOffset - borderHeight, Sprite.menuBorderTopLeftSmall, false, false);
		screen.renderSprite(xOffset, yOffset - borderHeight, Sprite.menuBorderTopSmall, false, false);
		screen.renderSprite(xOffset + spriteWidth, yOffset - borderHeight, Sprite.menuBorderTopRightSmall, false, false);
		screen.renderSprite(xOffset - spriteWidth, yOffset, Sprite.menuBorderLeftSmall, false, false);
		screen.renderSprite(xOffset, yOffset, tileSprite, false, false);
		screen.renderSprite(xOffset + spriteWidth, yOffset, Sprite.menuBorderRightSmall, false, false);
		screen.renderSprite(xOffset - spriteWidth, yOffset + borderHeight, Sprite.menuBorderLeftSmall, false, false);
		screen.renderSprite(xOffset, yOffset + borderHeight, Sprite.menuBlankSmall, false, false);
		
		for (int i = 0; i < tileDefense; i++) {
			screen.renderSprite(xOffset + ((i % 2) * spriteWidth / 2), yOffset + borderHeight + ((i / 2) * spriteWidth / 2), Sprite.menuTileDefenseSmall, false, false);
		}
		screen.renderSprite(xOffset + spriteWidth, yOffset + borderHeight, Sprite.menuBorderRightSmall, false, false);
		screen.renderSprite(xOffset - spriteWidth, yOffset + (2 * borderHeight), Sprite.menuBorderBottomLeftSmall, false, false);
		screen.renderSprite(xOffset, yOffset + (2 * borderHeight), Sprite.menuBorderBottomSmall, false, false);
		screen.renderSprite(xOffset + spriteWidth, yOffset + (2 * borderHeight), Sprite.menuBorderBottomRightSmall, false, false);
		
		if (unitSprite != null) {
			xOffset += level.getTileSize();
			screen.renderSprite(xOffset, yOffset - borderHeight, Sprite.menuBorderTopSmall, false, false);
			screen.renderSprite(xOffset + spriteWidth, yOffset - borderHeight, Sprite.menuBorderTopRightSmall, false, false);
			screen.renderSprite(xOffset, yOffset, Sprite.menuBlankSmall, false, false);
			screen.renderSprite(xOffset, yOffset, unitSprite, false, false);
			screen.renderSprite(xOffset + spriteWidth, yOffset, Sprite.menuBorderRightSmall, false, false);
			screen.renderSprite(xOffset, yOffset + borderHeight, Sprite.menuBlankSmall, false, false);
			screen.renderSprite(xOffset + spriteWidth, yOffset + borderHeight, Sprite.menuBorderRightSmall, false, false);
			screen.renderSprite(xOffset, yOffset + (2 * borderHeight), Sprite.menuBorderBottomSmall, false, false);
			screen.renderSprite(xOffset + spriteWidth, yOffset + (2 * borderHeight), Sprite.menuBorderBottomRightSmall, false, false);
		}
		
		
	}
	
	private void fillSprites() {
		sprites.add(Sprite.menuBorderTopLeft);
		sprites.add(Sprite.menuBorderTop);
		sprites.add(Sprite.menuBorderTopRight);

		sprites.add(Sprite.menuBorderLeft);
		
		sprites.add(Sprite.menuBorderRight);
		
		sprites.add(Sprite.menuBorderBottomLeft);
		sprites.add(Sprite.menuBorderBottom);
		sprites.add(Sprite.menuBorderBottomRight);
	}

}
