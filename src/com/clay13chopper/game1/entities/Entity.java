package com.clay13chopper.game1.entities;

import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.room.level.Level;

public abstract class Entity {

	protected Sprite sprite;
	protected int x, y, xd, yd;
	protected int anim;
	protected int delay;
	protected boolean removed = false;
	protected Level level;
	
	public void init(Level l) {
		level = l;
	}

	public void update() {
		anim++;
	}
	
	public void remove() {
		removed = true;
	}
	
	public boolean isRemoved() {
		return removed;
	}
	

	//Checks for collision on adjacent tiles
	protected boolean tileCollision(int xC, int yC) {

		int sW = sprite.getWidth();
		int sH = sprite.getHeight();
		
		//For [i % 2, i / 2] : [0,0], [1,0], [0,1], [1,1]
		for (int i = 0; i < 4; i++) {
			// Start at 0,0 of sprite, then add width/height alternatively
			int xi = (x + xC - (sW / 2) + (i % 2 * (sW - 1))) >> 4;
			int yi = (y + yC - (sH / 2) + (i / 2 * (sH - 1))) >> 4;
			
			if (level.getTile(xi, yi).solid()) return true; 
		}
		return false;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
}
