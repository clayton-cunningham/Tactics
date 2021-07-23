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
	int[] path = new int[0]; // Only used by AI
	
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
				path = level.pathFinder.calcDesiredPath(movement, minRange, maxRange, xGrid, yGrid);
				level.pathFinder.reset();
				if (path[0] == -1 && path[1] == xGrid + (yGrid * level.getWidth())) delay = 70;
				else level.pathFinder.calcPath(movement, minRange, maxRange, xGrid, yGrid);
			}
			if (delay > 80) {
				act();
				level.pathFinder.reset();
				turnDone = true;
				level.setUnitActing(null);
			}
			delay++;
			
		}

		sprite = getSpriteDown();
		
	}
	
	private void act() {
		if (path[0] != -1)  {
			int aX = path[0] % level.getWidth();
			int aY = path[0] / level.getWidth();
			attack(level.getUnit(aX, aY));
		}
		int mX = path[1] % level.getWidth();
		int mY = path[1] / level.getWidth();
		move(mX, mY);
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
	
	public void move(int xG, int yG) {
		level.moveUnit(this, xGrid, yGrid, xG, yG);
		int shift = level.getShift();
		int halfTile = level.getTileSize() / 2;
		super.move((xG << shift) + halfTile, (yG << shift) + halfTile);
		xGrid = xG;
		yGrid = yG;
		turnDone = true;
	}
	
	public int getXGrid() {
		return xGrid;
	}
	
	public int getYGrid() {
		return yGrid;
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
		GREEN (3),
		NONE (4);
		
		private final int id;
		Team(int id) { this.id = id; }
		public int getId() { return id; }
	}

}
