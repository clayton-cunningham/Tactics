package com.clay13chopper.game1.entities.displays;

import com.clay13chopper.game1.entities.Entity;
import com.clay13chopper.game1.graphics.Sprite;

/**
 * Generic UI class for any UI that needs to be displayed
 * @author Clayton Cunningham
 *
 */
public class GenericUI extends Entity {
	
	public GenericUI(int x, int y, Sprite s) {
		this.x = x;
		this.y = y;
		sprite = s;
	}

}
