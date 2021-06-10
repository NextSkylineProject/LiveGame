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
	private final Color colorCell = new Color(0x149CF8);
	private final Color colorBackground = new Color(0x393939);
	private final Color colorBorder = new Color(0xF87B7B);
	
	GameCanvas() {
		super();
	}
	
	public void init() {
		run = false;
		gameThread = new Thread(this);
		camera = new Camera(getSize());
		camera.setPos((float) getSize().width / 2, (float) getSize().height / 2);
		camera.setBounds(0, 0, Config.WIDTH * Config.CELL_SIZE, Config.HEIGHT * Config.CELL_SIZE);
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
	
	public void updateCanvasSize() {
		camera.setScreenSize(getSize());
		camera.updatePos();
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
				
				Debug.showString("FPS", String.valueOf(frameCounter));
				Debug.showString("TPS", String.valueOf(tickCounter));
				Debug.showString("Camera", camera.toString());
				
				frameCounter = 0;
				tickCounter = 0;
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		camera.transform(g2d);
		{
			g2d.setColor(colorBackground);
			g2d.fillRect(0, 0, Config.WIDTH * Config.CELL_SIZE, Config.HEIGHT * Config.CELL_SIZE);
			g2d.setColor(colorBorder);
			g2d.drawRect(0, 0, Config.WIDTH * Config.CELL_SIZE, Config.HEIGHT * Config.CELL_SIZE);
			
			for (int i = 0; i < Config.WIDTH; i++)
				for (int j = 0; j < Config.HEIGHT; j++) {
					Cell cell = gameManager.getCell(i, j);
					if (!cell.isAlive()) {
						continue;
					}
					g2d.setColor(colorCell);
					g2d.fillRect(cell.x * Config.CELL_SIZE,
								 cell.y * Config.CELL_SIZE,
								 Config.CELL_SIZE,
								 Config.CELL_SIZE);
				}
			
			if (!isRun()) {
				g2d.setColor(colorBorder);
				g2d.drawRect(mouseHandler.getCellCoordinates().x * Config.CELL_SIZE,
							 mouseHandler.getCellCoordinates().y * Config.CELL_SIZE,
							 Config.CELL_SIZE,
							 Config.CELL_SIZE);
			}
		}
		camera.restore(g2d);
		
		Debug.draw(g2d);
	}
}
