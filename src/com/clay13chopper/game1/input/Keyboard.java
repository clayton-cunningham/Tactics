package com.clay13chopper.game1.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Implementation of a Keyboard listener
 * 		Notes the input of several keys,
 * 		including the start/stop of some as well
 * @author Clayton Cunningham
 *
 */
public class Keyboard implements KeyListener {

	private static boolean[] keys = new boolean[120];
	private static boolean up, down, left, right;
	private static boolean z, x;
	private static boolean upOld, downOld, leftOld, rightOld;
	private static boolean zOld, xOld;
	private static boolean upStart, downStart, leftStart, rightStart;
	private static boolean zStart, xStart;
	private static boolean zRelease, xRelease;

	public void update() {
		setKeys();
		setKeysStart();
		setKeysRelease();
		setOldKeys();
	}
	
	/**
	 * Detects whether a key is being pressed
	 */
	private void setKeys() {
		up 		= keys[KeyEvent.VK_UP] 		|| keys[KeyEvent.VK_W];
		down 	= keys[KeyEvent.VK_DOWN] 	|| keys[KeyEvent.VK_S];
		left 	= keys[KeyEvent.VK_LEFT] 	|| keys[KeyEvent.VK_A];
		right 	= keys[KeyEvent.VK_RIGHT] 	|| keys[KeyEvent.VK_D];
		z 		= keys[KeyEvent.VK_Z];
		x 		= keys[KeyEvent.VK_X];
	}
	
	/**
	 * Detects the beginning of a key press
	 */
	private void setKeysStart() {
		if (!upOld && up) 		upStart = true; 		else upStart = false;
		if (!downOld && down) 	downStart = true; 		else downStart = false;
		if (!leftOld && left) 	leftStart = true; 		else leftStart = false;
		if (!rightOld && right) rightStart = true;		else rightStart = false;
		if (!zOld && z) 		zStart = true; 			else zStart = false;
		if (!xOld && x) 		xStart = true; 			else xStart = false;
	}
	
	/**
	 * Detects the end of a key press
	 */
	private void setKeysRelease() {
		if (zOld && !z) 		zRelease = true; 		else zRelease = false;
		if (xOld && !x) 		xRelease = true; 		else xRelease = false;
	}
	
	/**
	 * Records the key presses to be used for reference during the next update
	 */
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
	
	// Get methods
	public static boolean getUp() 				{	return up;			}
	public static boolean getDown() 			{	return down;		}
	public static boolean getLeft() 			{	return left;		}
	public static boolean getRight() 			{	return right;		}
	public static boolean getSelect() 			{	return z;			}
	public static boolean getDeselect() 		{	return x;			}
	public static boolean getUpStart() 			{	return upStart;		}
	public static boolean getDownStart() 		{	return downStart;	}
	public static boolean getLeftStart() 		{	return leftStart;	}
	public static boolean getRightStart() 		{	return rightStart;	}
	public static boolean getSelectStart() 		{	return zStart;		}
	public static boolean getDeselectStart() 	{	return xStart;		}
	public static boolean getSelectRelease() 	{	return zRelease;	}
	public static boolean getDeselectRelease() 	{	return xRelease;	}
	
}
