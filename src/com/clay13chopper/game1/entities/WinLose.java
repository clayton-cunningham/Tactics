package com.clay13chopper.game1.entities;

import com.clay13chopper.game1.graphics.Sprite;

public class WinLose extends Entity{
	
	public WinLose(int wL, int x, int y) {
		if (wL == 1) sprite = Sprite.win;
		else sprite = Sprite.lose;
		this.x = x;
		this.y = y;
	}

}
