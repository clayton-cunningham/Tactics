package com.clay13chopper.game1.entities.mob;

import com.clay13chopper.game1.entities.mob.Mob;
import com.clay13chopper.game1.graphics.Sprite;

public abstract class Unit extends Mob {

	protected Team team;
	protected int maxHealth;
	protected int health;
	protected boolean turnDone;
	protected int attack;
	protected int minRange;
	protected int maxRange;

	protected Sprite down0;
	protected Sprite down2;
	protected Sprite downDone0;
	protected Sprite downDone2;
	
	protected boolean hovered = false;
	
	public Unit (int x, int y) {
		super(x, y);
	}
	
	public void update() {
		super.update();
		if (health < 1) remove();
		
		//Enemy or other non-playable units
		if (!isPlayable() && team == level.getActiveTeam() && !turnDone && level.getLevelComplete() == 0
				&& (level.getUnitActing() == null || level.getUnitActing() == this)) {
			
			if (level.getUnitActing() == null) {
				delay = 0;
				level.setUnitActing(this);
			}
			
			if (delay == 2) {
				level.pathFinder.calcPath(movement, minRange, maxRange, x >> 4, y >> 4);
			}
			else if (delay > 80) {
				act();
				turnDone = true;
				level.setUnitActing(null);
			}
			delay++;
			
		}

		sprite = getSpriteDown();
		
	}
	
	private void act() {
		level.pathFinder.reset();
		int[] path = level.pathFinder.calcDesiredPath(movement, minRange, maxRange, x >> 4, y >> 4);
		if (path[0] != -1)  {
			int aX = path[0] % 16;
			int aY = path[0] / 16;
			attack(level.getUnit(aX, aY));
		}
		int mX = path[1] % 16;
		int mY = path[1] / 16;
		move((mX << 4) + 8, (mY << 4) + 8);
	}

	public Sprite getSpriteDown() {
		if (anim % 100 >= 50) {
			if (turnDone) return downDone0;
			else return down0;
		}
		else if (anim % 100 < 50) {
			if (turnDone) return downDone2;
			else return down2;
		}
		else return sprite;
	}
	
	public void attack(Unit target) {
		target.reduceHealth(this.attack);
	}
	
	public void reduceHealth(int damage) {
		health -= damage;
	}
	
	//Returns whether the unit is playable for the current team
	//Right now, only Blue team is playable.  Further support will be needed.
	public boolean isPlayable() {
		return team == Team.BLUE;
	}
	
	public void move(int x, int y) {
		level.moveUnit(this, getX() >> 4, getY() >> 4, x >> 4, y >> 4);
		super.move(x, y);
//		super.move((x << 4) + 8, (y << 4) + 8);
		turnDone = true;
	}
	
	public int getMovement() {
		return movement;
	}
	
	public int getMinRange() {
		return minRange;
	}
	
	public int getMaxRange() {
		return maxRange;
	}
	
	public boolean getTurnDone() {
		return turnDone;
	}
	
	public void resetTurn() {
		turnDone = false;
	}
	
	public int getHealthPercent() {
		return (health * 100) / maxHealth;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public enum Team {
		BLUE (0),
		RED (1),
		YELLOW (2),
		GREEN (3);
		
		private final int id;
		Team(int id) { this.id = id; }
		public int getId() { return id; }
	}

}
