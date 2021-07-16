package com.clay13chopper.game1.graphics;

import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.processors.PathFinder;
import com.clay13chopper.game1.processors.PathFinder.PathType;

public class PathDisplay {
	
	protected int hoveredTile;
	
	public PathDisplay() {
		hoveredTile = -1;
	}
	
	/**
	 * Displays the player's path while selecting an action
	 * 
	 * @param roomWidth		width of the room
	 * @param tileSize		size of tiles in the level
	 * @param pathFinder	a link to the room's path finder
	 * @param screen		a link to the room's screen
	 */
	public void render(int roomWidth, int tileSize, PathFinder pathFinder, Screen screen) {
		int currTile = hoveredTile;
		int aheadTile = -1;
		while (currTile != -1) {
			int hTx = (currTile % roomWidth);
			int hTy = (currTile / roomWidth);
			screen.renderSprite(hTx * tileSize, hTy * tileSize, 
					chooseSprite(currTile, hTx, hTy, aheadTile, roomWidth, pathFinder), false, false);
			aheadTile = currTile;
			currTile = pathFinder.prev(hTx, hTy);
		}
	}
	
	/**
	 * Selects the sprite for each path spot
	 * 
	 * @param hoveredTile	tile being analyzed for a sprite
	 * @param xGrid				x position
	 * @param yGrid				y position
	 * @param aheadTile		tile ahead of this one (also, last tile analyzed)
	 * @param roomWidth		width of the room
	 * @param pathFinder	a link to the room's path finder
	 * @return				the sprite chosen to use
	 */
	public Sprite chooseSprite(int hoveredTile, int xGrid, int yGrid, int aheadTile, int roomWidth, PathFinder pathFinder) {
		int behindTile = pathFinder.prev(xGrid, yGrid);
		if (behindTile == -1 && aheadTile == -1) return Sprite.pathBlueStart;
		if (behindTile == -1 && pathFinder.getType(xGrid, yGrid) == PathType.HOME) {
			int ax = (aheadTile % roomWidth);
			if (ax > xGrid) return Sprite.pathBlueStartRight;
			if (ax < xGrid) return Sprite.pathBlueStartLeft;
			int ay = (aheadTile / roomWidth);
			if (ay > yGrid) return Sprite.pathBlueStartDown;
			if (ay < yGrid) return Sprite.pathBlueStartUp;
		}
		if (aheadTile == -1) {
			int bx = (behindTile % roomWidth);
			if (bx > xGrid) return Sprite.pathBlueEndLeft;
			if (bx < xGrid) return Sprite.pathBlueEndRight;
			int by = (behindTile / roomWidth);
			if (by > yGrid) return Sprite.pathBlueEndUp;
			if (by < yGrid) return Sprite.pathBlueEndDown;
		}

		int ax = (aheadTile % roomWidth);
		int ay = (aheadTile / roomWidth);
		int bx = (behindTile % roomWidth);
		int by = (behindTile / roomWidth);

		if (ax != xGrid && bx != xGrid) return Sprite.pathBlueHorizontal;
		if (ay != yGrid && by != yGrid) return Sprite.pathBlueVertical;
		if ((ax > xGrid && by > yGrid) || (ay > yGrid && bx > xGrid)) return Sprite.pathBlueCornerDownRight;
		if ((ax < xGrid && by > yGrid) || (ay > yGrid && bx < xGrid)) return Sprite.pathBlueCornerDownLeft;
		if ((ax > xGrid && by < yGrid) || (ay < yGrid && bx > xGrid)) return Sprite.pathBlueCornerUpRight;
		if ((ax < xGrid && by < yGrid) || (ay < yGrid && bx < xGrid)) return Sprite.pathBlueCornerUpLeft;
		
		
		return Sprite.voidSprite;
	}
	
	public void setHoveredTile(int addr) {
		hoveredTile = addr;
	}
	
	public int  getHoveredTile() {
		return hoveredTile;
	}
	
	public void confirmAttackDistance(int xGrid, int yGrid, int roomWidth, Unit unit, PathFinder pathFinder) {
		int pastHoveredTile = hoveredTile;
		if (pastHoveredTile == -1) {

			int newHoveredTile = pathFinder.prev(xGrid, yGrid);
			setHoveredTile(newHoveredTile);
		}
		else {
			int distance = Math.abs((pastHoveredTile % roomWidth) - xGrid) 
							+ Math.abs((pastHoveredTile / roomWidth) - yGrid);
			if (distance < unit.getMinRange() || distance > unit.getMaxRange()) {
				int newHoveredTile = pathFinder.prev(xGrid, yGrid);
				setHoveredTile(newHoveredTile);
			}
		}
	}
	
	public void reset() {
		hoveredTile = -1;
	}

}
