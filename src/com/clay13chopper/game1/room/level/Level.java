package com.clay13chopper.game1.room.level;

import java.util.ArrayList;

/**
 * Definition:  Collection of all objects in a level
 * 				Assists running of every object's function, such as updating and rendering
 */

import java.util.Arrays;
import java.util.Iterator;

import com.clay13chopper.game1.graphics.PathDisplay;
import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.processors.PathFinder;
import com.clay13chopper.game1.room.Room;
import com.clay13chopper.game1.tiles.Tile;
import com.clay13chopper.game1.entities.Entity;
import com.clay13chopper.game1.entities.cursors.MapCursor;
import com.clay13chopper.game1.entities.cursors.MenuCursor;
import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.entities.mob.Unit.Team;
import com.clay13chopper.game1.entities.displays.GenericUI;
import com.clay13chopper.game1.entities.displays.TextMenu;
import com.clay13chopper.game1.entities.displays.WinLose;

//Can have two types of levels - random generation, and planned data
public abstract class Level extends Room {

	public static Level levelRandom = new RandomLevel(10, 10);
	public static Level level1 = new LoadedLevel("/levels/map1.png", "/levels/entities1.png");
	public static Level level2 = new LoadedLevel("/levels/map2.png", "/levels/entities2.png");
	public static Level level3 = new LoadedLevel("/levels/map3.png", "/levels/entities3.png");
//	public static Level levelTest = new LoadedLevel("/levels/mapTest.png", "/levels/entitiesTest.png");
	public static Level levelTest = new RandomLevel(10, 10);

	public PathFinder pathFinder;
	public PathDisplay pathDisplay;
	protected int[] tiles;
	protected static int TILE_SIZE;
	protected static int TILE_SIZE_SHIFT;
	protected Unit[] locations;
	protected Team activeTeam;
	protected int[] teamNumAlive;
	protected int levelComplete;
	protected Unit unitActing;
	protected ArrayList<Entity> queuedAdd;
	
	public Level() {
		super();
		activeTeam = Team.BLUE;
		teamNumAlive = new int[Unit.Team.values().length];
		Arrays.fill(teamNumAlive, 0);
		levelComplete = 0;
		queuedAdd = new ArrayList<Entity>();
		scale = 6;
		TILE_SIZE_SHIFT = 4;
		TILE_SIZE = (int) Math.pow(2, TILE_SIZE_SHIFT);
	}
	
	public void update() {
		boolean nextTurn = true;
		// We need to use an iterator to safely remove from the list in this loop
		for (Iterator<Entity> iterator = entities.iterator(); iterator.hasNext();) {
			Entity e = iterator.next();
			e.update();
			if (e.isRemoved()) {
				iterator.remove();
				if (e instanceof Unit) removeUnit((Unit)e);
				if (levelComplete != 0) return;
			}
			else if (e instanceof Unit && ((Unit) e).getTeam() == activeTeam && !((Unit) e).getTurnDone()) 
				nextTurn = false;
		}
		if (nextTurn) {
			nextTurn();
		}
		if (!queuedAdd.isEmpty()) {
			for (Entity e : queuedAdd) add(e);
			queuedAdd.clear();
		}
	}
	
