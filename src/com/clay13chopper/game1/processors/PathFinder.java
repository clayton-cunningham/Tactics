package com.clay13chopper.game1.processors;

import java.util.Arrays;
import java.util.HashMap;

import com.clay13chopper.game1.entities.mob.Unit.Team;
import com.clay13chopper.game1.room.level.Level;

public class PathFinder {

	protected Level level;
	protected int width;
	protected int height;
	protected PathType[] activeMove; 						// Holds type of action available for space
	protected HashMap<Integer, PathType> customMoveDisplay;	// Holds custom type of action available for space to override ^
	protected int[] prevTile;		 						// Holds previous tile for space
	protected int[] distances;		 						// Holds move amount left at space
	protected boolean homeSet;      						// Holds starting tile
	protected QueueMap<int[]> map;   						// Map of Queues - holds possible yet-to-be calculated movement

	// TODO: Should I use these Blocked variables, or just make a switch for when we're using calcDesiredPath so there's just one method?
	protected boolean attackSet;			// True if a unit was found to attack
	protected int attackAddr;  				// Set if a unit was found to attack
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
	
	// Calculates the paths available
	// Calls calcMove repeatedly until QueueMap is empty
	public void calcPath(int move, int minRange, int maxRange, int x, int y, Team team) {
		
		reset();
		
		calcMove(move, minRange, maxRange, x, y, -1, 0, team);
		
		while (true) {
			int[] next = map.next();
			if (next == null) {
				break;
			}
			calcMove(next[0], minRange, maxRange, next[1], next[2], next[3], next[4], team);
		}
		
	}
	
