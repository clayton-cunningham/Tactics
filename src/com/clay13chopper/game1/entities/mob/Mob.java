package com.clay13chopper.game1.entities.mob;

import com.clay13chopper.game1.entities.Entity;

/**
 * Parent class for any and all entities with actions or AI
 * 		Currently only used by Unit class - might not be necessary
 * @author Clayton Cunningham
 *
 */
public abstract class Mob extends Entity {
	
	protected int dir = 0; //up:0 ; right:1 ; down:2 ; left:3
	protected boolean moving = false;
	
	public Mob(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void move (int x, int y) {
		this.x = x;
		this.y = y;
	}

}
