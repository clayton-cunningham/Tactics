package com.clay13chopper.game1.graphics;

import com.clay13chopper.game1.processors.PathFinder;
import com.clay13chopper.game1.processors.PathFinder.PathType;

public class PathDisplay {
	
	public PathDisplay() {
		
	}
	
	/**
	 * Displays the player's path while selecting an action
	 * 
	 * @param roomWidth		width of the room
	 * @param tileSize		size of tiles in the level
	 * @param pathFinder	a link to the room's path finder
	 * @param screen		a link to the room's screen
	 */
	public static void render(int roomWidth, int tileSize, PathFinder pathFinder, Screen screen) {
		int hoveredTile = pathFinder.getHoveredTile();
		int aheadTile = -1;
		while (hoveredTile != -1) {
			int hTx = (hoveredTile % roomWidth);
			int hTy = (hoveredTile / roomWidth);
			screen.renderSprite(hTx * tileSize, hTy * tileSize, 
					chooseSprite(hoveredTile, hTx, hTy, aheadTile, roomWidth, pathFinder), false, false);
			aheadTile = hoveredTile;
			hoveredTile = pathFinder.prev(hTx, hTy);
		}
	}
	
	/**
	 * Selects the sprite for each path spot
	 * 
	 * @param hoveredTile	tile being analyzed for a sprite
	 * @param x				x position
	 * @param y				y position
	 * @param aheadTile		tile ahead of this one (also, last tile analyzed)
	 * @param roomWidth		width of the room
	 * @param pathFinder	a link to the room's path finder
	 * @return				the sprite chosen to use
	 */
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
