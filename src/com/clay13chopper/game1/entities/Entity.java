package com.clay13chopper.game1.entities;

import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.room.Room;
import com.clay13chopper.game1.room.level.Level;

public abstract class Entity {

	protected Sprite sprite;
	protected int x, y, xOld, yOld;
	protected int movement;
	protected int anim;
	protected int delay;
	protected boolean removed = false;
	protected Level level;
	protected Room room;

	public final static int colBlueSoldierUnit = 0xff0000ff;
	public final static int colRedSoldierUnit = 0xffff0000;
	public final static int colBlueArcherUnit = 0xff5050ff;
	public final static int colRedArcherUnit = 0xffff5050;
	public final static int colYellowUnit = 0xff00ffdd;
	public final static int colGreenUnit = 0xff00ff00;
	public final static int colCursor = 0xff01aaff;

	public void init(Level l) {
		level = l;
		room = l;
	}
	
	public void init(Room r) {
		room = r;
	}

	public void update() {
		if (anim < 3000) anim++; else anim = 0;
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
