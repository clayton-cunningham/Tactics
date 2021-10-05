package com.clay13chopper.game1.processors;

import java.util.Arrays;
import java.util.HashMap;

import com.clay13chopper.game1.entities.mob.Unit.Team;
import com.clay13chopper.game1.room.level.Level;

/**
 * This class calculates the most efficient paths available
 * 		using BFS with Dijkstra's algorithm
 * @author Clayton Cunningham
 *
 */
public class PathFinder {

	protected Level level;
	protected int width;
	protected int height;
	protected PathType[] activeMove; 						// Holds type of action available for each space
	protected HashMap<Integer, PathType> customMoveDisplay;	// Holds actions available from user's chosen space (overrides ^)
	protected int[] prevTile;		 						// Holds previous tile for spaces
	protected int[] distances;		 						// Holds move amount left at spaces
	protected boolean homeSet;      						// Holds starting tile
	protected QueueMap<int[]> map;   						// Map of Queues - holds possible yet-to-be calculated movement

	// TODO: Should I use these Blocked variables, or just make a switch for when we're using calcDesiredPath so there's just one method?
	protected boolean attackSet;			// True if a unit was found to attack
	protected int attackAddr;  				// Set	if a unit was found to attack
	protected boolean attackBlockedSet;		// True if a unit was found to attack, but is blocked
	protected int attackBlockedAddr;		// Set  if a unit was found to attack, but is blocked

	protected boolean openWalls = false;	// True if currently editing walls
	protected boolean enemyShown = false;	// True if path is for non-active team

	
	public PathFinder(int width, int height, Level l) {
		this.width = width;
		this.height = height;
		level = l;
		activeMove = new PathType[width * height];
		Arrays.fill(activeMove, PathType.NONE);
		customMoveDisplay = new HashMap<Integer, PathType>();
		prevTile = new int[width * height];
		distances = new int[width * height];
		Arrays.fill(distances, -10);
		homeSet = false;
		attackSet = false;
		attackAddr = -1;
		attackBlockedSet = false;
		attackBlockedAddr = -1;
		map = new QueueMap<int[]>();
	}
	
	/**
	 * Calculates the paths available
	 * Calls calcMove repeatedly until QueueMap is empty
	 * @param move		movement allowed from space
	 * @param minRange	minimum attack range
	 * @param maxRange	maximum attack range
	 * @param xGrid		x position to be calculated from (in grid format)
	 * @param yGrid		y position to be calculated from (in grid format)
	 * @param team		team the caller is on
	 */
	public void calcPath(int move, int minRange, int maxRange, int xGrid, int yGrid, Team team) {
		
		reset();
		
		calcMove(move, minRange, maxRange, xGrid, yGrid, -1, 0, team);
		
		while (true) {
			int[] next = map.next();
			if (next == null) {
				break;
			}
			calcMove(next[0], minRange, maxRange, next[1], next[2], next[3], next[4], team);
		}
	}
	
	/**
	 * Calculates the a target space to move to or attack
	 * Does not hold all possibilities afterwards
	 * @param move		movement allowed from space
	 * @param minRange	minimum attack range
	 * @param maxRange	maximum attack range
	 * @param xGrid		x position to be calculated from (in grid format)
	 * @param yGrid		y position to be calculated from (in grid format)
	 * @param team		team the caller is on
	 * @return		an array holding the desired space to move to or attack
	 */
	public int[] calcDesiredPath(int move, int minRange, int maxRange, int xGrid, int yGrid, Team team) {
		
		reset();
		
		int moveLimit = width * height * 5;
		
		calcMove(moveLimit, minRange, maxRange, xGrid, yGrid, -1, 0, team);
		
		while (!attackSet) {
			int[] next = map.next();
			if (next == null) {
				break;
			}
			calcMove(next[0], minRange, maxRange, next[1], next[2], next[3], next[4], team);
		}
		
		if (!attackSet) attackAddr = attackBlockedAddr;
		
		if (attackAddr == -1) {
			reset();
			return new int[] {-1, xGrid + (yGrid * width)}; // If unit cannot find enemies - should only happen for ranged units blocked by enemies
		}
		
		int target = attackAddr;
		int reqMove = moveLimit - move;
		int curTile = prevTile[target];
		
		while (distances[curTile] < reqMove || (activeMove[curTile] != PathType.MOVE && activeMove[curTile] != PathType.HOME)) {
			target = -1;
			curTile = prevTile[curTile];
		}
		
		reset();
		
		return new int[] {target, curTile};
		
	}
	
