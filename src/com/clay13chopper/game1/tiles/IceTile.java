package com.clay13chopper.game1.tiles;

import com.clay13chopper.game1.graphics.Sprite;

/**
 * Ice:		Wall tile - blocks movement
 * @author Clayton Cunningham
 *
 */
public class IceTile extends Tile {

	public IceTile(Sprite s) {
		super(s);
	}
	
	public boolean solid() 		{	return true;	}
	public boolean breakable() 	{	return true;	}

}
