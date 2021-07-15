package com.clay13chopper.game1.entities.cursors;

import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.entities.mob.Unit.Team;
import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.input.Keyboard;
import com.clay13chopper.game1.processors.PathFinder.PathType;
import com.clay13chopper.game1.room.level.Level;

public class MapCursor extends Cursor {

	private Unit unitChosen = null;
	
	public MapCursor(int x, int y) {
		super(x, y);
		sprite = Sprite.cursor1;
	}
	
	public void init(Level l) {
		super.init(l);
		movement = level.getTileSize();
	}

	public void update() {
		super.update();
		//Animation counter
		if (anim % 100 == 50) sprite = Sprite.cursor2; 
		if (anim % 100 == 0) sprite = Sprite.cursor1; 

		movementEqualize(xa, ya);
		movementModerateX(xa);
		movementModerateY(ya);
		movementMemoryX(xa);
		movementMemoryY(ya);
		
		//Select or move unit
		if (Keyboard.getSelectStart()  && level.getActiveTeam() == Team.BLUE) {

			//Recover unit at location
			Unit unitViewed = level.getUnit(xGrid, yGrid);
			
			if (unitChosen == null && unitViewed == null) {
				// 00  // empty space
				//TODO: enable showing tile info
			}
			else if (unitChosen == null && unitViewed.isPlayable() && !unitViewed.getTurnDone()) { 
				//10 && playable // selecting a unit
				unitChosen = unitViewed;
				level.pathFinder.calcPath(unitChosen.getMovement(), unitChosen.getMinRange(), unitChosen.getMaxRange(), xGrid, yGrid);
			}
			else if ((unitViewed == null || unitViewed == unitChosen) 
					&& (level.pathFinder.getType(xGrid, yGrid) == PathType.MOVE 
					|| level.pathFinder.getType(xGrid, yGrid) == PathType.HOME)) {  
				// 01 or == // move a unit
				unitChosen.move(xGrid, yGrid);
				unitChosen = null;
				level.pathFinder.reset();
			}
			else if (unitChosen != null && unitViewed != null && !unitViewed.isPlayable() 
					&& level.pathFinder.getType(xGrid, yGrid) == PathType.ATTACK) { 
				//11 && not playable // attacking
				int ua = level.pathFinder.getHoveredTile();
				if (ua == -1) ua = level.pathFinder.prev(xGrid, yGrid);
				int uXa = ua % level.getWidth();
				int uYa = ua / level.getWidth();
				unitChosen.move(uXa, uYa);
				unitChosen.attack(unitViewed);
				unitChosen = null;
				level.pathFinder.reset();
				
			}
			else {  
				// 11 && !=  // can't attack or move to team-occupied space
				cursorError();
			}
			
		}
		
		// Deselecting a unit
		if (Keyboard.getDeselectStart()  && level.getActiveTeam() == Team.BLUE) {
			unitChosen = null;
			level.pathFinder.reset();
			
		}
		
		// Behavior from cursor hovering
		// TODO: the hoveredTile variable shouldn't be in PathFinder - it isn't even used there.  Put it where it's used
		if ((xa != 0 || ya != 0) && unitChosen != null) {
			
			// Enable showing path of a unit
			PathType hoveredTileType = level.pathFinder.getType(xGrid, yGrid);
			Unit unitViewed = level.getUnit(xGrid, yGrid);
			if (hoveredTileType == PathType.MOVE 
					|| hoveredTileType == PathType.HOME) {
				level.pathFinder.setHoveredTile(xGrid, yGrid);
			}
			else if (hoveredTileType == PathType.ATTACK && unitViewed != null && !unitViewed.isPlayable()) {
				int pastHoveredTile = level.pathFinder.getHoveredTile();
				if (pastHoveredTile == -1) {

					int newHoveredTile = level.pathFinder.prev(xGrid, yGrid);
					level.pathFinder.setHoveredTile(newHoveredTile % level.getWidth(), newHoveredTile / level.getWidth());
				}
				else {
					int distance = Math.abs((pastHoveredTile % level.getWidth()) - xGrid) 
									+ Math.abs((pastHoveredTile / level.getWidth()) - yGrid);
					if (distance < unitChosen.getMinRange() || distance > unitChosen.getMaxRange()) {
						int newHoveredTile = level.pathFinder.prev(xGrid, yGrid);
						level.pathFinder.setHoveredTile(newHoveredTile % level.getWidth(), newHoveredTile / level.getWidth());
					}
				}
				
			}
			
		}
		
	}
	
	public void showPath() {
		
	}
	
	public void cursorError() {
		anim = 25;
		sprite = Sprite.cursorError1;
	}

}
