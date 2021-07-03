package com.clay13chopper.game1.graphics;

import com.clay13chopper.game1.processors.PathFinder;
import com.clay13chopper.game1.processors.PathFinder.PathType;

public class PathDisplay {
	
	public PathDisplay() {
		
	}
	
	public void render() {
		
	}
	
	public static Sprite chooseSprite(int hoveredTile, int x, int y, int aheadTile, int roomWidth, PathFinder pathFinder) {
		int behindTile = pathFinder.prev(x, y);
		if (behindTile == -1 && aheadTile == -1) return Sprite.pathBlueStart;
		if (behindTile == -1 && pathFinder.getType(x, y) == PathType.HOME) {
			int ax = (aheadTile % roomWidth);
			if (ax > x) return Sprite.pathBlueStartRight;
			if (ax < x) return Sprite.pathBlueStartLeft;
			int ay = (aheadTile / roomWidth);
			if (ay > y) return Sprite.pathBlueStartDown;
			if (ay < y) return Sprite.pathBlueStartUp;
		}
		if (aheadTile == -1) {
			int bx = (behindTile % roomWidth);
			if (bx > x) return Sprite.pathBlueEndLeft;
			if (bx < x) return Sprite.pathBlueEndRight;
			int by = (behindTile / roomWidth);
			if (by > y) return Sprite.pathBlueEndUp;
			if (by < y) return Sprite.pathBlueEndDown;
		}

		int ax = (aheadTile % roomWidth);
		int ay = (aheadTile / roomWidth);
		int bx = (behindTile % roomWidth);
		int by = (behindTile / roomWidth);

		if (ax != x && bx != x) return Sprite.pathBlueHorizontal;
		if (ay != y && by != y) return Sprite.pathBlueVertical;
		if ((ax > x && by > y) || (ay > y && bx > x)) return Sprite.pathBlueCornerDownRight;
		if ((ax < x && by > y) || (ay > y && bx < x)) return Sprite.pathBlueCornerDownLeft;
		if ((ax > x && by < y) || (ay < y && bx > x)) return Sprite.pathBlueCornerUpRight;
		if ((ax < x && by < y) || (ay < y && bx < x)) return Sprite.pathBlueCornerUpLeft;
		
		
		return Sprite.voidSprite;
	}

}
