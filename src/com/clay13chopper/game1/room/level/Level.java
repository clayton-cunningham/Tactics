package com.clay13chopper.game1.room.level;

/**
 * Definition:  Collection of all objects in a level
 * 				Assists running of every object's function, such as updating and rendering
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.graphics.Sprite;
import com.clay13chopper.game1.processors.PathFinder;
import com.clay13chopper.game1.room.Room;
import com.clay13chopper.game1.tiles.Tile;
import com.clay13chopper.game1.entities.Cursor;
import com.clay13chopper.game1.entities.Entity;
import com.clay13chopper.game1.entities.WinLose;
import com.clay13chopper.game1.entities.mob.Unit;
import com.clay13chopper.game1.entities.mob.Unit.Team;

//Can have two types of levels - random generation, and planned data
public abstract class Level extends Room {

	public static Level random = new RandomLevel(10, 10);
	public static Level level1 = new LoadedLevel("/levels/map1.png");
	public static Level level2 = new LoadedLevel("/levels/map2.png");
	
	public PathFinder pathFinder;
	protected int[] tiles;
	protected Unit[] locations;
	protected Team activeTeam;
	protected int[] teamNumAlive;
	protected int levelComplete;
	protected Unit unitActing;

	protected List<Entity> entities = new ArrayList<Entity>();
	
	public Level() {
		focus = new Cursor(8,8);
		add(focus);
		activeTeam = Team.BLUE;
		teamNumAlive = new int[Unit.Team.values().length];
		Arrays.fill(teamNumAlive, 0);
		levelComplete = 0;
	}
	
	public void update() {
		if (levelComplete != 0) return;
		boolean nextTurn = true;
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.update();
			if (e.isRemoved()) {
				entities.remove(i);
				if (e instanceof Unit) removeUnit((Unit)e);
				if (levelComplete != 0) return;
			}
			else if (e instanceof Unit && ((Unit) e).getTeam() == activeTeam && !((Unit) e).getTurnDone()) 
				nextTurn = false;
		}
		if (nextTurn) {
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				if (e instanceof Unit && ((Unit) e).getTurnDone()) {
					((Unit) e).resetTurn();
				}
			}
			activeTeam = getNextTeam();
		}
	}
	
	public void render(Screen screen) {
		int xOff = screen.getXOffset();
		int yOff = screen.getYOffset();
		int x0 = xOff >> 4;
		int y0 = yOff >> 4;
		int xF = ((screen.getWidth() - 1) + xOff) >> 4;
		int yF = ((screen.getHeight() - 1) + yOff) >> 4;
		
		for (int y = y0; y <= yF; y++) {
			for (int x = x0; x <= xF; x++) {
				/**
				 * Works by using the (address / 16) to get the tile from the array.
				 * Since we need that coordinate, he uses (that * 16) to get the top left corner of the tile location
				 */
				getTile(x, y).render(x, y, screen, pathFinder.getType(x, y));
			}
		}
		
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			Sprite s = e.getSprite();
			int xPos = e.getX() - (s.getWidth() / 2);
			int yPos = e.getY() - (s.getHeight() / 2);
			screen.renderSprite(xPos, yPos, s, false, false);
			if (e instanceof Unit && ((Unit) e).getHealthPercent() < 100) {
				screen.renderSprite(xPos + 1, yPos + 13, Sprite.healthBar, false, false);
				screen.renderSprite(xPos + 2, yPos + 14, Sprite.makeHealth((Unit) e), false, false);
			}
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
			locations[(e.getX() >> 4) + ((e.getY() >> 4) * width)] = (Unit) e;
			teamNumAlive[ ((Unit) e).getTeam().getId() ]++;
			
		}
		
	}
	
	public void moveUnit(Unit u, int x, int y, int xa, int ya) {
		locations[x + (y * width)] = null;
		locations[xa + (ya * width)] = u;
	}
	
	public void removeUnit(Unit u) {
		int x = u.getX() >> 4;
		int y = u.getY() >> 4;
		locations[x + (y * width)] = null;
		int teamId = u.getTeam().getId();
		teamNumAlive[ teamId ]--;
		if (teamNumAlive[ teamId ] < 1) {
			if (teamId != 0) {
				levelComplete = 1;
				add(new WinLose(1, (width << 4) / 2, (height << 4) / 2));
			}
			else {
				levelComplete = -1;
				add(new WinLose(-1, (width << 4) / 2, (height << 4) / 2));
			}
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
	
	public void setUnitActing(Unit u) {
		unitActing = u;
	}
	
	public Unit getUnitActing() {
		return unitActing;
	}

}
