package com.clay13chopper.game1.room;

import java.util.ArrayList;
import java.util.List;

import com.clay13chopper.game1.entities.Entity;
import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.graphics.Sprite;

public class Room {
	
	protected int width, height;
	protected Entity focus;
	protected int scale = 1;
	
	protected List<Entity> entities = new ArrayList<Entity>();
	
	protected Room changeRoom = null;
	
	public Room() {
		
	}
	
	public void update() {
		
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.update();
			if (e.isRemoved()) {
				entities.remove(i);
			}
		}
		
	}
	
	public void render(Screen screen) {
		
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			Sprite s = e.getSprite();
			int xPos = e.getX() - (s.getWidth() / 2);
			int yPos = e.getY() - (s.getHeight() / 2);
			screen.renderSprite(xPos, yPos, s, false, false);
		}
		
	}
	
	public void add(Entity e) {
		
		entities.add(e);
		e.init(this);
		
	}
	
	public Entity getFocus() {
		return focus;
	}
	
	public int getWidthbyPixel() {
		return width;
	}
	
	public int getHeightbyPixel() {
		return height;
	}
	
	public int getScale() {
		return scale;
	}
	
	public void changeRoom(Room r) {
		changeRoom = r;
	}
	
	/**
	 *  IMPORTANT: This function signals the Game to change rooms
	 *             The name might not make that clear
	 * @return	room to change to
	 */
	public Room getChangeRoom() {
		Room r = changeRoom;
		if (r != null) reset();
		return r;
	}
	
	public void reset() {
		changeRoom = null;
		entities = new ArrayList<Entity>();
	}

}
