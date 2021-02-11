package com.clay13chopper.game1.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

	private static boolean[] keys = new boolean[120];
	private static boolean up, down, left, right;
	private static boolean z, x;
	private static boolean upOld, downOld, leftOld, rightOld;
	private static boolean zOld, xOld;
	private static boolean upStart, downStart, leftStart, rightStart;
	private static boolean zStart, xStart;

	public void update() {
		setKeys();
		setKeysStart();
		setOldKeys();
	}
	
	private void setKeys() {
		up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		z = keys[KeyEvent.VK_Z];
		x = keys[KeyEvent.VK_X];
	}
	
	private void setKeysStart() {
		if (!upOld && up) upStart = true; 			else upStart = false;
		if (!downOld && down) downStart = true; 	else downStart = false;
		if (!leftOld && left) leftStart = true; 	else leftStart = false;
		if (!rightOld && right) rightStart = true;	else rightStart = false;
		if (!zOld && z) zStart = true; 				else zStart = false;
		if (!xOld && x) xStart = true; 				else xStart = false;
	}
	
	private void setOldKeys() {
		upOld = up;
		downOld = down;
		leftOld = left;
		rightOld = right;
		zOld = z;
		xOld = x;
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
	
	public static boolean getUpStart() {
		return upStart;
	}
	

	public static boolean getDownStart() {
		return downStart;
	}
	

	public static boolean getLeftStart() {
		return leftStart;
	}
	

	public static boolean getRightStart() {
		return rightStart;
	}

	public static boolean getSelectStart() {
		return zStart;
	}

	public static boolean getDeselectStart() {
		return xStart;
	}
	
}
