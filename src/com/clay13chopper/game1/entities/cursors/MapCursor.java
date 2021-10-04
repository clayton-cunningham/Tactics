package com.clay13chopper.game1.entities.cursors;


import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.entities.mob.Unit.Team;
import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.input.Keyboard;
import com.clay13chopper.game1.processors.PathFinder.PathType;
import com.clay13chopper.game1.room.level.Level;

/**
 * A cursor that navigates levels
 * @author Clayton Cunningham
 *
 */
public class MapCursor extends Cursor {

	private Unit unitChosen = null;
	private boolean invisible = false;	// If true, the cursor is invisible (i.e. during enemy turns)
	private boolean locked = false;		// If true, the cursor is locked (i.e. the game is focused on the in-game menu)
	private boolean pathSet = false;	// If true, the pathfinder has been setup for movement to an attack space
	
	public MapCursor(int x, int y) {
		super(x, y);
		sprite = Sprite.cursor1;
	}
	
	public void init(Level l) {
		super.init(l);
		movement = level.getTileSize();
	}

	public void update() {
		super.update();
		
		//Animation counter
		if (anim % 100 == 50) sprite = Sprite.cursor2; 
		if (anim % 100 == 0) sprite = Sprite.cursor1; 

		if (locked || level.getActiveTeam() != Team.BLUE) return;
		
		// Movement
		movementEqualize(xa, ya);
		movementModerateX(xa);
		movementModerateY(ya);
		movementMemoryX(xa);
		movementMemoryY(ya);
		
		//Selecting a space
		if (Keyboard.getSelectStart()) {
			updateSelection();
		}
		else if (Keyboard.getSelectRelease()){
			// Reset enemy paths (if player selected an enemy to view their movements)
			if (unitChosen == null) {
				level.pathFinder.reset();
				level.pathFinder.enemyNotShown();
			}
		}
		
		// Deselecting a unit
		if (Keyboard.getDeselectStart()) {
			// If movement space was selected, go back to selecting movement
			if (pathSet) {
				level.pathFinder.clearCustomMove();
				pathSet = false;
			}
			// If not, unselect unit entirely
			else deselectUnit();
			
		}
		
		// Behavior for when cursor is hovering on a new spot
		if (signalMoved) {
			if (unitChosen != null && !pathSet) {
				updatePath();
			}
		}

		//TODO: enable showing tile info
		//if (signalMoved && unitChosen != null && unitViewed == null)
		
	}
	
	public void render(Screen screen) {
		if (invisible) return;
		super.render(screen);
	}
	
	/**
	 * Updates the level based on the location of the cursor
	 * 		when the user enters the selection button
	 */
	private void updateSelection() {

		//Recover unit at location
		Unit unitViewed = level.getUnit(xGrid, yGrid);

		// 00  // empty space
		if (unitChosen == null && unitViewed == null) {
			// Display options
			level.createTextBox(x + level.getTileSize(), y, new int[]{0, 1, 4});
			locked = true;
		}
		//10 && playable // selecting a unit
		else if (unitChosen == null && unitViewed.isPlayable() && !unitViewed.getTurnDone()) { 
			unitChosen = unitViewed;
			level.pathFinder.calcPath(unitChosen.getMovement(), 
					unitChosen.getMinRange(), unitChosen.getMaxRange(), xGrid, yGrid, unitChosen.getTeam());
			level.pathDisplay.setHoveredTile(xGrid, yGrid, unitChosen, level.pathFinder);
		}
		//10 && not playable // showing enemy paths
		else if (unitChosen == null && unitViewed != null && !unitViewed.isPlayable()) {
			level.pathFinder.calcPath(unitViewed.getMovement(), 
					unitViewed.getMinRange(), unitViewed.getMaxRange(), xGrid, yGrid, unitViewed.getTeam());
			level.pathFinder.enemyShown();
		}
		// 01 or == // move a unit
		else if ((unitViewed == null || unitViewed == unitChosen) 
				&& (level.pathFinder.getType(xGrid, yGrid) == PathType.MOVE 
				|| level.pathFinder.getType(xGrid, yGrid) == PathType.HOME)) {  
			int[] menuOptions;
			level.pathFinder.calcAttack(unitChosen.getMinRange(), unitChosen.getMaxRange(), xGrid, yGrid, 2, unitChosen.getTeam());
			if (!level.pathFinder.customMoveIsEmpty()) menuOptions = new int[] {2, 3, 4};
			else menuOptions = new int[] {3, 4};
			level.pathFinder.clearCustomMove();
			level.createTextBox(x + level.getTileSize(), y, menuOptions);
			locked = true;
		}
		//11 && not playable // attacking
		else if (unitChosen != null && unitViewed != null && !unitViewed.isPlayable() 
				&& level.pathFinder.getType(xGrid, yGrid) == PathType.ATTACK) { 
			// If movement was already chosen, just attack from space chosen
			if (pathSet) {
				approveAttack();
				pathSet = false;
			}
			// If movement not chosen, confirm in in-game menu
			else {
				level.createTextBox(x + level.getTileSize(), y, new int[]{2, 4});
				locked = true;
			}
		}
		// 11 && !=  // can't attack or move to team-occupied space
		else {  
			cursorError();
		}
	}
	
