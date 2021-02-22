package com.clay13chopper.game1.entities.mob.player_units;

import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.graphics.Sprite;

public class Mage extends Unit {
	
	public Mage(int x, int y, Team t) {
		super(x, y);
		team = t;
		sprite = getSpriteDown();
		maxHealth = health = 6;
		movement = 3;
		attack = 3;
		minRange = 1;
		maxRange = 2;
		if (t == Team.BLUE) {
			down0 = Sprite.blueMageDown0;
			down2 = Sprite.blueMageDown2;
			downDone0 = Sprite.blueMageDown0Done;
			downDone2 = Sprite.blueMageDown2Done;
		}
		else {
			down0 = Sprite.redMageDown0;
			down2 = Sprite.redMageDown2;
			downDone0 = Sprite.redMageDown0Done;
			downDone2 = Sprite.redMageDown2Done;
		}
	}

}
