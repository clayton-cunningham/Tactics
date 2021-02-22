package com.clay13chopper.game1.entities.mob.player_units;

import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.graphics.Sprite;

public class Heavy extends Unit {
	
	public Heavy(int x, int y, Team t) {
		super(x, y);
		team = t;
		sprite = getSpriteDown();
		maxHealth = health = 15;
		movement = 2;
		attack = 3;
		minRange = maxRange = 1;
		if (t == Team.BLUE) {
			down0 = Sprite.blueHeavyDown0;
			down2 = Sprite.blueHeavyDown2;
			downDone0 = Sprite.blueHeavyDown0Done;
			downDone2 = Sprite.blueHeavyDown2Done;
		}
		else {
			down0 = Sprite.redHeavyDown0;
			down2 = Sprite.redHeavyDown2;
			downDone0 = Sprite.redHeavyDown0Done;
			downDone2 = Sprite.redHeavyDown2Done;
		}
	}

}
