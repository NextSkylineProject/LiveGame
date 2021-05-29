package app;

import javax.swing.*;
import java.awt.*;

public class GameCanvas extends JComponent implements Runnable {
	private Thread gameThread;
	private boolean run;
	private long oneFrameTime = 1_000_000_000 / Config.FRAME_PER_SECOND;
	private final Object threadPauseObject = new Object();
	private Camera camera;
	private MouseHandler mouseHandler;
	private GameManager gameManager;
	
	GameCanvas() {
		super();
	}
	
	public void init() {
		run = false;
		gameThread = new Thread(this);
		camera = new Camera(new Dimension(Config.FRAME_WIDTH, Config.FRAME_HEIGHT));
		camera.setPos((float) (Config.FRAME_WIDTH) / 2, (float) (Config.FRAME_HEIGHT) / 2);
		mouseHandler = new MouseHandler(this);
		gameManager = new GameManager();
		
		//todo нужно ли добавлять листенеры тут или надо перенести в майнФрейм???
		addKeyListener(new KeyHandler(this));
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
		addMouseWheelListener(mouseHandler);
	}
	
	// Main thread control functions
	
	public void resumeGame() {
		run = true;
		
		if (!gameThread.isAlive()) {
			Debug.print("Start mainThread");
			gameThread.start();
		} else {
			Debug.print("Resume mainThread");
			synchronized (threadPauseObject) {
				threadPauseObject.notify();
			}
		}
	}
	
	public void pauseGame() {
		Debug.print("Paused mainThread");
		run = false;
	}
	
	public boolean isRun() {
		return run;
	}
	
	public void setFrameCountPerSecond(int frameCount) {
		oneFrameTime = 1_000_000_000 / (Math.max(frameCount, 1));
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	// Game functions
	public Cell getCell(int x, int y) {
		return gameManager.getCell(x, y);
	}
	
	public void gameNextStep() {
		gameManager.gameNextStep();
	}
	
	public void clearField() {
		gameManager.clearField();
		repaint();
		Debug.print("Clear all cells");
	}
	
	public void randomField() {
		gameManager.randomField();
		repaint();
		Debug.print("Random all cells");
	}
	
	@Override
	public void run() {
		long currentTime;
		long deltaTime;
		long lastTime = 0;
		long updateTime = 0;
		long currentRateTimerTime;
		long refreshRateTimer = 0;
		int frameCounter = 0;
		int tickCounter = 0;
		
		while (!Thread.currentThread().isInterrupted()) {
			synchronized (threadPauseObject) {
				if (!run) {
					try {
						threadPauseObject.wait();
					} catch (InterruptedException ignored) { }
				}
			}
			
			currentTime = System.nanoTime();
			deltaTime = currentTime - lastTime;
			lastTime = currentTime;
			updateTime += deltaTime;
			currentRateTimerTime = System.currentTimeMillis();
			
			tickCounter++;
			
			if (updateTime > oneFrameTime) {
				updateTime = 0;
				
				gameManager.gameNextStep();
				
				frameCounter++;
				repaint();
			}
			
			if (currentRateTimerTime > refreshRateTimer) {
				refreshRateTimer = currentRateTimerTime + 1_000;
				frameCounter = 0;
				tickCounter = 0;
				
				Debug.showString("FPS", String.valueOf(frameCounter));
				Debug.showString("TPS", String.valueOf(tickCounter));
				Debug.showString("Camera", camera.toString());
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		camera.transform(g2d);
		{
			g2d.setColor(Color.gray);
			g2d.fillRect(0, 0, Config.WIDTH * Config.CELL_SIZE, Config.HEIGHT * Config.CELL_SIZE);
			
			for (int i = 0; i < Config.WIDTH; i++)
				for (int j = 0; j < Config.HEIGHT; j++)
					gameManager.getCell(i, j).paint(g2d);
			
			if (!isRun()) {
				g2d.setColor(Color.red);
				g2d.drawRect(mouseHandler.getCellCoordinates().x * Config.CELL_SIZE,
							 mouseHandler.getCellCoordinates().y * Config.CELL_SIZE,
							 Config.CELL_SIZE,
							 Config.CELL_SIZE);
				g2d.drawString(mouseHandler.getCellCoordinates().x + "," + mouseHandler
									   .getCellCoordinates().y,
							   mouseHandler.getCellCoordinates().x * Config.CELL_SIZE,
							   mouseHandler.getCellCoordinates().y * Config.CELL_SIZE - 1);
			}
		}
		camera.restore(g2d);
		
		Debug.draw(g2d);
	}
}
