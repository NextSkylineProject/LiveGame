package app;

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
}
