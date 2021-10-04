package com.clay13chopper.game1.entities.cursors;

import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.input.Keyboard;

/**
 * A cursor that exists for navigating menus
 * 		Currently compatible with the start menu and in-game menu
 * @author Clayton Cunningham
 *
 */
public class MenuCursor extends Cursor {

	private int currSelect = 0;
	private int trigger = -1;
	private int yLimit;
	
	public MenuCursor(int x, int y, int yL, int type) {
		super(x, y);
		switch (type) {
			case 0: // Start menu
				sprite = Sprite.menuCursorLarge;
				movement = 250;
				break;
			case 1: // In-game menu
				sprite = Sprite.menuSelector;
				this.x += sprite.getWidth() / 2;
				this.y += sprite.getHeight() / 2;
				movement = 14;
		}
		yLimit = yL;
	}
	
	public void update() {
		super.update();

		movementModerateY(ya);
		movementMemoryY(ya);
		
		trigger = -1;
		// Check for input
		if (Keyboard.getSelectStart()) {
			trigger = currSelect;
		}
		if (Keyboard.getDeselectStart()) {
			trigger = -2;
		}
		
	}

	/**
	 * Move according to the requested x/y change (format is -1/0/+1)
	 * 		Overrides so we can update current selection
	 * @param xa	change in x requested
	 * @param ya	change in y requested
	 */
	public void move(int xa, int ya) {
		super.move(xa, ya);
		currSelect += ya;
	}
	
	/**
	 * Check if within the bounds of the menu's list
	 * @param xa	change in x
	 * @param ya	change in y
	 * @return		true if within bounds
	 */
	protected boolean checkInBounds(int xa, int ya) {
		return (currSelect + ya >= 0 && currSelect + ya < yLimit);
	}

	// Get and set methods
	public int 	checkTrigger() 	{	return trigger;	}
	public void setX(int x) 	{ 	this.x = x; 	}
	public void setY(int y) 	{ 	this.y = y; 	}

}
