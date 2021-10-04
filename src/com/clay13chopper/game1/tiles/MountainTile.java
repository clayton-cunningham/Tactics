package com.clay13chopper.game1.tiles;

import com.clay13chopper.game1.graphics.Sprite;

/**
 * Mountain:	High defense, but slow to traverse
 * @author Clayton Cunningham
 *
 */
public class MountainTile extends Tile {

	public MountainTile(Sprite s) {
		super(s);
	}
	
	public boolean 	solid() 	{	return true;	}
	public int 		defense()	{	return 3; 		}
	
}
