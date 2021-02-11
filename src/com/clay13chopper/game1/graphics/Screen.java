package com.clay13chopper.game1.graphics;

import com.clay13chopper.game1.entities.Entity;

/**
 * Definition:	Screen does not have access to most other objects.
 * 				Most actions here require input from elsewhere to specify what to show.
 * 				There may be some exceptions (such as player input).
 */

public class Screen {
	
	private int width, height;
	private int[] pixels;
	private int xOffset = 0, yOffset = 0;
	
	public Screen(int w, int h) {
		setWidth(w);
		setHeight(h);
		pixels = new int[w * h];
	}
	
	public void update(Entity focus) {
		if (focus == null) return;
		
		int x = focus.getX();
		int y = focus.getY();
		int sW = focus.getSprite().getWidth();
		int sH = focus.getSprite().getHeight();
		
		if ((y - (sH / 2)) < yOffset) yOffset -= 2;
		if ((y + (sH / 2)) > (yOffset + height)) yOffset += 2;
		if ((x + (sW / 2)) > (xOffset + width)) xOffset += 2;
		if ((x - (sW / 2)) < xOffset) xOffset -= 2;
	}
	
	/**
	 * xMap & yMap are the map coordinates of the top left corner of the sprite (not offset!)
	 * xOffset & yOffset are the top left coordinates of what we should be seeing
	 * xFlip is whether we should flip the sprite horizontally
	 * yFlip is whether we should flip the sprite vertically
	 * 
	 * We take the map location and subtract the offset, because we want everything on 
	 * the map to move opposite of the player's movements.  Then we go through every x for every y
	 * of the sprite in that location, starting at the top left.  If any point is off screen, we skip it.
	 */
	public void renderSprite(int xMap, int yMap, Sprite sprite, boolean xFlip, boolean yFlip) {

		/**
		 * x & y are the current pixel in the sprite
		 * xx & yy are used so x & y are not changed for their loops
		 * xScreen & yScreen are the current pixels on the screen we are setting
		 */
		for (int y = 0; y < sprite.getHeight(); y++) {
			int yScreen = y + yMap - yOffset;
			if (yScreen < 0 || yScreen >= getHeight()) continue;
			
			for (int x = 0; x < sprite.getWidth(); x++) {
				int xScreen = x + xMap - xOffset;
				if (xScreen < 0 || xScreen >= getWidth()) continue;
				
				//Flip?
				int xx = x;
				if (xFlip) xx = (sprite.getWidth() - 1) - xx;
				int yy = y;
				if (yFlip) yy = (sprite.getHeight() - 1) - yy;
				
				//Copy
				int col = sprite.getPixel(xx + (yy * sprite.getWidth()));
				if (col != 0x000000) pixels[xScreen + (yScreen * getWidth())] = col;
			}
		}
	}

	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	
	public void setOffset(int xOff, int yOff) {
		xOffset = xOff;
		yOffset = yOff;
	}
	
	public int getXOffset() {
		return xOffset;
	}
	
	public int getYOffset() {
		return yOffset;
	}
	
	public int getPixel(int i) {
		return pixels[i];
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
