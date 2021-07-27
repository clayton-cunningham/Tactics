package com.clay13chopper.game1.entities.cursors;

import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.input.Keyboard;

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
		// Select a level
		if (Keyboard.getSelectStart()) {
			trigger = currSelect;
		}
		if (Keyboard.getDeselectStart()) {
			trigger = -2;
		}
		
	}
	
	//xC, yC is the change
	public void move(int xC, int yC) {
		
		if (currSelect + yC < 0 || currSelect + yC >= yLimit) {
			return;
		}
		
		super.move(xC, yC);
		currSelect += yC;
	}
	
	public int checkTrigger() {
		return trigger;
	}

}
