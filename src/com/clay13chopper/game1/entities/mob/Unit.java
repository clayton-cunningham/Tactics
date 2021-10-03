package com.clay13chopper.game1.entities.mob;

import com.clay13chopper.game1.entities.mob.Mob;
import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.graphics.Sprite;

/**
 * Unit class
 * 	Behavior units in a class will use
 * 
 * @author Clayton Cunningham
 *
 */
public abstract class Unit extends Mob {

	protected Team team;
	protected int maxHealth;
	protected int health;
	protected boolean turnDone;
	protected int attack;
	protected int minRange;
	protected int maxRange;

	// Sprite values held for faster lookup
	protected Sprite down0;
	protected Sprite down2;
	protected Sprite downDone0;
	protected Sprite downDone2;
	
	protected boolean hovered = false;
	int[] path = new int[0]; // Only used by AI / non-players;  details in act() method below
	
	public Unit (int x, int y) {
		super(x, y);
	}
	
	public void update() {
		super.update();
		if (health < 1) remove();

		// Only enemy or other non-playable units: steps for their turn
		// This provides a delay so the player can see things happening in real time
		else if (!isPlayable() && team == level.getActiveTeam() && !turnDone && level.getLevelComplete() == 0
				&& (level.getUnitActing() == null || level.getUnitActing() == this)) {
			
			// Start turn for this unit 
			if (level.getUnitActing() == null) {
				delay = 0;
				level.setUnitActing(this);
			}
			
			// Show movement on screen
			if (delay == 2) {
				// Calculate closest action available
				path = level.pathFinder.calcDesiredPath(movement, minRange, maxRange, xGrid, yGrid, team);
				level.pathFinder.reset();
				// If no actions are available, skip showing movement and just end turn
				if (path[0] == -1 && path[1] == xGrid + (yGrid * level.getWidth())) delay = 60;
				else level.pathFinder.calcPath(movement, minRange, maxRange, xGrid, yGrid, team);
			}
			
			// End turn
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
	
	public void render(Screen screen) {
		super.render(screen);

		int sW = sprite.getWidth();
		int sH = sprite.getHeight();
		
		// if hurt, display a health bar
		if (getHealthPercent() < 100) {
			screen.renderSprite(x - (sW / 2) + 1, y + (sH / 2) - 3, Sprite.healthBar, false, false);
			screen.renderSprite(x - (sW / 2) + 2, y + (sH / 2) - 2, Sprite.showHealth(this), false, false);
		}
	}
	
	/**
	 * Actions non-player units follow:
	 *   If an attack target is recorded, attack that target
	 *   Move to the space 
	 */
	private void act() {
		if (path[0] != -1)  {
			int aX = path[0] % level.getWidth();
			int aY = path[0] / level.getWidth();
			attack(level.getUnit(aX, aY), level.getTile(aX, aY).defense());
		}
		int mX = path[1] % level.getWidth();
		int mY = path[1] / level.getWidth();
		move(mX, mY);
	}

	/**
	 * Retrieves a downward facing sprite
	 *   Changes based on the unit's animation counter and if their turn is down
	 * TODO: add more directional sprite options;  maybe add a class SpriteManager?
	 * @return	Sprite of the unit to display
	 */
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
	
	/**
	 * Tells another unit to reduce their health and by how much
	 * @param target Unit to reduce health
	 * @param tileDefense for target's space
	 */
	public void attack(Unit target, int tileDefense) {
		target.reduceHealth(this.attack - tileDefense);
	}
	
	/**
	 * Exposed method to change health
	 * @param damage to reduce health by
	 */
	public void reduceHealth(int damage) {
		if (damage > 0) health -= damage;
	}
	
	/**
	 * Moves the Unit to the address specified
	 * The Unit's turn is ended once moved
	 * Address is read in grid format
	 * @param xG 	x address in grid format
	 * @param yG 	y address in grid format
	 */
	public void move(int xG, int yG) {
		level.moveUnit(this, xGrid, yGrid, xG, yG);
		int shift = level.getShift();
		int halfTile = level.getTileSize() / 2;
		super.move((xG << shift) + halfTile, (yG << shift) + halfTile);
		xGrid = xG;
		yGrid = yG;
		turnDone = true;
	}
	
	/**
	 * Returns whether the unit is playable for the current team
	 * Right now, only Blue team is playable.  Further support will be needed.
	 * @return whether this Unit is playable or not
	 */
	public boolean isPlayable() {
		return team == Team.BLUE;
	}
	
	/**
	 * Resets the Unit's turn
	 * Is called when the Unit's team is done with its turn
	 * This is done to show the correct sprite while other teams are acting
	 */
	public void resetTurn() {
		turnDone = false;
	}
	
	/**
	 * Retrieves the percentage of health left, in integer format
	 * Is called to accurately display the health bar
	 * @return	percentage of health left
	 */
	public int getHealthPercent() {
		return (health * 100) / maxHealth;
	}
	
	// Get methods
	public int 		getMovement() 		{	return movement;	}
	public int 		getMinRange() 		{	return minRange;	}
	public int 		getMaxRange() 		{	return maxRange;	}
	public boolean 	getTurnDone() 		{	return turnDone;	}
	public Team 	getTeam() 			{	return team;		}
	
	/**
	 * Team enumeration
	 * 	Sets up Team label with integers for more readable checks
	 */
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
