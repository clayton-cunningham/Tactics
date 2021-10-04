package com.clay13chopper.game1.tiles;

import com.clay13chopper.game1.graphics.Sprite;

/**
 * Void:	out of bounds
 * @author Clayton Cunningham
 *
 */
public class VoidTile extends Tile {

	public VoidTile(Sprite s) {
		super(s);
	}

	public boolean solid() 			{	return true;	}
	public boolean outOfBounds() 	{	return true;	}
	
}
