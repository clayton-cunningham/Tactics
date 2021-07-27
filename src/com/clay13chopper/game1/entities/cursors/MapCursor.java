package com.clay13chopper.game1.entities.cursors;


import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.entities.mob.Unit.Team;
import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.input.Keyboard;
import com.clay13chopper.game1.processors.PathFinder.PathType;
import com.clay13chopper.game1.room.level.Level;

public class MapCursor extends Cursor {

	private Unit unitChosen = null;
	private boolean invisible = false;
	private boolean locked = false;
	
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

		if (locked) return;
		
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
				level.createTextBox(x, y, new int[]{0, 1});
				locked = true;
			}
			else if (unitChosen == null && unitViewed.isPlayable() && !unitViewed.getTurnDone()) { 
				//10 && playable // selecting a unit
				unitChosen = unitViewed;
				level.pathFinder.calcPath(unitChosen.getMovement(), unitChosen.getMinRange(), unitChosen.getMaxRange(), xGrid, yGrid);
				level.pathDisplay.setHoveredTile(xGrid, yGrid, unitChosen, level.pathFinder);
			}
			else if ((unitViewed == null || unitViewed == unitChosen) 
					&& (level.pathFinder.getType(xGrid, yGrid) == PathType.MOVE 
					|| level.pathFinder.getType(xGrid, yGrid) == PathType.HOME)) {  
				// 01 or == // move a unit
				level.createTextBox(x, y, new int[]{3, 4});
				locked = true;
			}
			else if (unitChosen != null && unitViewed != null && !unitViewed.isPlayable() 
					&& level.pathFinder.getType(xGrid, yGrid) == PathType.ATTACK) { 
				//11 && not playable // attacking
				int ua = level.pathDisplay.getHoveredTile();
				if (ua == -1) ua = level.pathFinder.prev(xGrid, yGrid); // NOTE - this line shouldn't run; pathDisplay should always be set
				int uXa = ua % level.getWidth();
				int uYa = ua / level.getWidth();
				unitChosen.move(uXa, uYa);
				unitChosen.attack(unitViewed);
				deselectUnit();
			}
			else {  
				// 11 && !=  // can't attack or move to team-occupied space
				cursorError();
			}
		}
		
		// Deselecting a unit
		if (Keyboard.getDeselectStart()  && level.getActiveTeam() == Team.BLUE) {
			deselectUnit();
			
		}
		
		// Behavior for when cursor is hovering on a new spot
		if (signalMoved && unitChosen != null) {
			
			// Enable showing path of a unit
			updatePath();
			
		}

		//TODO: enable showing tile info
		//if (signalMoved && unitChosen != null && unitViewed == null)
		
	}
	
	public void render(Screen screen) {
		if (invisible) return;
		super.render(screen);
	}
	
	protected boolean checkInBounds(int xa, int ya) {
		if (level.getTile(xGrid + xa, yGrid + ya).outOfBounds()) {
			return false;
		}
		return true;
	}
	
	protected void updatePath() {
		PathType hoveredTileType = level.pathFinder.getType(xGrid, yGrid);
		Unit unitViewed = level.getUnit(xGrid, yGrid);
		if (hoveredTileType == PathType.MOVE 
				|| hoveredTileType == PathType.HOME) {
			level.pathDisplay.setHoveredTile(xGrid, yGrid, unitChosen, level.pathFinder);
		}
		else if (hoveredTileType == PathType.ATTACK && unitViewed != null && !unitViewed.isPlayable()) {
			level.pathDisplay.confirmAttackDistance(xGrid, yGrid, unitChosen, level.pathFinder);
		}
		//TODO: After menus are implemented and you can choose where you want to attack from, un-comment this
//		else {
//			level.pathDisplay.reset();
//		}
	}
	
	protected void cursorError() {
		anim = 25;
		sprite = Sprite.cursorError2;
	}
	
	protected void deselectUnit() {
		unitChosen = null;
		level.pathFinder.reset();
		level.pathDisplay.reset();
	}
	
	public void unlock() {
		locked = false;
	}
	
	public void approveMove() {
		unitChosen.move(xGrid, yGrid);
		deselectUnit();
		locked = false;
	}

}
