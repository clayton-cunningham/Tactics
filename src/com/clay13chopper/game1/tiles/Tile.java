package com.clay13chopper.game1.tiles;

import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.processors.PathFinder.PathType;

/**
 * UI parent class detailing properties of level tiles
 * 		(tiles are individual space on the map, like a chess board)
 * @author Clayton Cunningham
 *
 */
public abstract class Tile {

	protected Sprite sprite;
	
	public final static Tile grass0 	= new GrassTile		(Sprite.grass0);
	public final static Tile grass1 	= new GrassTile		(Sprite.grass1);
	public final static Tile grass2 	= new GrassTile		(Sprite.grass2);
	public final static Tile snow 		= new SnowTile		(Sprite.snow);
	public final static Tile ice 		= new IceTile		(Sprite.ice);
	public final static Tile mountain 	= new MountainTile	(Sprite.mountain);
	public final static Tile water 		= new GrassTile		(Sprite.water);
	public final static Tile sand 		= new SandTile		(Sprite.sand);
	
	public final static Tile voidTile 	= new VoidTile		(Sprite.voidSprite);

	public final static int colGrass 	= 0xff00ff00;
	public final static int colSnow 	= 0xffffffff;
	public final static int colIce 		= 0xff00ffdd;
	public final static int colMountain = 0xff7f7f00;
	public final static int colWater 	= 0xff0000ff;
	public final static int colSand 	= 0xffffff00;

	public Tile(Sprite s) {
		sprite = s;
	}

	public void render(int x, int y, Screen screen, PathType type, boolean enemyShown) {
		screen.renderSprite(x, y, this.sprite, false, false);
		
		// Render the path, if anything exists here
		if 		(enemyShown && type == PathType.MOVE) 	screen.renderSprite(x, y, Sprite.enemyMoveSpace, false, false);
		else if (enemyShown && type == PathType.ATTACK) screen.renderSprite(x, y, Sprite.attackSpace, false, false);
		else if (			   type == PathType.MOVE) 	screen.renderSprite(x, y, Sprite.moveSpace, false, false);
		else if (			   type == PathType.ATTACK) screen.renderSprite(x, y, Sprite.attackSpace, false, false);
	}
	
	public boolean solid() 			{	return false;	}	
	public boolean outOfBounds() 	{	return false;	}
	public boolean breakable() 		{	return false;	}

	public int defense()  	{	return 0; }

	public int moveCostFoot()  {	return 1; }
	public int moveCostWheel() {	return 1; }
	public int moveCostHorse() {	return 1; }
	
	public Sprite getSprite() 	{	return sprite;	}

}
