package com.clay13chopper.game1.entities.cursors;

import com.clay13chopper.game1.entities.Entity;
import com.clay13chopper.game1.input.Keyboard;

public class Cursor extends Entity {

	int xa, ya;
	
	public Cursor(int x, int y) {
		this.x = x;
		this.y = y;
		movement = 1;
	}
	
	public void update() {
		super.update();
		
		xa = 0; ya = 0;
		if (Keyboard.getUp()) ya--;
		if (Keyboard.getDown())	ya++;
		if (Keyboard.getLeft()) xa--;
		if (Keyboard.getRight()) xa++;
		
	}
	
	
	
	/**
	 * Equalize the movement pattern, but only if past the time threshold
	 * @param xa	Input in x direction
	 * @param ya	Input in y direction
	 */
	protected void movementEqualize(int xa, int ya) {
		if (ya != 0 && xa != 0 && Math.abs(yOld) != Math.abs(xOld) && (Math.abs(xOld) > 30 || Math.abs(yOld) > 30)) {
			int max = Math.max(Math.abs(yOld), Math.abs(xOld));
			yOld = max * ya;
			xOld = max * xa;
		}
	}
	
	/**
	 * If we're told to move, but haven't moved yet, move.  After a threshold, move regularly
	 * Separate functions so x & y move independently (even if they're running at the same time anyway)
	 * @param xa	Input in x direction
	 */
	protected void movementModerateX(int xa) {
		if (xa != 0 && (xOld == 0 || ((Math.abs(xOld) > 30) && (xOld % 5 == 0)))) {
			move(xa, 0);
		}
	}
	
	protected void movementModerateY(int ya) {
		if (ya != 0 && (yOld == 0 || ((Math.abs(yOld) > 30) && (yOld % 5 == 0)))) {
			move(0, ya);
		}
	}
	
	/**
	 * If no input, you're not moving.  Otherwise, if input is the same +/- sign, increase the counter.  Otherwise, reset.
	 * We don't need the first if statement, but it's more clear with it.
	 * Separate functions so x & y move independently (even if they're running at the same time anyway)
	 * @param xa	Input in x direction
	 */
	protected void movementMemoryX(int xa) {
		if (xa == 0) xOld = 0;
		else if (xa * xOld > 0) xOld += xa;
		else xOld = xa;
	}
	
	protected void movementMemoryY(int ya) {
		if (ya == 0) yOld = 0;
		else if (ya * yOld > 0) yOld += ya;
		else yOld = ya;
	}
	
	
	//xC, yC is the change
	public void move(int xC, int yC) {
		if (xC != 0) {
			x += xC * movement;
		}
		if (yC != 0) {
			y += yC * movement;
		}
	}

}
