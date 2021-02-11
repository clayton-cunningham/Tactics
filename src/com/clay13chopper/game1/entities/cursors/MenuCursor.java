package com.clay13chopper.game1.entities.cursors;

import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.input.Keyboard;
import com.clay13chopper.game1.room.level.Level;

public class MenuCursor extends Cursor {
	
	private int currSelect;
	private int yLimit;
	
	public MenuCursor(int x, int y, int yL) {
		super(x, y);
		sprite = Sprite.menuCursorLarge;
		movement = 250;
		currSelect = 0;
		yLimit = yL;
	}
	
	public void update() {
		super.update();

		movementModerateY(ya);
		movementMemoryY(ya);
		
		// Select a level
		if (Keyboard.getSelectStart()) {
			if (currSelect == 1) room.changeRoom(Level.level1);
			if (currSelect == 1) room.changeRoom(Level.level2);
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

}
