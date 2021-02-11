package com.clay13chopper.game1.entities.text;

import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.room.level.Level;

public class LevelSelector extends TextBox {
	
	protected Level level;
	
	public LevelSelector(int x, int y, Sprite s) {
		this.x = x;
		this.y = y;
		sprite = s;
	}
	
	

}
