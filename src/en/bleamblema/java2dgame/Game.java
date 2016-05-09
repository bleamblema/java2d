package en.bleamblema.java2dgame;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import en.bleamblema.java2dgame.entities.Player;
import en.bleamblema.java2dgame.entities.PlayerMP;
import en.bleamblema.java2dgame.gfx.Screen;
import en.bleamblema.java2dgame.gfx.SpriteSheet;
import en.bleamblema.java2dgame.level.Level;
import en.bleamblema.java2dgame.net.GameClient;
import en.bleamblema.java2dgame.net.GameServer;
import en.bleamblema.java2dgame.net.packets.Packet00Login;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 160;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 3;
	public static final String NAME = "Game";

	public JFrame frame;
	public boolean running = false;
	public int tickCount = 0;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
			BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer())
			.getData();
	private int[] colours = new int[6 * 6 * 6]; // R=6 G=6 b=6

	// private SpriteSheet spriteSheet = new SpriteSheet("/sprite_sheet.png");
	private Screen screen;

	public InputHandler input;
	
	public WindowHandler windowHandler;

	public Level level;

	public Player player;
	
	public GameClient socketClient;
	public GameServer socketServer;
	
	public Game() {
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		frame = new JFrame(NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void init() {
		int index = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = (r * (255 / 5));
					int gg = (g * (255 / 5));
					int bb = (b * (255 / 5));

					colours[index++] = rr << 16 | gg << 8 | bb;

				}
			}
		}

		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
		input = new InputHandler(this);
		windowHandler = new WindowHandler(this);
		level = new Level("/level/water_test_level.png");
		player = new PlayerMP(level, 100, 100, input, JOptionPane.showInputDialog(this, "Please Enter a username"), null, -1);
		level.addEntity(player);
		Packet00Login loginPacket = new Packet00Login(player.getUsername());
		if(socketServer != null){
			socketServer.addConnection((PlayerMP) player, loginPacket);
		}
//		socketClient.sendData("ping".getBytes());
		loginPacket.writeData(socketClient);
	}

	private synchronized void start() {
		running = true;
		new Thread(this).start();

		if(JOptionPane.showConfirmDialog(this, "Do you want to run the server") == 0){
			socketServer = new GameServer(this);
			socketServer.start();
		}
		
		socketClient = new GameClient(this, "localhost");
		socketClient.start();

	}

	public synchronized void stop() {
		running = false;
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;
		int ticks = 0;
		int frames = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		init();

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;

			boolean shouldRender = true;

			while (delta >= 1) {
				ticks++;
				tick();
				delta--;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				frame.setTitle(frames+ " frames" + "," + ticks + " ticks");
				frames = 0;
				ticks = 0;
			}

		}
	}

	public void tick() {
		tickCount++;
		level.tick();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		int xOffset = player.x - (screen.width / 2);
		int yOffset = player.y - (screen.height / 2);

		level.renderTiles(screen, xOffset, yOffset);
		level.renderEntities(screen);
		
		for (int y = 0; y < screen.height; y++) {
			for (int x = 0; x < screen.width; x++) {
				int colourCode = screen.pixels[x + y * screen.width];
				if (colourCode < 255)
					pixels[x + y * WIDTH] = colours[colourCode];
			}
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		new Game().start();
	}

}
