package com.clay13chopper.game1.tiles;

import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.processors.PathFinder.PathType;

public abstract class Tile {

	protected Sprite sprite;
	protected Sprite spriteMovable;
	protected int spriteScale = 1;
	
	public final static Tile grass0 = new GrassTile(Sprite.grass0);
	public final static Tile grass1 = new GrassTile(Sprite.grass1);
	public final static Tile grass2 = new GrassTile(Sprite.grass2);
	public final static Tile snow = new GrassTile(Sprite.snow);
	public final static Tile ice = new IceTile(Sprite.ice);
	public final static Tile mountain = new MountainTile(Sprite.mountain);
	public final static Tile water = new GrassTile(Sprite.water);
	public final static Tile sand = new GrassTile(Sprite.sand);
	
	public final static Tile voidTile = new VoidTile(Sprite.voidSprite);

	public final static int colGrass = 0xff00ff00;
	public final static int colSnow = 0xffffffff;
	public final static int colIce = 0xff00ffdd;
	public final static int colMountain = 0xff7f7f00;
	public final static int colWater = 0xff0000ff;
	public final static int colSand = 0xffffff00;

	public Tile(Sprite s) {
		sprite = s;
	}

	public void render(int x, int y, Screen screen, PathType type) {
		screen.renderSprite(x << 4, y << 4, this.sprite, false, false);
		if (type == PathType.MOVE) screen.renderSprite(x << 4, y << 4, Sprite.moveSpace, false, false);
		else if (type == PathType.ATTACK) screen.renderSprite(x << 4, y << 4, Sprite.attackSpace, false, false);
	}
	
	public boolean solid() {
		return false;
	}
	
	public boolean breakable() {
		return false;
	}

}
