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
			Unit unitViewed = level.getUnit(x >> 4, y >> 4);
			
			if (unitChosen == null && unitViewed == null) {
				//TODO: enable showing menu info
			}
			else if (unitChosen == null && unitViewed.isPlayable() && !unitViewed.getTurnDone()) { //10 && playable // selecting a unit
				unitChosen = unitViewed;
				level.pathFinder.calcPath(unitChosen.getMovement(), unitChosen.getMinRange(), unitChosen.getMaxRange(), x >> 4, y >> 4);
			}
			else if ((unitViewed == null || unitViewed == unitChosen) 
					&& (level.pathFinder.getType(x>>4, y>>4) == PathType.MOVE || level.pathFinder.getType(x>>4, y>>4) == PathType.HOME)) {  // 01 or == // move a unit
//				level.moveUnit(unitChosen, unitChosen.getX() >> 4, unitChosen.getY() >> 4, x >> 4, y >> 4);
				unitChosen.move(x, y);
				unitChosen = null;
				level.pathFinder.reset();
			}
			else if (unitChosen != null && unitViewed != null && !unitViewed.isPlayable() 
					&& level.pathFinder.getType(x >> 4, y >> 4) == PathType.ATTACK) { //11 && not playable // attacking
				int ua = level.pathFinder.prev(x >> 4, y >> 4);
				int uXa = ua % 16;
				int uYa = ua / 16;
//				level.moveUnit(unitChosen, unitChosen.getX() >> 4, unitChosen.getY() >> 4, uXa, uYa);
				unitChosen.move((uXa << 4) + 8, (uYa << 4) + 8);
				unitChosen.attack(unitViewed);
				unitChosen = null;
				level.pathFinder.reset();
				
			}
			else {  // 11 && !=
				cursorError();
			}
			
		}
		if (Keyboard.getDeselectStart()  && level.getActiveTeam() == Team.BLUE) {
			// Deselect unit
			unitChosen = null;
			level.pathFinder.reset();
			
		}
		
	}
	
	//xC, yC is the change
//	public void move(int xC, int yC) {
//		if (xC != 0) {
//			x += xC * movement;
//		}
//		if (yC != 0) {
//			y += yC * movement;
//		}
//	}
	
	public void cursorError() {
		anim = 25;
		sprite = Sprite.cursorError1;
	}

}
