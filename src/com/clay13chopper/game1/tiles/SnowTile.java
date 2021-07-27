package com.clay13chopper.game1.tiles;

import com.clay13chopper.game1.graphics.Sprite;

public class SnowTile extends Tile {

	public SnowTile(Sprite s) {
		super(s);
	}

	public int moveCostFoot()  {	return 2; }
	
}
