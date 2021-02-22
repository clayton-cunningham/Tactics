package com.clay13chopper.game1.entities.mob.player_units;

import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.graphics.Sprite;

public class Archer extends Unit {
	
	public Archer(int x, int y, Team t) {
		super(x, y);
		team = t;
		sprite = getSpriteDown();
		maxHealth = health = 8;
		movement = 3;
		attack = 3;
		range = 2;
		if (t == Team.BLUE) {
			down0 = Sprite.blueArcherDown0;
			down2 = Sprite.blueArcherDown2;
			downDone0 = Sprite.blueArcherDown0Done;
			downDone2 = Sprite.blueArcherDown2Done;
		}
		else {
			down0 = Sprite.redArcherDown0;
			down2 = Sprite.redArcherDown2;
			downDone0 = Sprite.redArcherDown0Done;
			downDone2 = Sprite.redArcherDown2Done;
		}
	}

}
