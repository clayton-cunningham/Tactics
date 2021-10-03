package com.clay13chopper.game1.entities.cursors;

import com.clay13chopper.game1.entities.Entity;
import com.clay13chopper.game1.input.Keyboard;

/**
 * Parent class for any cursor type
 * 		Programs movement behavior to be smooth and user friendly
 * @author Clayton Cunningham
 *
 */
public abstract class Cursor extends Entity {

	// xa/ya		are the requested change (+1/0/-1) based on player input
	// xHeld/yHeld 	are the x/y values from the previous frame
	int xa, ya, xHeld, yHeld;
	boolean signalMoved = false;
	
	public Cursor(int x, int y) {
		this.x = x;
		this.y = y;
		movement = 1;
	}
	
	public void update() {
		super.update();
		signalMoved = false;
		
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
		if (ya != 0 && xa != 0 && Math.abs(yHeld) != Math.abs(xHeld) && (Math.abs(xHeld) > 30 || Math.abs(yHeld) > 30)) {
			int max = Math.max(Math.abs(yHeld), Math.abs(xHeld));
			yHeld = max * ya;
			xHeld = max * xa;
		}
	}
	
	/**
	 * If we're told to move, but haven't moved yet, move.  After a threshold, move regularly
	 * Separate functions so x & y move independently (even if they're running at the same time anyway)
	 * @param xa	Input in x direction
	 */
	protected void movementModerateX(int xa) {
		if ((xa != 0 && (xHeld == 0 || ((Math.abs(xHeld) > 30) && (xHeld % 5 == 0)))) 
				&& checkInBounds(xa, 0)) {
			move(xa, 0);
		}
	}
	
	protected void movementModerateY(int ya) {
		if ((ya != 0 && (yHeld == 0 || ((Math.abs(yHeld) > 30) && (yHeld % 5 == 0))))
				&& checkInBounds(0, ya)) {
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
		if (xa == 0) xHeld = 0;
		else if (xa * xHeld > 0) xHeld += xa;
		else xHeld = xa;
	}
	
	protected void movementMemoryY(int ya) {
		if (ya == 0) yHeld = 0;
		else if (ya * yHeld > 0) yHeld += ya;
		else yHeld = ya;
	}
	
	
	/**
	 * Move according to the requested x/y change (format is -1/0/+1)
	 * @param xa	change in x requested
	 * @param ya	change in y requested
	 */
	public void move(int xa, int ya) {
		if (xa != 0) {
			x += xa * movement;
			xGrid += xa;
		}
		if (ya != 0) {
			y += ya * movement;
			yGrid += ya;
		}
		signalMoved = true;
	}

	/**
	 * Check if within the bounds of the cursor's container
	 * 		Since this will depend on where the cursor is used,
	 * 		it must be implemented for each type individually.
	 * @param xa	change in x
	 * @param ya	change in y
	 * @return		true if within bounds
	 */
	protected abstract boolean checkInBounds(int xa, int ya);

}
