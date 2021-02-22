package com.clay13chopper.game1.entities.mob.player_units;

import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.graphics.Sprite;

public class Runner extends Unit {
	
	public Runner(int x, int y, Team t) {
		super(x, y);
		team = t;
		sprite = getSpriteDown();
		maxHealth = health = 10;
		movement = 4;
		attack = 3;
		minRange = maxRange = 1;
		if (t == Team.BLUE) {
			down0 = Sprite.blueRunnerDown0;
			down2 = Sprite.blueRunnerDown2;
			downDone0 = Sprite.blueRunnerDown0Done;
			downDone2 = Sprite.blueRunnerDown2Done;
		}
		else {
			down0 = Sprite.redRunnerDown0;
			down2 = Sprite.redRunnerDown2;
			downDone0 = Sprite.redRunnerDown0Done;
			downDone2 = Sprite.redRunnerDown2Done;
		}
	}

}
