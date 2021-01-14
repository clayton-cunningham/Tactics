package com.clay13chopper.game1.entities.mob;

import com.clay13chopper.game1.entities.mob.Mob;

public abstract class Unit extends Mob {

	protected Team team;
	protected int maxHealth;
	protected int health;
	protected int movement;
	protected boolean turnDone;
	protected int attack;
	protected int range;
	
	protected boolean hovered = false;
	
	public Unit (int x, int y) {
		super(x, y);
	}
	
	public void update() {
		super.update();
		if (health < 1) remove();
		
		//Enemy or other non-playable units
		if (!isPlayable() && team == level.getActiveTeam() && !turnDone 
				&& (level.getUnitActing() == null || level.getUnitActing() == this)) {
			
			if (level.getUnitActing() == null) {
				delay = 0;
				level.pathFinder.calcPath(movement, x >> 4, y >> 4);
				level.setUnitActing(this);
			}
			
			delay++;
			if (delay > 80) {
				level.pathFinder.reset();
				act();
				turnDone = true;
				delay++;
				level.setUnitActing(null);
			}
			
		}
		
	}
	
	private void act() {
		int[] path = level.pathFinder.calcDesiredPath(movement, x >> 4, y >> 4);
		if (path[0] == -1)  {
			int mX = path[1] % 16;
			int mY = path[1] / 16;
			move((mX << 4) + 8, (mY << 4) + 8);
		}
		else {
			int aX = path[0] % 16;
			int aY = path[0] / 16;
			attack(level.getUnit(aX, aY));
			int mX = path[1] % 16;
			int mY = path[1] / 16;
			move((mX << 4) + 8, (mY << 4) + 8);
		}
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
	
	public int getMovement() {
		return movement;
	}
	
	public boolean getTurnDone() {
		return turnDone;
	}
	
	public void resetTurn() {
		turnDone = false;
	}
	
	public void move(int x, int y) {
		level.moveUnit(this, getX() >> 4, getY() >> 4, x >> 4, y >> 4);
		super.move(x, y);
//		super.move((x << 4) + 8, (y << 4) + 8);
		turnDone = true;
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
