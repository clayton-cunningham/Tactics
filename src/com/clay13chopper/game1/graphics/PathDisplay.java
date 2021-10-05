package com.clay13chopper.game1.graphics;

import java.util.Stack;

import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.processors.PathFinder;

/**
 * This class records and displays the path of the currently selected unit
 * @author Clayton Cunningham
 *
 */
public class PathDisplay {
	
	protected Stack<Integer> path;		// List of path spaces in order 0 --> n :: start --> target
	protected int roomWidth;
	
	public PathDisplay(int width) {
		path = new Stack<Integer>();
		roomWidth = width;
	}
	
	/**
	 * Displays a unit's path while the player is selecting an action
	 * 
	 * @param tileSize		size of tiles in the level
	 * @param screen		a link to the room's screen
	 */
	public void render(int tileSize, Screen screen) {
		if (path.isEmpty()) return;
		int currTile = -1;
		int aheadTile = -1;
		int behindTile = -1;
		int xGrid = -1, yGrid = -1;
		
		// Go through path stack in opposite order (n --> 0)
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
	 * @param behindTile	tile behind of this one (also, next tile to be analyzed)
	 * @return				the sprite chosen to use
	 */
	public Sprite chooseSprite(int hoveredTile, int xGrid, int yGrid, int aheadTile, int behindTile) {
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
	
	/**
	 * Add the hovered tile to the path.  If the path is empty or invalid, set a new path
	 * @param xGrid		x location in grid format
	 * @param yGrid		y location in grid format
	 * @param unit		Unit that has been chosen
	 * @param pathFinder	the level's pathFinder 
	 */
	public void setHoveredTile(int xGrid, int yGrid, Unit unit, PathFinder pathFinder) {
		
		int addr = xGrid + (yGrid * roomWidth);
		
		// If empty, start a new stack
		if (path.isEmpty()) {
			recordNewPath(xGrid, yGrid, pathFinder);
			return;
		}
		
		// Check if address is already part of the path.  If so, remove anything after it in the stack
		int pastTile = path.peek();
		if (pastTile == addr) return;
		if (path.contains(addr)) {
			while (path.peek() != addr) {
				path.pop();
			}
			return;
		}
		
		// If not adjacent to last spot or stack is too big, reset the path
		// Since the path stack holds the HOME space, the max size is unit's movement + 1
		// TODO: allow diagonal movement - requires building the path to get there
		int pXGrid = pastTile % roomWidth;
		int pYGrid = pastTile / roomWidth;
		if ((Math.abs(pXGrid - xGrid) != 1 || pYGrid != yGrid) && (Math.abs(pYGrid - yGrid) != 1 || pXGrid != xGrid)
		     || unit.getMovement() < path.size()) {
			path.clear();
			recordNewPath(xGrid, yGrid, pathFinder);
		}
		// Else, add new space to the path
		else { 
			path.push(addr);
		}
		
		
	}
	
	/**
	 * Builds a new path from the address.
	 * Note: Since this is recursive, do NOT clear the queue in this method.
	 * @param xGrid			x address in grid format
	 * @param yGrid			y address in grid format
	 * @param pathFinder	level's pathFinder
	 */
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
	
	/**
	 * Retrieves the move space the player hovered over
	 * This is used when selecting an attack space, in contrast to using the default from PathFinder
	 * @return	top of path stack / end of the path
	 */
	public int getHoveredTile() {
		return path.peek();
	}
	
	/**
	 * Check the top of the stack to see if it can attack the address.  
	 * If the unit can't attack from there, set a new path using the default closest tile.
	 * @param xGrid			x address of target in grid format
	 * @param yGrid			y address of target in grid format
	 * @param unit			Unit we selected
	 * @param pathFinder	level's pathFinder
	 */
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
	
	/**
	 * Clear the stack
	 */
	public void reset() {
		path.clear();
	}

}
