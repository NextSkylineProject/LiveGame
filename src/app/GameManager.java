package app;

import app.menu.ControlBar;

import java.util.Random;

public class GameManager {
	private final Cell[][] cells;
	private int gameSteps;
	private final Random random;
	
	GameManager() {
		cells = new Cell[Config.WIDTH][Config.HEIGHT];
		gameSteps = 0;
		random = new Random(12345);
		
		initCells();
	}
	
	private void initCells() {
		for (int i = 0; i < Config.WIDTH; i++) {
			for (int j = 0; j < Config.HEIGHT; j++) {
				cells[i][j] = new Cell(i, j);
			}
		}
	}
	
	public void clearField() {
		for (int i = 0; i < Config.WIDTH; i++) {
			for (int j = 0; j < Config.HEIGHT; j++) {
				cells[i][j].setAlive(false);
			}
		}
	}
	
	public void randomField() {
		for (int i = 0; i < Config.WIDTH; i++) {
			for (int j = 0; j < Config.HEIGHT; j++) {
				cells[i][j].setAlive(random.nextBoolean());
			}
		}
	}
	
	public Cell getCell(int x, int y) {
		if (x < 0 || x > Config.WIDTH || y < 0 || y > Config.HEIGHT) {
			return cells[0][0];
		}
		return cells[x][y];
	}
	
	public void gameNextStep() {
		// Count near cells
		for (int x = 0; x < Config.WIDTH; x++)
			for (int y = 0; y < Config.HEIGHT; y++)
				cells[x][y].setNearbyLivingCellsCount(getNearbyLivingCells(cells[x][y]));
		
		// Use the rules
		for (int x = 0; x < Config.WIDTH; x++)
			for (int y = 0; y < Config.HEIGHT; y++)
				useTheRules(cells[x][y]);
		
		gameSteps++;
		ControlBar.updateStepLabel(gameSteps);
	}
	
	private int getNearbyLivingCells(Cell cell) {
		if (cell == null)
			return 0;
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
		} else if (cell.isAlive() && (n != 2 && n != 3)) { // Died
			cell.setAlive(false);
		}
	}
}
