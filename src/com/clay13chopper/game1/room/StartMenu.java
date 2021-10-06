package com.clay13chopper.game1.room;

import com.clay13chopper.game1.entities.cursors.MenuCursor;
import com.clay13chopper.game1.entities.displays.GenericUI;
import com.clay13chopper.game1.entities.displays.LevelSelector;
import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.room.level.Level;

/**
 * A screen which displays controls and several levels to choose from
 * @author Clayton Cunningham
 *
 */
public class StartMenu extends Room {
	
	protected Sprite background;
	protected String[] levels;
	
	public StartMenu() {
		background = Sprite.background_start;
		levels = new String[] {"Level 1", "Level 2", "Level 3", "Test"};
		width = 1920;
		height = 1080;
		scale = 1;
		add(new LevelSelector(960, 250, Sprite.level1));
		add(new LevelSelector(960, 500, Sprite.level2));
		add(new LevelSelector(960, 750, Sprite.level3));
		add(new LevelSelector(960, 1000, Sprite.levelTest));
		add(new GenericUI(350, 500, Sprite.controls));
		focus = new MenuCursor(750, 250, levels.length, 0);
		add(focus);
	}
	
	public void update() {
		
		super.update();
		
		// Check any user selection to potentially move to a level
		int currSelect = ((MenuCursor) focus).checkTrigger();
		if (currSelect == 0) changeRoom(Level.level1);
		if (currSelect == 1) changeRoom(Level.level2);
		if (currSelect == 2) changeRoom(Level.level3);
		if (currSelect == 3) changeRoom(Level.levelTest);
		
	}
	
	public void render(Screen screen) {
		screen.renderSprite(0, 0, background, false, false);
		
		super.render(screen);
		
	}
	
	

}