	public void render(Screen screen) {
		int xOff = Screen.getXOffset();
		int yOff = Screen.getYOffset();
		int x0 = xOff >> TILE_SIZE_SHIFT;
		int y0 = yOff >> TILE_SIZE_SHIFT;
		int xF = ((Screen.getWidth() - 1) + xOff) >> TILE_SIZE_SHIFT;
		int yF = ((Screen.getHeight() - 1) + yOff) >> TILE_SIZE_SHIFT;
		
		// Display background
		for (int y = y0; y <= yF; y++) {
			for (int x = x0; x <= xF; x++) {
				/**
				 * Works by using the (address / 16) to get the tile from the array.
				 * Since we need that coordinate, he uses (that * 16) to get the top left corner of the tile location
				 */
				getTile(x, y).render(x << TILE_SIZE_SHIFT, y << TILE_SIZE_SHIFT, 
						screen, pathFinder.getType(x, y), pathFinder.getEnemyShown()); 
			}
		}
		
		// Display player's path
		pathDisplay.render(TILE_SIZE, screen);
		
		// Display entities
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(screen);
		}
	}
	
	public Tile getTile(int x, int y) {
		return Tile.voidTile;
	}
	
	// Add an entity to the level
	public void add(Entity e) {
		
		entities.add(e);
		e.init(this);
		
		//If is a unit, place in the array
		if (e instanceof Unit) {
			Unit u = (Unit) e;
			locations[u.getXGrid() + (u.getYGrid() * width)] = u;
			teamNumAlive[ u.getTeam().getId() ]++;
		}
		
	}
	
	// Creates a text box with the options requested
	public void createTextBox(int x, int y, int[] options) {

		ArrayList<Integer> aL = new ArrayList<Integer>();
		for (int i = 0; i < options.length; i++) {
			aL.add(options[i]);
		}
		// TODO: this address calculation is specific to the in-game menu.  Move the formula or change the function name

		int screenMaxX = Screen.getWidth() + Screen.getXOffset();
		int screenMaxY = Screen.getHeight() + Screen.getYOffset();
		
		int menuWidth = Sprite.menuAttack.getWidth();
		int menuHeight = Sprite.menuBorderTop.getHeight() * options.length;
		if (x + menuWidth > screenMaxX) x -= (menuWidth + (2 * TILE_SIZE));
		if (y + menuHeight > screenMaxY) y -= menuHeight;
		
		MenuCursor menu = new MenuCursor(x, y, aL.size(), 1);
		scheduleAdd(new TextMenu(x, y, aL, menu));
		scheduleAdd(menu);
	}
	
	// Queue adding an entity to the level
	public void scheduleAdd(Entity e) {
		queuedAdd.add(e);
	}
	
	public void moveUnit(Unit u, int x, int y, int xa, int ya) {
		locations[x + (y * width)] = null;
		locations[xa + (ya * width)] = u;
	}
	
	public void removeUnit(Unit u) {
		int x = u.getXGrid();
		int y = u.getYGrid();
		locations[x + (y * width)] = null;
		
		// Check if the level is complete
		int teamId = u.getTeam().getId();
		teamNumAlive[ teamId ]--;
		if (teamNumAlive[ teamId ] < 1) {
			if (teamId != 0) {
				levelComplete = 1;
				add(new WinLose(1, getWidthbyPixel() / 2, getHeightbyPixel() / 2));
				add(new GenericUI(getWidthbyPixel() / 2, getHeightbyPixel() / 2 + 32, Sprite.winContinue));
				entities.remove(focus); // TODO: This is lazy, find another way to pause the level
			}
			else {
				levelComplete = -1;
				add(new WinLose(-1, getWidthbyPixel() / 2, getHeightbyPixel() / 2));
				add(new GenericUI(getWidthbyPixel() / 2, getHeightbyPixel() / 2 + 32, Sprite.loseContinue));
			}
			entities.remove(focus);
		}
	}
	
	public Unit getUnit(int x, int y) {
		return locations[ x + (y * width)];
	}
	
	public Team getActiveTeam() {
		return activeTeam;
	}
	
	private Team getNextTeam() {
		if (activeTeam == Team.BLUE) return Team.RED;
		return Team.BLUE;
	}
	
	public void nextTurn() {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e instanceof Unit && ((Unit) e).getTurnDone()) {
				((Unit) e).resetTurn();
			}
		}
		activeTeam = getNextTeam();
		if (activeTeam != Team.BLUE) ((MapCursor) focus).lockAndHide();
		else ((MapCursor) focus).unlockAndUnhide();
	}
	
	public void setUnitActing(Unit u) {
		unitActing = u;
	}
	
	public Unit getUnitActing() {
		return unitActing;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidthbyPixel() {
		return width * TILE_SIZE;
	}
	
	public int getHeightbyPixel() {
		return height * TILE_SIZE;
	}
	
	public int getTileSize() {
		return TILE_SIZE;
	}
	
	public int getShift() {
		return TILE_SIZE_SHIFT;
	}
	
	public int getLevelComplete() {
		return levelComplete;
	}
	
	protected int iToX(int i) {
		return (i % width) * TILE_SIZE + (TILE_SIZE / 2);
	}
	
	protected int iToY(int i) {
		return (i / width) * TILE_SIZE + (TILE_SIZE / 2);
	}
	
	public void reset() {
		super.reset();
		Arrays.fill(locations, null);
		unitActing = null;
		activeTeam = Team.BLUE;
		Arrays.fill(teamNumAlive, 0);
		levelComplete = 0;
		queuedAdd.clear();
	}

}
