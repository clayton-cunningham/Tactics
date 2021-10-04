package com.clay13chopper.game1.tiles;

import com.clay13chopper.game1.graphics.Sprite;

/**
 * Sand:	Slow movement
 * 		TODO: This may include other values in the future
 * @author Clayton Cunningham
 *
 */
public class SandTile extends Tile{

	public SandTile(Sprite s) {
		super(s);
	}

	public int moveCostFoot()  {	return 2; }

}
