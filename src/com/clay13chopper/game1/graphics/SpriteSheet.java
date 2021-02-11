package com.clay13chopper.game1.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteSheet {
	
	private final String path;
	private final int[] pixels;
	private final int width, height;

	//int is (sprites * pixels) = 8 * 16 = 128
	public final static SpriteSheet background_tiles = new SpriteSheet("/spritesheets/background_tiles.png", 128, 128);
	public final static SpriteSheet example_player = new SpriteSheet("/spritesheets/rpg_16x16.png", 128, 144);
	public final static SpriteSheet example_player_done = new SpriteSheet("/spritesheets/rpg_16x16_done.png", 128, 144);
	public final static SpriteSheet projectiles1 = new SpriteSheet("/spritesheets/projectiles1.png", 48, 48);
	public final static SpriteSheet cursors = new SpriteSheet("/spritesheets/cursors.png", 48, 48);
	public final static SpriteSheet cursorsLarge = new SpriteSheet("/spritesheets/cursorsLarge.png", 240, 240);
	public final static SpriteSheet UI = new SpriteSheet("/spritesheets/UI.png", 48, 48);
	public final static SpriteSheet startMenu = new SpriteSheet("/spritesheets/startMenu.png", 320, 564);
	
	// Backgrounds - maybe change to different class?
	public final static SpriteSheet background_start = new SpriteSheet("/spritesheets/background_start.png", 1920, 1080);
	
	public SpriteSheet(String p, int w, int h) {
		path = p;
		width = w;
		height = h;
		pixels = new int[getWidth() * height];
		load();
	}

	//Used once to get the sprite sheet
	private void load() {
		try {
			System.out.print("Trying to load: " + path + "...");
			BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));
			image.getRGB(0, 0, getWidth(), height, pixels, 0, getWidth());
			System.out.println(" succeeded!");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println(" failed!");
		}

	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getPixel(int position) {
		return pixels[position];
	}
}
