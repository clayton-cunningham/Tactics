package com.clay13chopper.game1.entities.displays;

import java.util.ArrayList;
import java.util.List;

import com.clay13chopper.game1.entities.cursors.MapCursor;
import com.clay13chopper.game1.entities.cursors.MenuCursor;
import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.room.StartMenu;
import com.clay13chopper.game1.room.level.Level;

/**
 * Shows a menu of in-game actions.  This includes:
 * 		Attack; Wait (Move); End turn; Quit; Cancel (closes menu);
 * @author Clayton Cunningham
 *
 */
public class TextMenu extends TextBox {

	private List<Sprite> sprites = new ArrayList<Sprite>();
	private List<Integer> actions = new ArrayList<Integer>();
	private MenuCursor cursor;
	
	public TextMenu(int x, int y, List<Integer> cases, MenuCursor c) {
		spriteWidth = Sprite.menuBorderTop.getWidth();
		spriteHeight = Sprite.menuBorderTop.getHeight();
		boxWidth = 3;
		boxHeight = cases.size();
		this.x = x;
		this.y = y;
		fillBoxSprites();
		fillSprites(cases);
		if (c != null) {
			cursor = c;
		}
	}
	
	public void update() {
		super.update();
		
		// Check for no action to perform (nothing selected yet, or action not implemented)
		int trigger = cursor.checkTrigger();
		if (trigger == -1) {
			return;
		}
		// Check if player input to go back
		else if (trigger == -2) {
			cursor.remove();
			this.remove();
			((MapCursor) room.getFocus()).unlock();
			return;
		}
		
		// Perform menu action selected
		int action = actions.get(trigger);
		doAction(action);
	}
	
	public void render(Screen screen) {

		renderBox(screen, x, y);
		
		int xa = 0; // X is currently unused, but held in case we implement a menu with horizontal choice movement 
		int ya = 0;
		for (Sprite s : sprites) {
			screen.renderSprite(x + (xa * spriteWidth), y + (ya * spriteHeight), s, false, false);
			ya++;
		}
	}
	
	/**
	 * Parse input for what actions to add to the menu
	 * @param cases		Integers representing which actions to add to menu
	 */
	private void fillSprites(List<Integer> cases) {
		if (cases.contains(0)) add(0, Sprite.menuEndTurn);
		if (cases.contains(1)) add(1, Sprite.menuQuit);
		if (cases.contains(2)) add(2, Sprite.menuAttack);
		if (cases.contains(3)) add(3, Sprite.menuWait);
		if (cases.contains(4)) add(4, Sprite.menuCancel);
	}
	
	/**
	 * Add an action to the menu
	 * @param key	integer representation for action
	 * @param s		Sprite of action
	 */
	private void add(int key, Sprite s) {
		sprites.add(s);
		actions.add(key);
	}
	
	/**
	 * Performs an action based on the trigger
	 * @param action	action to perform
	 */
	private void doAction(int action) {
		// End Turn
		if (action == 0) {
			cursor.remove();
			this.remove();
			((MapCursor) room.getFocus()).unlock();
			((Level) room).nextTurn();
		}
		// Quit
		else if (action == 1) {
			room.changeRoom(new StartMenu());
		}
		// Attack
		else if (action == 2) {
			cursor.remove();
			this.remove();
			((MapCursor) room.getFocus()).approveAttack();
		}
		// Wait
		else if (action == 3) {
			cursor.remove();
			this.remove();
			((MapCursor) room.getFocus()).approveMove();
		}
		// Cancel
		else if (action == 4) {
			cursor.remove();
			this.remove();
			((MapCursor) room.getFocus()).unlock();
		}
	}

}
