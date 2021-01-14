package com.clay13chopper.game1.tiles;

import com.clay13chopper.game1.graphics.Sprite;

public class IceTile extends Tile {

	public IceTile(Sprite s) {
		super(s);
	}
	
	public boolean solid() {
		return true;
	}
	
	public boolean breakable() {
		return true;
	}

}
