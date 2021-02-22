package com.clay13chopper.game1.entities.mob.player_units;

import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.graphics.Sprite;

public class Soldier extends Unit {

	public Soldier(int x, int y, Team t) {
		super(x, y);
		team = t;
		sprite = getSpriteDown();
		maxHealth = health = 10;
		movement = 3;
		attack = 3;
		minRange = maxRange = 1;
		if (t == Team.BLUE) {
			down0 = Sprite.blueSoldierDown0;
			down2 = Sprite.blueSoldierDown2;
			downDone0 = Sprite.blueSoldierDown0Done;
			downDone2 = Sprite.blueSoldierDown2Done;
		}
		else {
			down0 = Sprite.redSoldierDown0;
			down2 = Sprite.redSoldierDown2;
			downDone0 = Sprite.redSoldierDown0Done;
			downDone2 = Sprite.redSoldierDown2Done;
		}
	}

}
