package app;

import java.awt.*;
import java.awt.event.*;

public class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {
	private final GameCanvas gameCanvas;
	private int mouseX = 0;
	private int mouseY = 0;
	private int cellX;
	private int cellY;
	private boolean dragged = false;
	
	public MouseHandler(GameCanvas gameCanvas) {
		this.gameCanvas = gameCanvas;
	}
	
	public Point getCellCoordinates() {
		return new Point(cellX, cellY);
	}
	
	public Point getMousePos() {
		return new Point(mouseX, mouseY);
	}
	
	public boolean isDragged() {
		return dragged;
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
	
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (!gameCanvas.isRun() && !dragged) {
			gameCanvas.getCell(cellX, cellY).setAlive(!gameCanvas.getCell(cellX, cellY).isAlive());
			gameCanvas.repaint();
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	
	}
	
	// MouseWheelListener
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getPreciseWheelRotation() < -0.4) {
			gameCanvas.getCamera().scale(0.02d);
			gameCanvas.repaint();
		} else if (e.getPreciseWheelRotation() > 0.4) {
			gameCanvas.getCamera().scale(-0.02d);
			gameCanvas.repaint();
		}
	}
	
	// MouseMoveListener
	
	@Override
	public void mouseDragged(MouseEvent e) {
		dragged = true;
		int tx = e.getX() - mouseX;
		int ty = e.getY() - mouseY;
		mouseX = e.getX();
		mouseY = e.getY();
		
		gameCanvas.getCamera().move(-tx / gameCanvas.getCamera().getScale(),
									-ty / gameCanvas.getCamera().getScale());
		gameCanvas.repaint();
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		dragged = false;
		mouseX = e.getX();
		mouseY = e.getY();
		
		double cellTempX = gameCanvas.getCamera().screenToWorld(mouseX, mouseY).x;
		double cellTempY = gameCanvas.getCamera().screenToWorld(mouseX, mouseY).y;
		cellTempX = cellTempX / Config.CELL_SIZE;
		cellTempY = cellTempY / Config.CELL_SIZE;
		cellX = (int) cellTempX;
		cellY = (int) cellTempY;
		
		// Remove this part if use lowFPS
		if (!gameCanvas.isRun()) {
			gameCanvas.repaint();
		}
		//
	}
}
