package com.clay13chopper.game1;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.input.Keyboard;
import com.clay13chopper.game1.room.Room;
import com.clay13chopper.game1.room.level.Level;

/**
 * Tactics game
 * 
 *  To run:
 *  	export to an executable JAR file 
 *  	open a terminal in the directory where you put the JAR file ^
 *  		run "set path=C:\Program Files\Java\jdk1.8.0_121\bin" to set the java file path
 *  		run the command "java -jar [filename].jar"
 *  
 * @author Me
 *
 */
public class Game extends Canvas implements Runnable {

	/**
	 * Simply need this, unexplained
	 */
	private static final long serialVersionUID = 1L;
	
	private static int width = 500;
	private static int height = width * 9 / 16;
	private static int scale = 5;

	private static String title = "Game1";
	
	private Thread thread;
	private JFrame frame;

	private boolean running = false;
	
	private Room room;
	private Screen screen;
	private Keyboard key;

	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public Game() {
		
		// TODO: Create a "start level x" method that resets all this:
		// Need to update all occurrences of width & height
		room = Level.level1;
		width = room.getWidth() * 16; // TODO: add static final sprite size somewhere
		height = room.getHeight() * 16;
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		// end of todo
		
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);

		frame = new JFrame();
		screen = new Screen(width, height);
		
		key = new Keyboard();
		addKeyListener(key);
	}
	
	@Override
	public void run() {
		
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		// Convert to s (1 million), and run 60 times a second
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;

		requestFocus();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}

			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(updates + "ups , " + frames + "fps");
//				System.out.println(updates + ", " + frames);
				updates = 0;
				frames = 0;
			}
			
		}
		stop();
		
	}
	
	public void update() {
		key.update();
		screen.update(room.getFocus());
		room.update();
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.clear();

		room.render(screen);

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.getPixel(i);
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();

		bs.show();
		
	}

	public static void main(String[] args) {
		Game game = new Game();

		game.frame.setResizable(false);
		game.frame.setTitle(Game.title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);

		game.start();

	}

	public void start() {
		running = true;

		thread = new Thread(this, "Display");
		thread.start();

	}

	public void stop() {
		running = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
