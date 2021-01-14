package com.clay13chopper.game1.entities;

import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.entities.mob.Unit.Team;
import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.input.Keyboard;
import com.clay13chopper.game1.processors.PathFinder.PathType;

public class Cursor extends Entity {

	private int anim = 0;
	protected int selInput;
	protected int deselInput;
	private Unit unitChosen = null;
	
	public Cursor(int x, int y) {
		this.x = x;
		this.y = y;
		sprite = Sprite.cursor1;
	}

	public void update() {
		int xa = 0, ya = 0;
		//Animation counter
		if (anim < 3000) anim++; else anim = 0;
		
		if (anim % 100 == 50) sprite = Sprite.cursor2; 
		if (anim % 100 == 0) sprite = Sprite.cursor1; 
		
		if (Keyboard.getUp()) ya--;
		if (Keyboard.getDown())	ya++;
		if (Keyboard.getLeft()) xa--;
		if (Keyboard.getRight()) xa++;
		

		//Equalize the movement pattern, but only if past the time threshold
		if (ya != 0 && xa != 0 && Math.abs(yd) != Math.abs(xd) && (Math.abs(xd) > 30 || Math.abs(yd) > 30)) {
			int max = Math.max(Math.abs(yd), Math.abs(xd));
			yd = max * ya;
			xd = max * xa;
		}
		
		//If we're told to move, but haven't moved yet, move.  After a threshold, move regularly
		//Separate functions so x & y move independently (even if they're running at the same time anyway)
		if (xa != 0 && (xd == 0 || ((Math.abs(xd) > 30) && (xd % 5 == 0)))) {
			move(16 * xa, 0);
		}

		if (ya != 0 && (yd == 0 || ((Math.abs(yd) > 30) && (yd % 5 == 0)))) {
			move(0, 16 * ya);
		}

		//If no input, you're not moving.  Otherwise, if input is the same +/- sign, increase the counter.  Otherwise, reset.
		//Doesn't need the first if statement, but it's more clear with it.
		if (ya == 0) yd = 0;
		else if (ya * yd > 0) yd += ya;
		else yd = ya;
		if (xa == 0) xd = 0;
		else if (xa * xd > 0) xd += xa;
		else xd = xa;		
		
		//Select or move unit
		//TODO: Keyboard class should have onPressed methods instead of just getters
		if (Keyboard.getSelect()) selInput++;
		else selInput = 0;
		if (Keyboard.getDeselect() && selInput != 1) deselInput++;
		else deselInput = 0;
		
		if (selInput == 1  && level.getActiveTeam() == Team.BLUE) {

			//Recover unit at location
			Unit unitViewed = level.getUnit(x >> 4, y >> 4);
			
			if (unitChosen == null && unitViewed == null) {
				//TODO: enable showing menu info
			}
			else if (unitChosen == null && unitViewed.isPlayable() && !unitViewed.getTurnDone()) { //10 && playable // selecting a unit
				unitChosen = unitViewed;
				level.pathFinder.calcPath(unitChosen.getMovement(), x >> 4, y >> 4);
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
		if (deselInput == 1  && level.getActiveTeam() == Team.BLUE) {
			// Deselect unit
			unitChosen = null;
			level.pathFinder.reset();
			
		}
		
	}
	
	//xC, yC is the change
	public void move(int xC, int yC) {
		if (xC != 0) {
			x += xC;
		}
		if (yC != 0) {
			y += yC;
		}
	}
	
	public void cursorError() {
		anim = 25;
		sprite = Sprite.cursorError1;
	}

}
