package com.clay13chopper.game1;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.clay13chopper.game1.graphics.Screen;
import com.clay13chopper.game1.input.Keyboard;
import com.clay13chopper.game1.room.Room;
import com.clay13chopper.game1.room.StartMenu;

/**
 * Tactics game
 * 
 *  To run:
 *  	export to an executable JAR file 
 *  	open a terminal in the directory where you put the JAR file ^
 *  		run "set path=C:\Program Files\Java\jdk1.8.0_121\bin" to set the java file path
 *  		run the command "java -jar [filename].jar"
 *  
 * @author Clayton Cunningham
 *
 */
public class Game extends Canvas implements Runnable {

	/**
	 * Simply need this, unexplained
	 */
	private static final long serialVersionUID = 1L;
	
	private static int width = 500;
	private static int height = width * 9 / 16;
	private static int scale = 3;

	private static double computerScreenWidth = 0;
	private static double computerScreenHeight = 0;

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
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		computerScreenWidth = screenSize.getWidth();
		computerScreenHeight = screenSize.getHeight();

		frame = new JFrame();
		
		setRoom(new StartMenu());
		
		key = new Keyboard();
		addKeyListener(key);
	}
	
	/**
	 * Sets a new room or level to move to in the game
	 * @param r		Room to move to
	 */
	public void setRoom(Room r) {
		room = r;

		scale = room.getScale();
		// Sets the game screen's width and height
		width = Math.min(room.getWidthbyPixel(), (int)computerScreenWidth / scale); 
		height = Math.min(room.getHeightbyPixel(), (int)computerScreenHeight / scale); // Note: could set to dependent on width i.e. (w*9/16)

		// Reduces the scale if necessary to make sure the room fits on screen
		double widthDif = computerScreenWidth / width;
		double heightDif = computerScreenHeight / height;
		for (int i = scale; i > 0; i--) {
			if (widthDif >= i && heightDif >= i) {
				scale = i;
				break;
			}
		}
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);

		screen = new Screen(width, height);
		room.prep();

		this.frame.setSize(size);
		
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
	
	/**
	 * This cycle tells the keyboard, screen and room to update continuously
	 */
	public void update() {
		Room r = room.getChangeRoom();
		if (r != null) setRoom(r);
		
		key.update();
		screen.update(room.getFocus());
		room.update();
		
	}
	
	/**
	 * This cycle tells the room to re-render after every update
	 */
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

		game.frame.setResizable(true);
		game.frame.setTitle(Game.title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);

		game.start();

	}

	/**
	 * Starts the game
	 */
	public void start() {
		running = true;

		thread = new Thread(this, "Display");
		thread.start();

	}

	/**
	 * Stops the game
	 */
	public void stop() {
		running = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
