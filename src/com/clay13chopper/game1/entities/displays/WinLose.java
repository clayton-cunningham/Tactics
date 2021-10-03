package com.clay13chopper.game1.entities.displays;

import com.clay13chopper.game1.entities.Entity;
import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.input.Keyboard;
import com.clay13chopper.game1.room.StartMenu;

/**
 * Records Win/Lost status at end of level
 * 		Note: this is currently not the UI display
 * 		Also signals level to change rooms
 * @author Clayton Cunningham
 *
 */
public class WinLose extends Entity{
	
	public WinLose(int wL, int x, int y) {
		if (wL == 1) sprite = Sprite.win;
		else sprite = Sprite.lose;
		this.x = x;
		this.y = y;
	}
	
	public void update() {
		
		// Select a level
		if (Keyboard.getSelectStart()) {
			room.changeRoom(new StartMenu());
		}
	}

}
