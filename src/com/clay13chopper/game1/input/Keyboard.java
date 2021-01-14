package com.clay13chopper.game1.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

	private static boolean[] keys = new boolean[120];
	private static boolean up, down, left, right;
	private static boolean z, x;

	public void update() {
		up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		z = keys[KeyEvent.VK_Z];
		x = keys[KeyEvent.VK_X];
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}
	
	public static boolean getUp() {
		return up;
	}
	

	public static boolean getDown() {
		return down;
	}
	

	public static boolean getLeft() {
		return left;
	}
	

	public static boolean getRight() {
		return right;
	}

	public static boolean getSelect() {
		return z;
	}

	public static boolean getDeselect() {
		return x;
	}
	
}
