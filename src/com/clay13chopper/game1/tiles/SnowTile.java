package com.clay13chopper.game1.tiles;

import com.clay13chopper.game1.graphics.Sprite;

/**
 * Snow:	Slow movement, but some defense
 * @author Clayton Cunningham
 *
 */
public class SnowTile extends Tile {

	public SnowTile(Sprite s) {
		super(s);
	}

	public int moveCostFoot()  	{	return 2; }
	public int defense()	  	{	return 1; }
	
}
