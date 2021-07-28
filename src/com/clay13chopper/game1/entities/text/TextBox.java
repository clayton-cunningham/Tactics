package com.clay13chopper.game1.entities.text;

import java.util.ArrayList;
import java.util.List;

import com.clay13chopper.game1.entities.Entity;
import com.clay13chopper.game1.entities.cursors.MapCursor;
import com.clay13chopper.game1.entities.cursors.MenuCursor;
import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.room.StartMenu;
import com.clay13chopper.game1.room.level.Level;

public class TextBox extends Entity {

	private List<Sprite> sprites = new ArrayList<Sprite>();
	private List<Integer> actions = new ArrayList<Integer>();
	private int spriteWidth;
	private int spriteHeight;
	private MenuCursor cursor;
	
	public TextBox(int x, int y, List<Integer> cases, MenuCursor c) {
		spriteWidth = Sprite.menuBorderTop.getWidth();
		spriteHeight = Sprite.menuBorderTop.getHeight();
		this.x = x - spriteWidth;
		this.y = y - spriteHeight;
		sprites.add(Sprite.menuBorderTopLeft);
		sprites.add(Sprite.menuBorderTop);
		sprites.add(Sprite.menuBorderTopRight);
		fillSprites(cases);
		sprites.add(Sprite.menuBorderBottomLeft);
		sprites.add(Sprite.menuBorderBottom);
		sprites.add(Sprite.menuBorderBottomRight);
		if (c != null) {
			cursor = c;
		}
	}
	
	public void update() {
		super.update();
		
		// Check for no signal or return key
		int trigger = cursor.checkTrigger();
		if (trigger == -1) {
			return;
		}
		else if (trigger == -2) {
			cursor.remove();
			this.remove();
			((MapCursor) room.getFocus()).unlock();
			return;
		}
		
		int action = actions.get(trigger);
		doAction(action);
	}
	
	public void render(Screen screen) {
		int xa = 0;
		int ya = 0;
		for (Sprite s : sprites) {
			screen.renderSprite(x + (xa * spriteWidth), y + (ya * spriteHeight), s, false, false);
			xa = (xa + 1) % 3;
			if (xa == 0) ya++;
		}
	}
	
	private void fillSprites(List<Integer> cases) {
		if (cases.contains(0)) add(0, Sprite.menuEndTurn);
		if (cases.contains(1)) add(1, Sprite.menuQuit);
		if (cases.contains(2)) add(2, Sprite.menuAttack);
		if (cases.contains(3)) add(3, Sprite.menuWait);
		if (cases.contains(4)) add(4, Sprite.menuCancel);
	}
	
	private void add(int key, Sprite s) {
		sprites.add(Sprite.menuBorderLeft);
		sprites.add(s);
		sprites.add(Sprite.menuBorderRight);
		actions.add(key);
	}
	
	// Performs an action based on the trigger
	private void doAction(int action) {
		if (action == 0) {
			cursor.remove();
			this.remove();
			((MapCursor) room.getFocus()).unlock();
			((Level) room).nextTurn();
		}
		else if (action == 1) {
			room.changeRoom(new StartMenu());
		}
		else if (action == 2) {
			cursor.remove();
			this.remove();
			((MapCursor) room.getFocus()).approveAttack();
		}
		else if (action == 3) {
			cursor.remove();
			this.remove();
			((MapCursor) room.getFocus()).approveMove();
		}
		else if (action == 4) {
			cursor.remove();
			this.remove();
			((MapCursor) room.getFocus()).unlock();
		}
	}

}
