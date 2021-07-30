package com.clay13chopper.game1.entities.displays;

import java.util.ArrayList;
import java.util.List;

import com.clay13chopper.game1.entities.Entity;
import com.clay13chopper.game1.entities.cursors.MapCursor;
import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.graphics.Sprite;

public class TileInfo extends Entity {
	
	private List<Sprite> sprites = new ArrayList<Sprite>();
	private Sprite tileSprite;
	private Sprite unitSprite;
	
	//TODO: Cleanup! 
	public TileInfo(int x, int y) {
		sprite = Sprite.voidSprite;
		this.x = x;
		this.y = y;
	}
	
	public void update() {
		super.update();
		
		MapCursor cursor = (MapCursor) level.getFocus();
		tileSprite = level.getTile(cursor.getX() / level.getTileSize(), cursor.getY() / level.getTileSize()).getSprite();
		
		Unit unit = level.getUnit(cursor.getX() / level.getTileSize(), cursor.getY() / level.getTileSize());
		if (unit != null) unitSprite = unit.getSprite();
		else unitSprite = null;
	}
	
	public void render(Screen screen) {
		int xOffset = x + Screen.getXOffset() - (sprite.getWidth() / 2);
		int yOffset = y + Screen.getYOffset() - (sprite.getHeight() / 2);
		screen.renderSprite(xOffset, yOffset, tileSprite, false, false);
		if (unitSprite != null)
			screen.renderSprite(xOffset + level.getTileSize(), yOffset, unitSprite, false, false);
		
		
	}

}
