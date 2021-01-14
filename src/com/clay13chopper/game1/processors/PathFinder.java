package com.clay13chopper.game1.processors;

import java.util.Arrays;

import com.clay13chopper.game1.room.level.Level;

public class PathFinder {

	protected Level level;
	protected int width;
	protected int height;
	protected PathType[] activeMove; // Holds type of action available for space
	protected int[] prevTile;		 // Holds previous tile for space
	protected int[] distances;		 // Holds move amount left at space
	protected boolean homeSet;       // Holds starting tile
	protected QueueMap<int[]> map;   // Map of Queues - holds possible yet-to-be calculated movement
	
	protected boolean attackSet;
	protected int attackAddr;  	// TODO: Should I toss these variables in, or just make a switch for when we're using calcDesiredPath?
	
	
	public PathFinder(int width, int height, Level l) {
		this.width = width;
		this.height = height;
		level = l;
		activeMove = new PathType[width * height];
		Arrays.fill(activeMove, PathType.NONE);
		prevTile = new int[width * height];
		distances = new int[width * height];
		Arrays.fill(distances, -10);
		homeSet = false;
		attackSet = false;
		attackAddr = -1;
		map = new QueueMap<int[]>();
	}
	
	// Calculates the paths available
	// Calls calcMove repeatedly until QueueMap is empty
	public void calcPath(int move, int x, int y) {
		
		reset();
		
		calcMove(move, x, y, x, y);
		
		while (true) {
			int[] next = map.next();
			if (next == null) {
				break;
			}
			calcMove(next[0], next[1], next[2], next[3], next[4]);
		}
		
	}
	
	public int[] calcDesiredPath(int move, int x, int y) {
		
		reset();
		
		int moveLimit = width * height;
		
		calcMove(moveLimit, x, y, x, y);
		
		while (!attackSet) {
			int[] next = map.next();
			if (next == null) {
				break;
			}
			calcMove(next[0], next[1], next[2], next[3], next[4]);
		}
		
//		if (attackAddr == -1) return new int[] {-1, x + (y * width)}; //SHOULD NOT EVER RUN but will if unit cannot find enemies
		
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
	private void calcMove(int move, int x, int y, int px, int py) {

		if (x < 0 || y < 0 || x >= width || y >= height) return; // Check bounds of level
		if (move <= -1) return;                          // Check if too far
		if (distances[x + (y * width)] >= move) return; // Check if shorter path already found
		if (level.getUnit(x, y) != null && level.getUnit(x, y).getTeam() != level.getActiveTeam()) return;  //Cannot move onto a non-playable unit
		if (level.getTile(x, y).solid()) return;
		
		distances[x + (y * width)] = move;              // Mark path distance
		
		if (!homeSet) {  // First space - can attack from here, but this unit is here too
			activeMove[x + (y * width)] = PathType.HOME;
			calcAttack(move, 1, x, y);
			homeSet = true;
		}  			// Cannot attack from another unit's space, so keep moving
		else if (level.getUnit(x, y) != null && level.getUnit(x, y).getTeam() == level.getActiveTeam()) { 
			activeMove[x + (y * width)] = PathType.PASS;
		}
		else {		// Mark move, calculate attack
			activeMove[x + (y * width)] = PathType.MOVE;
			calcAttack(move, 1, x, y);
		}
		
		prevTile[x + ((y) * width)] = (px + (py * width));
		
//		calcPath(move - 1, x, y + 1);
//		calcPath(move - 1, x, y - 1);
//		calcPath(move - 1, x + 1, y);
//		calcPath(move - 1, x - 1, y);

		// Queue all possible moves (breadth-first)
		// Better than recursion (depth-first)
		int[] down = 	{move - 1, x, y + 1, x, y};
		int[] up = 		{move - 1, x, y - 1, x, y};
		int[] right = 	{move - 1, x + 1, y, x, y};
		int[] left = 	{move - 1, x - 1, y, x, y};
		map.add(move - 1, down);
		map.add(move - 1, up);
		map.add(move - 1, right);
		map.add(move - 1, left);
		
		
	}
	
	// Labels nearby titles that can be attacked
	public void calcAttack(int move, int range, int x, int y) {
		
		for (int i = -2; i < 2; i++) {
			int xa = x + (range * (i % 2));
			int ya = y + (range * ((i + 1) % 2));
			
			if (xa < 0 || ya < 0 || xa >= width || ya >= height) continue;
			
			int index = xa + (ya * width);
			if (activeMove[index] == PathType.NONE) {
				distances[index] = -1;
				activeMove[index] = PathType.ATTACK;
				prevTile[index] = (x + (y * width));
				if (level.getUnit(xa, ya) != null && level.getUnit(xa, ya).getTeam() != level.getActiveTeam()) {
					attackSet = true;
					attackAddr = index;
				}
			}
			
		}
		
	}
	
	public void reset() {
		Arrays.fill(activeMove, PathType.NONE);
		Arrays.fill(prevTile, 0);
		Arrays.fill(distances, -10);
		homeSet = false;
		attackSet = false;
		attackAddr = -1;
		map.clear();
	}
	
	public PathType getType(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) return PathType.NONE;
		return activeMove[x + (y * width)];
	}
	
	public int prev(int x, int y) {
		return prevTile[x + (y * width)];
	}

	public enum PathType {
		NONE,
		MOVE,
		ATTACK,
		PASS,
		HOME
	}
}
