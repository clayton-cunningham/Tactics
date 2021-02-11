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
		range = 1;
	}

	public Sprite getSpriteDown() {
		if (anim % 100 >= 50) {
			if (team == Team.BLUE) {
				if (turnDone) return Sprite.blueSoldierDown0Done;
				else return Sprite.blueSoldierDown0;
			}
			else { 
				if (turnDone) return Sprite.redSoldierDown0Done;
				else return Sprite.redSoldierDown0;
			}
		}
		else if (anim % 100 < 50) {
			if (team == Team.BLUE) {
				if (turnDone) return Sprite.blueSoldierDown2Done;
				else return Sprite.blueSoldierDown2;
			}
			else {
				if (turnDone) return Sprite.redSoldierDown2Done;
				else return Sprite.redSoldierDown2;
			}
		}
		else return sprite;
	}
	
	public void update() {
		super.update();
		sprite = getSpriteDown();
	}

}