	/**
	 * Labels all movement spaces with PathType.Move;
	 *		This label is used to show area available to move to
	 * Also labels spaces with how much movement is left
	 * Every iteration is for a spot to move to
	 * @param move		movement allowed from space (decreases during recursion)
	 * @param minRange	minimum attack range
	 * @param maxRange	maximum attack range
	 * @param xGrid		x position to be calculated from (in grid format)
	 * @param yGrid		y position to be calculated from (in grid format)
	 * @param px		past recursion x position (in grid format)
	 * @param py		past recursion y position (in grid format)
	 * @param team		team the caller is on
	 */
	private void calcMove(int move, int minRange, int maxRange, int xGrid, int yGrid, int px, int py, Team team) {

		if (xGrid < 0 || yGrid < 0 || xGrid >= width || yGrid >= height) return; // Check bounds of level
		if (homeSet) move -= level.getTile(xGrid, yGrid).moveCostFoot(); // Decrement movement based on tile
		if (move <= -1) return;                          		 		 //   ... then check any movement is left
		if (distances[xGrid + (yGrid * width)] >= move) return; 		 // Check if shorter path already found
		if (level.getUnit(xGrid, yGrid) != null && level.getUnit(xGrid, yGrid).getTeam() != team) return;  //Cannot move onto a non-playable unit
		if (level.getTile(xGrid, yGrid).solid() && !openWalls) return;
		
		distances[xGrid + (yGrid * width)] = move;              // Mark path distance
		
		if (!homeSet) {  // First space - can attack from here, but this unit is here too
			activeMove[xGrid + (yGrid * width)] = PathType.HOME;
			calcAttack(minRange, maxRange, xGrid, yGrid, 0, team);
			homeSet = true;
		}  			// Cannot attack from another unit's space, so keep moving
		else if (level.getUnit(xGrid, yGrid) != null && level.getUnit(xGrid, yGrid).getTeam() == team) { 
			activeMove[xGrid + (yGrid * width)] = PathType.PASS;
			calcAttack(minRange, maxRange, xGrid, yGrid, 1, team);
			
		}
		else {		// Mark move, calculate attack
			activeMove[xGrid + (yGrid * width)] = PathType.MOVE;
			calcAttack(minRange, maxRange, xGrid, yGrid, 0, team);
		}
		
		prevTile[xGrid + ((yGrid) * width)] = (px + (py * width));

		// Queue all possible moves (breadth-first)
		// Better than recursion (depth-first)
		int[] down = 	{move, xGrid, yGrid + 1, xGrid, yGrid};
		int[] up = 		{move, xGrid, yGrid - 1, xGrid, yGrid};
		int[] right = 	{move, xGrid + 1, yGrid, xGrid, yGrid};
		int[] left = 	{move, xGrid - 1, yGrid, xGrid, yGrid};
		map.add(move - 1, down);
		map.add(move - 1, up);
		map.add(move - 1, right);
		map.add(move - 1, left);
		
	}
	
	/**
	 * Labels nearby titles that can be attacked
	 * Type 0: Regular path finding
	 * Type 1: Blocked path finding
	 * Type 2: Single space search
	 * @param minRange	minimum attack range
	 * @param maxRange	maximum attack range
	 * @param xGrid		x position to be calculated from (in grid format)
	 * @param yGrid		y position to be calculated from (in grid format)
	 * @param type		type of search (list is in this comment, above)
	 * @param team		team the caller is on
	 */
	public void calcAttack(int minRange, int maxRange, int xGrid, int yGrid, int type, Team team) {
		
		for (int range = minRange; range <= maxRange; range++) {

			if (type == 1 && attackBlockedSet) return;
			
			int xDir = -1;
			int yDir = 1;
			
			// xi and yi cycle through all relative locations for attack range - 
					// i.e. for range = 3 :	 2,1; 1,2; 0,3; -1,2; etc.
			for (int xi = range - 1, yi = 1; Math.abs(xi) <= range; xi += xDir, yi += yDir) {
				if (xi == -1 * range) xDir *= -1;
				if (xi == 0) yDir *= -1;
				
				// Current tile locations we're checking
				int xCurr = xGrid + xi;
				int yCurr = yGrid + yi;
				if (xCurr < 0 || yCurr < 0 || xCurr >= width || yCurr >= height) continue;
				
				switch (type) {
				case 0: checkAttackSpace(xCurr, yCurr, xGrid, yGrid, team); break;
				case 1: checkBlockedAttackSpace(xCurr, yCurr, xGrid, yGrid, team); break;
				case 2: checkAttackforOneSpace(xCurr, yCurr, xGrid, yGrid, team); break;
				}
				
			}
			
		}
		
	}
	