	public int[] calcDesiredPath(int move, int minRange, int maxRange, int x, int y, Team team) {
		
		reset();
		
		int moveLimit = width * height * 5;
		
		calcMove(moveLimit, minRange, maxRange, x, y, -1, 0, team);
		
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
			return new int[] {-1, x + (y * width)}; // If unit cannot find enemies - should only happen for ranged units blocked by enemies
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
	
	// Labels all movement spaces with PathType.Move;
	//		This label is used to show area to move to, and
	//		receive input to move the unit.
	// Also labels those spaces with amount of movement left
	// Every iteration is for a spot to move to
	private void calcMove(int move, int minRange, int maxRange, int x, int y, int px, int py, Team team) {

		if (x < 0 || y < 0 || x >= width || y >= height) return; // Check bounds of level
		if (homeSet) move -= level.getTile(x, y).moveCostFoot(); // Decrement movement based on tile
		if (move <= -1) return;                          		 //   ... then check if too far
		if (distances[x + (y * width)] >= move) return; 		 // Check if shorter path already found
		if (level.getUnit(x, y) != null && level.getUnit(x, y).getTeam() != team) return;  //Cannot move onto a non-playable unit
		if (level.getTile(x, y).solid() && !openWalls) return;
		
		distances[x + (y * width)] = move;              // Mark path distance
		
		if (!homeSet) {  // First space - can attack from here, but this unit is here too
			activeMove[x + (y * width)] = PathType.HOME;
			calcAttack(minRange, maxRange, x, y, 0, team);
			homeSet = true;
		}  			// Cannot attack from another unit's space, so keep moving
		else if (level.getUnit(x, y) != null && level.getUnit(x, y).getTeam() == team) { 
			activeMove[x + (y * width)] = PathType.PASS;
			calcAttack(minRange, maxRange, x, y, 1, team);
			
		}
		else {		// Mark move, calculate attack
			activeMove[x + (y * width)] = PathType.MOVE;
			calcAttack(minRange, maxRange, x, y, 0, team);
		}
		
		prevTile[x + ((y) * width)] = (px + (py * width));

		// Queue all possible moves (breadth-first)
		// Better than recursion (depth-first)
		int[] down = 	{move, x, y + 1, x, y};
		int[] up = 		{move, x, y - 1, x, y};
		int[] right = 	{move, x + 1, y, x, y};
		int[] left = 	{move, x - 1, y, x, y};
		map.add(move - 1, down);
		map.add(move - 1, up);
		map.add(move - 1, right);
		map.add(move - 1, left);
		
	}
	
	// Labels nearby titles that can be attacked
	// Type 0: Regular pathfinding
	// Type 1: Blocked pathfinding
	// Type 2: Single space search
	public void calcAttack(int minRange, int maxRange, int x, int y, int type, Team team) {
		
		for (int range = minRange; range <= maxRange; range++) {

			if (type == 1 && attackBlockedSet) return;
			
			int xDir = -1;
			int yDir = 1;
			
			// xi and yi cycle through all relative locations for attack range - i.e. 2,1; 1,2; 0,3; -1,2; etc.
			for (int xi = range - 1, yi = 1; Math.abs(xi) <= range; xi += xDir, yi += yDir) {
				if (xi == -1 * range) xDir *= -1;
				if (xi == 0) yDir *= -1;
				
				// Tile locations we're checking
				int xTile = x + xi;
				int yTile = y + yi;
				if (xTile < 0 || yTile < 0 || xTile >= width || yTile >= height) continue;
				
				switch (type) {
				case 0: checkAttackSpace(xTile, yTile, x, y, team); break;
				case 1: checkBlockedAttackSpace(xTile, yTile, x, y, team); break;
				case 2: checkAttackforOneSpace(xTile, yTile, x, y, team); break;
				}
				
			}
			
		}
		
	}
	
	private void checkAttackSpace(int xTile, int yTile, int x, int y, Team team) {
		int index = xTile + (yTile * width);
		if (activeMove[index] == PathType.NONE) {
			activeMove[index] = PathType.ATTACK;
			prevTile[index] = (x + (y * width));
			if (level.getUnit(xTile, yTile) != null && level.getUnit(xTile, yTile).getTeam() != team) {
				attackSet = true;
				attackAddr = index;
			}
		}
	}
	
	private void checkBlockedAttackSpace(int xTile, int yTile, int x, int y, Team team) {
		int index = xTile + (yTile * width);
		if (activeMove[index] == PathType.NONE) {
			prevTile[index] = (x + (y * width));
			if (level.getUnit(xTile, yTile) != null && level.getUnit(xTile, yTile).getTeam() != team) {
				attackBlockedSet = true;
				attackBlockedAddr = index;
			}
		}
	}
	
	private void checkAttackforOneSpace(int xTile, int yTile, int x, int y, Team team) {
		int index = xTile + (yTile * width);
		if (activeMove[index] == PathType.ATTACK && 
				level.getUnit(xTile, yTile) != null && level.getUnit(xTile, yTile).getTeam() != team) {
			customMoveDisplay.put(index, PathType.ATTACK);
		}
	}
	
	public void openWallsEdit() {
		openWalls = true;
	}
	
	public void closeWallsEdit() {
		openWalls = false;
	}
	
	public void enemyShown() {
		enemyShown = true;
	}
	
	public boolean getEnemyShown() {
		return enemyShown;
	}
	
	public void enemyNotShown() {
		enemyShown = false;
	}
	
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
	
	public PathType getCustomMove(int x, int y) {
		return customMoveDisplay.get(x + (y * width));
	}
	
	public boolean customMoveIsEmpty() {
		return customMoveDisplay.isEmpty();
	}
	
	public void clearCustomMove() {
		customMoveDisplay.clear();
	}
	
	public PathType getType(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) return PathType.NONE;
		int index = x + (y * width);
		if (!customMoveDisplay.isEmpty()) {
			return customMoveDisplay.get(index);
		}
		return activeMove[index];
	}
	
	public int prev(int x, int y) {
		return prevTile[x + (y * width)];
	}

	public enum PathType {
		NONE,
		MOVE,
		ATTACK,
		PASS,
		HOME 		// Same as MOVE, but doesn't render blue on tiles
	}
}
