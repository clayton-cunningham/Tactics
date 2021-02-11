package com.clay13chopper.game1.entities;

import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.input.Keyboard;
import com.clay13chopper.game1.room.StartMenu;

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
