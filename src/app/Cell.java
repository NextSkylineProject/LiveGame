package app;

import java.awt.*;

public class Cell {
	public final int x;
	public final int y;
	private int nearbyLivingCellsCount;
	private boolean alive;
	
	Cell(int x, int y) {
		this.x = x;
		this.y = y;
		this.alive = false;
		this.nearbyLivingCellsCount = 0;
	}
	
	public int getNearbyLivingCellsCount() {
		return nearbyLivingCellsCount;
	}
	
	public void setNearbyLivingCellsCount(int nearbyLivingCellsCount) {
		this.nearbyLivingCellsCount = nearbyLivingCellsCount;
	}
	
	public boolean isAlive() {
		return this.alive;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public void paint(Graphics2D g2d) {
		if(!alive) {return;}
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect(this.x * Config.CELL_SIZE,
					 this.y * Config.CELL_SIZE,
					 Config.CELL_SIZE,
					 Config.CELL_SIZE);
	}
}