	/**
	 * Updates the selected unit's path every time the cursor moves
	 */
	protected void updatePath() {
		
		PathType hoveredTileType = level.pathFinder.getType(xGrid, yGrid);
		Unit unitViewed = level.getUnit(xGrid, yGrid);
		// Movement space: just update recorded tile
		if (hoveredTileType == PathType.MOVE || hoveredTileType == PathType.HOME) {
			level.pathDisplay.setHoveredTile(xGrid, yGrid, unitChosen, level.pathFinder);
		}
		// Attack space: update the path to be in range
		else if (hoveredTileType == PathType.ATTACK && unitViewed != null && !unitViewed.isPlayable()) {
			level.pathDisplay.confirmAttackDistance(xGrid, yGrid, unitChosen, level.pathFinder);
		}
		// Unreachable space: remove record
		else {
			level.pathDisplay.reset();
		}
	}
	
	/**
	 * Move the unit to the recorded space
	 * 		Nothing is input now, since the last tile hovered over is recorded
	 */
	private void moveUnit() {
		int ua = level.pathDisplay.getHoveredTile();
		if (ua == -1) ua = level.pathFinder.prev(xGrid, yGrid); // NOTE - this line shouldn't run; pathDisplay should always be set
		int uXa = ua % level.getWidth();
		int uYa = ua / level.getWidth();
		unitChosen.move(uXa, uYa);
	}
	
	/**
	 * Deselect the currently chosen unit
	 */
	protected void deselectUnit() {
		unitChosen = null;
		level.pathFinder.reset();
		level.pathDisplay.reset();
	}
	
	/**
	 * Locking or hiding the cursor, so it is invisible or cannot be controlled
	 * 		This is done during non-player turns, or while other menus take precedence
	 */
	public void lockAndHide() 		{	locked = true;	invisible = true;	}
	public void unlock() 			{	locked = false;						}
	public void unlockAndUnhide() 	{	locked = false;	invisible = false;	}
	
	/**
	 * Move the chosen unit to the chosen space
	 */
	public void approveMove() {
		moveUnit();
		deselectUnit();
		locked = false;
	}
	
	/**
	 * Attack the chosen enemy with the chosen unit
	 */
	public void approveAttack() {
		Unit unitViewed = level.getUnit(xGrid, yGrid);
		
		// Case 0: Chose move space, now need to choose attack space
		if (unitViewed == null || unitViewed == unitChosen) {  	
			level.pathFinder.calcAttack(unitChosen.getMinRange(), unitChosen.getMaxRange(), xGrid, yGrid, 2, unitChosen.getTeam());
			pathSet = true;
		}
		// Case 1: Chose attack space, complete action
		else {													
			moveUnit();
			unitChosen.attack(unitViewed, level.getTile(xGrid, yGrid).defense());
			deselectUnit();
		}
		
		locked = false;
	}
	
	/**
	 * Check if within the bounds of the map
	 * @param xa	change in x
	 * @param ya	change in y
	 * @return		true if within bounds
	 */
	protected boolean checkInBounds(int xa, int ya) {
		if (level.getTile(xGrid + xa, yGrid + ya).outOfBounds()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Changes the cursor to red to show the user input was invalid
	 */
	protected void cursorError() {
		anim = 25;
		sprite = Sprite.cursorError2;
	}

}
