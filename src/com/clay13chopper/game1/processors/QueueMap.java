package com.clay13chopper.game1.processors;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Map of Queues, with each queue is mapped
 * 		to an Integer value.
 * Purpose is to match with PathFinder, and map 
 * 		movement values to corresponding board locations.
 * @author Clayton Cunningham
 *
 * @param <E> 
 */
public class QueueMap<E> {
	
	private HashMap<Integer, Queue<E>> map;
	private final int MINLIMIT = -6;  //Minimum allowed key
	private int highKey = MINLIMIT;  
	
	public QueueMap() {
		map = new HashMap<Integer, Queue<E>>();
	}
	
	/**
	 * Add a value to the queue for the given Integer value
	 * If no queue exists, one will be created and added to the map.
	 * If the key is higher than previous keys, that new highKey is saved
	 * 		If a key is less than or equal to the MINLIMIT (shown above), 
	 * 		it will not be accepted
	 * @param key	Key to add to
	 * @param val	Value to add
	 */
	public void add(Integer key, E val) {
		if (key <= MINLIMIT) return;
		Queue<E> queue;
		if (!map.containsKey(key)) {
			queue = new LinkedList<E>();
			map.put(key, queue);
		}
		else {
			queue = map.get(key);
		}
		queue.add(val);
		if (highKey < key) highKey = key;
	}
	
	/**
	 * Grab the next value in the queue for the current highest key.
	 * This will remove that value.
	 * @return	the next value
	 */
	public E next() {
		while (!map.containsKey(highKey)) {
			highKey--;
			if (highKey <= MINLIMIT) {
				return null;
			}
		}
		
		Queue<E> queue = map.get(highKey);
		E val = queue.remove();
		if (queue.isEmpty()) {
			map.remove(highKey);
			highKey--;
		}
		return val;
	}
	
	/**
	 * Empty the map
	 * Reset the key
	 */
	public void clear() {
		map.clear();
		highKey = MINLIMIT;
	}

}
