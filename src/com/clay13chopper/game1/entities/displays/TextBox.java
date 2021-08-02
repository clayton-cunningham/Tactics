package com.clay13chopper.game1.entities.displays;

import java.util.ArrayList;
import java.util.List;

import com.clay13chopper.game1.entities.Entity;
import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.graphics.Sprite;

public class TextBox extends Entity {

	protected List<Sprite> boxSprites = new ArrayList<Sprite>();
	protected int boxWidth, boxHeight;
	protected int spriteWidth;
	protected int spriteHeight;
	
	// Renders a text box for in-game UI
	protected void renderBox(Screen screen, int x, int y) {

		int xa = 0;
		int ya = 0;
		for (Sprite s : boxSprites) {
			screen.renderSprite(x + ((xa - 1) * spriteWidth), y + ((ya - 1) * spriteHeight), s, false, false);
			xa = (xa + 1) % (boxWidth + 2);
			if (xa == 0) ya++;
		}
		
	}

	// Adds the menu sprites to an array for rendering later.  Order matters!
	protected void fillBoxSprites() {
		
		boxSprites.clear();

		boxSprites.add(Sprite.menuBorderTopLeft);
		for (int i = 0; i < boxWidth; i++) {
			boxSprites.add(Sprite.menuBorderTop);
		}
		boxSprites.add(Sprite.menuBorderTopRight);

		for (int j = 0; j < boxHeight; j++) {
			boxSprites.add(Sprite.menuBorderLeft);
			for (int i = 0; i < boxWidth; i++) {
				boxSprites.add(Sprite.menuBlank);
			}
			boxSprites.add(Sprite.menuBorderRight);
		}
		
		boxSprites.add(Sprite.menuBorderBottomLeft);
		for (int i = 0; i < boxWidth; i++) {
			boxSprites.add(Sprite.menuBorderBottom);
		}
		boxSprites.add(Sprite.menuBorderBottomRight);
	}

}