	/**
	 * Mark any space within range of the input space as an attack space (if not already something else)
	 * 		Then signal that a space to attack has been found (only relevant for the desired path)
	 * @param xCurr		x address we're checking (in grid format)
	 * @param yCurr		y address we're checking (in grid format)
	 * @param xGrid		x position to be calculated from (in grid format)
	 * @param yGrid		y position to be calculated from (in grid format)
	 * @param team		team the caller is on
	 */
	private void checkAttackSpace(int xCurr, int yCurr, int xGrid, int yGrid, Team team) {
		int index = xCurr + (yCurr * width);
		if (activeMove[index] == PathType.NONE) {
			activeMove[index] = PathType.ATTACK;
			prevTile[index] = (xGrid + (yGrid * width));
			if (level.getUnit(xCurr, yCurr) != null && level.getUnit(xCurr, yCurr).getTeam() != team) {
				attackSet = true;
				attackAddr = index;
			}
		}
	}

	/**
	 * Mark any space within range of the input space as an blocked attack space (if not already something else)
	 * 		Then signal that a blocked space to attack has been found (only relevant for the desired path)
	 * 		and only used if no non-blocked attack space was found
	 * @param xCurr		x address we're checking (in grid format)
	 * @param yCurr		y address we're checking (in grid format)
	 * @param xGrid		x position to be calculated from (in grid format)
	 * @param yGrid		y position to be calculated from (in grid format)
	 * @param team		team the caller is on
	 */
	private void checkBlockedAttackSpace(int xCurr, int yCurr, int xGrid, int yGrid, Team team) {
		int index = xCurr + (yCurr * width);
		if (activeMove[index] == PathType.NONE) {
			prevTile[index] = (xGrid + (yGrid * width));
			if (level.getUnit(xCurr, yCurr) != null && level.getUnit(xCurr, yCurr).getTeam() != team) {
				attackBlockedSet = true;
				attackBlockedAddr = index;
			}
		}
	}

	/**
	 * Record any space within range of the input space that is already marked as an attack space
	 * 		This will override the PathType array without deleting it
	 * @param xCurr		x address we're checking (in grid format)
	 * @param yCurr		y address we're checking (in grid format)
	 * @param xGrid		x position to be calculated from (in grid format)
	 * @param yGrid		y position to be calculated from (in grid format)
	 * @param team		team the caller is on
	 */
	private void checkAttackforOneSpace(int xCurr, int yCurr, int xGrid, int yGrid, Team team) {
		int index = xCurr + (yCurr * width);
		if (activeMove[index] == PathType.ATTACK && 
				level.getUnit(xCurr, yCurr) != null && level.getUnit(xCurr, yCurr).getTeam() != team) {
			customMoveDisplay.put(index, PathType.ATTACK);
		}
	}
	
	/**
	 * Allows path finding through walls
	 * 		Used during level generation to guarantee all units can reach each other
	 */
	public void openWallsEdit() 	{	openWalls = true;	}
	public void closeWallsEdit() 	{	openWalls = false;	}
	
	/**
	 * This setting allows a distinction between user and enemy path displays
	 * 		In rendering, this is checked to determine if different path colors should be used
	 */
	public void enemyShown() 		{	enemyShown = true;	}
	public void enemyNotShown() 	{	enemyShown = false;	}
	public boolean getEnemyShown() 	{	return enemyShown;	}
	
	/**
	 * Clear all structures and variables
	 */
	public void reset() {
		Arrays.fill(activeMove, PathType.NONE);
		customMoveDisplay.clear();
		Arrays.fill(prevTile, -1);
		Arrays.fill(distances, -10);
		homeSet = false;
		attackSet = false;
		attackAddr = -1;
		attackBlockedSet = false;
		attackBlockedAddr = -1;
		map.clear();
	}
	
	/**
	 * The customMove structure records attack options from the user's chosen space
	 * 		This allows the program to override the stored PathTypes without deleting them
	 */
	public PathType getCustomMove(int x, int y) {	return customMoveDisplay.get(x + (y * width));	}
	public boolean customMoveIsEmpty() 			{	return customMoveDisplay.isEmpty();				}
	public void clearCustomMove() 				{	customMoveDisplay.clear();						}
	
	/**
	 * Retrieve the PathType for the given address
	 * @param xGrid		x address in grid format
	 * @param yGrid		y address in grid format
	 * @return			PathType of given address
	 */
	public PathType getType(int xGrid, int yGrid) {
		if (xGrid < 0 || yGrid < 0 || xGrid >= width || yGrid >= height) return PathType.NONE;
		int index = xGrid + (yGrid * width);
		if (!customMoveDisplay.isEmpty()) {
			return customMoveDisplay.get(index);
		}
		return activeMove[index];
	}
	
	/**
	 * Retrieve the address of the tile preceding the given address
	 * @param x		x address in grid format
	 * @param y		y address in grid format
	 * @return		integer address of the preceding tile
	 */
	public int prev(int x, int y) {
		return prevTile[x + (y * width)];
	}

	/**
	 * PathType enumeration - allows for clear marking of what action is available on each space
	 */
	public enum PathType {
		NONE,
		MOVE,
		ATTACK,
		PASS,
		HOME 		// Same as MOVE, but doesn't render blue on tiles
	}
}
