package com.clay13chopper.game1.room;

import com.clay13chopper.game1.entities.cursors.MenuCursor;
import com.clay13chopper.game1.entities.text.LevelSelector;
import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.graphics.Sprite;

public class StartMenu extends Room {
	
	protected Sprite background;
	protected String[] levels;
	
	public StartMenu() {
		background = Sprite.background_start;
		levels = new String[] {"Level 1", "Level 2", "Level 3"};
		width = 1920;
		height = 1080;
		scale = 1;
		add(new LevelSelector(960, 250, Sprite.level1));
		add(new LevelSelector(960, 500, Sprite.level2));
		add(new LevelSelector(960, 750, Sprite.level3));
		focus = new MenuCursor(750, 250, levels.length);
		add(focus);
	}
	
	public void update() {
		
		super.update();
		
	}
	
	public void render(Screen screen) {
		screen.renderSprite(0, 0, background, false, false);
		
		super.render(screen);
		
	}
	
	

}
