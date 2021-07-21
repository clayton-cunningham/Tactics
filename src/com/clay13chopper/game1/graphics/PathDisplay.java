package com.clay13chopper.game1.graphics;

import java.util.Stack;

import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.processors.PathFinder;

public class PathDisplay {

	protected Stack<Integer> path;
	protected int roomWidth;
	
	public PathDisplay(int width) {
		path = new Stack<Integer>();
		roomWidth = width;
	}
	
	/**
	 * Displays the player's path while selecting an action
	 * 
	 * @param roomWidth		width of the room
	 * @param tileSize		size of tiles in the level
	 * @param screen		a link to the room's screen
	 */
	public void render(int tileSize, Screen screen) {
		if (path.isEmpty()) return;
		int currTile = -1;
		int aheadTile = -1;
		int behindTile = -1;
		int xGrid = -1, yGrid = -1;
		
		for (int i = path.size() - 1; i >= 0; i--) {
			currTile = path.get(i);
			xGrid = (currTile % roomWidth);
			yGrid = (currTile / roomWidth);
			if (i > 0) behindTile = path.get(i - 1);
			else behindTile = -1;
			screen.renderSprite(xGrid * tileSize, yGrid * tileSize, 
					chooseSprite(currTile, xGrid, yGrid, aheadTile, behindTile), false, false);
			aheadTile = currTile;
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
	 * @return				the sprite chosen to use
	 */
	public Sprite chooseSprite(int hoveredTile, int xGrid, int yGrid, int aheadTile, int behindTile) {
//		int behindTile = pathFinder.prev(xGrid, yGrid);
		if (behindTile == -1 && aheadTile == -1) return Sprite.pathBlueStart;
		if (behindTile == -1) {
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
	
	// Add the hovered tile to the path.  If the path is empty or invalid, set a new path
	public void setHoveredTile(int xGrid, int yGrid, Unit unit, PathFinder pathFinder) {
		
		int addr = xGrid + (yGrid * roomWidth);
		if (path.isEmpty()) {
			recordNewPath(xGrid, yGrid, pathFinder);
			return;
		}
		
		// Check if address is already part of the path
		int pastTile = path.peek();
		if (pastTile == addr) return;
		if (path.contains(addr)) {
			while (path.peek() != addr) {
				path.pop();
			}
			return;
		}
		
		// If not adjacent to last spot or too queue is too long, reset the path
		// Size technically is 1 larger than movement required since it includes the "HOME" space,
		//   but that's what we want;  we know we're adding a new adjacent space here,
		//   and this check is done before the addition instead of after
		int pXGrid = pastTile % roomWidth;
		int pYGrid = pastTile / roomWidth;
		if ((Math.abs(pXGrid - xGrid) != 1 || pYGrid != yGrid) && (Math.abs(pYGrid - yGrid) != 1 || pXGrid != xGrid)
		     || unit.getMovement() < path.size()) {
			path.clear();
			recordNewPath(xGrid, yGrid, pathFinder);
		}
		else { 
			// Case: nothing special, simply moving to the new adjacent legal move spot
			// Add new address
			path.push(addr);
		}
		
		
	}
	
	// Builds a new path whenever we need to reset the queue.
	// Since this is recursive, we cannot clear the queue in this method.
	protected void recordNewPath(int xGrid, int yGrid, PathFinder pathFinder) {
		
		int pastTile = pathFinder.prev(xGrid, yGrid);
		if (pastTile == -1) {
			path.push(xGrid + (yGrid * roomWidth));
			return;
		}
		
		int pXGrid = pastTile % roomWidth;
		int pYGrid = pastTile / roomWidth;
		recordNewPath(pXGrid, pYGrid, pathFinder);
		path.push(xGrid + (yGrid * roomWidth));
	}
	
	// Retrieves the move space the player hovered over
	// This is used when selecting an attack space, in contrast to using the default from PathFinder
	public int getHoveredTile() {
		return path.peek();
	}
	
	// Check the recorded path for an attack space.  
	// If the unit can't attack from there, set a new path using the default closest tile.
	public void confirmAttackDistance(int xGrid, int yGrid, Unit unit, PathFinder pathFinder) {
		if (path.isEmpty()) {

			int newHoveredTile = pathFinder.prev(xGrid, yGrid);
			setHoveredTile(newHoveredTile % roomWidth, newHoveredTile / roomWidth, unit, pathFinder);
		}
		else {
			int pastHoveredTile = path.peek();
			int distance = Math.abs((pastHoveredTile % roomWidth) - xGrid) 
							+ Math.abs((pastHoveredTile / roomWidth) - yGrid);
			if (distance < unit.getMinRange() || distance > unit.getMaxRange()) {
				int newHoveredTile = pathFinder.prev(xGrid, yGrid);
				setHoveredTile(newHoveredTile % roomWidth, newHoveredTile / roomWidth, unit, pathFinder);
			}
		}
	}
	
	public void reset() {
		path.clear();
	}

}
