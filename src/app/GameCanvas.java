package app;

import app.menu.ControlBar;

import javax.swing.*;
import java.awt.*;

public class GameCanvas extends JComponent implements Runnable {
	private Cell[][] cells;
	private int gameSteps;
	private Thread gameThread;
	private boolean run;
	private long oneFrameTime = 1_000_000_000 / Config.FRAME_PER_SECOND;
	//	private boolean lowFPS = oneFrameTime > Config.FIXED_FPS;
	private final Object threadPauseObject = new Object();
	private int fps = 0;
	private int tps = 0;
	private Camera camera;
	private MouseHandler mouseHandler;
	
	GameCanvas() {
		super();
	}
	
	public void init() {
		cells = new Cell[Config.HEIGHT][Config.WIDTH];
		gameSteps = 0;
		run = false;
		gameThread = new Thread(this);
		camera = new Camera(new Dimension(Config.WIDTH * Config.CELL_SIZE,
										  Config.HEIGHT * Config.CELL_SIZE));
		camera.setPos((Config.WIDTH * Config.CELL_SIZE) / 2,
					  (Config.HEIGHT * Config.CELL_SIZE) / 2);
		mouseHandler = new MouseHandler(this);
		
		//todo нужно ли добавлять листенеры тут или надо перенести в майнФрейм???
		addKeyListener(new KeyHandler(this));
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
		addMouseWheelListener(mouseHandler);
		
		initCells();
	}
	
	private void initCells() {
		for (int i = 0; i < Config.HEIGHT; i++) {
			for (int j = 0; j < Config.WIDTH; j++) {
				cells[i][j] = new Cell(i, j);
			}
		}
		
		cells[10][10].setAlive(true);
		cells[9][11].setAlive(true);
		cells[10][11].setAlive(true);
		cells[11][11].setAlive(true);
		cells[16][10].setAlive(true);
		cells[15][11].setAlive(true);
		cells[16][11].setAlive(true);
		cells[17][11].setAlive(true);
		
		cells[19][29].setAlive(true);
		cells[20][30].setAlive(true);
		cells[21][30].setAlive(true);
		cells[22][30].setAlive(true);
		cells[23][30].setAlive(true);
		cells[23][29].setAlive(true);
		cells[23][28].setAlive(true);
		cells[22][27].setAlive(true);
	}
	
	// Main thread control functions
	
	public void resumeGame() {
		run = true;
		
		if (!gameThread.isAlive()) {
			System.out.println("Start mainThread");
			gameThread.start();
		} else {
			System.out.println("Resume mainThread");
			synchronized (threadPauseObject) {
				threadPauseObject.notify();
			}
		}
	}
	
	public void pauseGame() {
		System.out.println("Paused mainThread");
		run = false;
	}
	
	public boolean isRun() {
		return run;
	}
	
	public void setFrameCountPerSecond(int frameCount) {
		oneFrameTime = 1_000_000_000 / (Math.max(frameCount, 1));
//		lowFPS = oneFrameTime > Config.FIXED_FPS;
	}
	
	public Cell getCell(int x, int y) {
		return cells[x][y];
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	// Game functions
	
	public void gameNextStep() {
		// Count near cells
		for (int x = 0; x < Config.HEIGHT; x++)
			for (int y = 0; y < Config.WIDTH; y++)
				cells[x][y].setNearbyLivingCellsCount(getNearbyLivingCells(cells[x][y]));
		
		// Use the rules
		for (int x = 0; x < Config.HEIGHT; x++)
			for (int y = 0; y < Config.WIDTH; y++)
				useTheRules(cells[x][y]);
		
		gameSteps++;
		ControlBar.updateStepLabel(gameSteps);
	}
	
	private int getNearbyLivingCells(Cell cell) {
		int n = 0;
		for (int sx = -1; sx <= +1; sx++)
			for (int sy = -1; sy <= +1; sy++)
				if (!(sx == 0 && sy == 0))
					if (cells[(cell.x + sx + Config.WIDTH) % Config.WIDTH][(cell.y + sy + Config.HEIGHT) % Config.HEIGHT]
							.isAlive())
						n++;
		return n;
	}
	
	private void useTheRules(Cell cell) {
		if (cell == null)
			return;
		int n = cell.getNearbyLivingCellsCount();
		
		if (!cell.isAlive() && n == 3) { // Born
			cell.setAlive(true);
		} else if (cell.isAlive() && (n == 2 || n == 3)) { // Live
			cell.setAlive(true);
		} else { // Dying
			cell.setAlive(false);
		}
	}
	
	
	@Override
	public void run() {
		long currentTime;
		long deltaTime;
		long lastTime = 0;
//		long frameTime = 0;
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
			/*
			if (lowFPS) {
				frameTime += deltaTime;
				if (frameTime > Config.FIXED_FPS) {
					frameTime = 0;
					frameCounter++;
					
					repaint();
				}
			}
			*/
			
			if (updateTime > oneFrameTime) {
				updateTime = 0;
				
				gameNextStep();

//				if (!lowFPS) {
				frameCounter++;
				repaint();
//				}
			}
			
			if (currentRateTimerTime > refreshRateTimer) {
				refreshRateTimer = currentRateTimerTime + 1_000;
				fps = frameCounter;
				tps = tickCounter;
				frameCounter = 0;
				tickCounter = 0;
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		if (camera != null) {
			camera.transform(g2d);
		}
		
		for (int i = 0; i < Config.HEIGHT; i++) {
			for (int j = 0; j < Config.WIDTH; j++)
				cells[i][j].paint(g2d);
		}
		
		if (!isRun()) {
			g2d.setColor(Color.red);
			g2d.drawRect(mouseHandler.getCellCoordinates().x * Config.CELL_SIZE,
						 mouseHandler.getCellCoordinates().y * Config.CELL_SIZE,
						 Config.CELL_SIZE,
						 Config.CELL_SIZE);
			g2d.drawString(mouseHandler.getCellCoordinates().x + "," + mouseHandler
								   .getCellCoordinates().y,
						   mouseHandler.getMousePos().x,
						   mouseHandler.getMousePos().y - 1);
		}
		
		if (camera != null) {
			camera.restore(g2d);
		}
		
		// Debug
		
		g2d.setColor(Color.gray);
		g2d.drawString("tps: " + tps, 2, 10);
		g2d.drawString("fps: " + fps, 2, 25);
		g2d.drawString("camera: " + camera, 2, 40);
	}
}
