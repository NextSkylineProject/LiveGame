package app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
	private final GameCanvas gameCanvas;
	
	KeyHandler(GameCanvas canvas) {
		this.gameCanvas = canvas;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		Debug.print("Key code: " + e.getKeyCode());
		// space 32
		// + - 61 45
		// escape - 27
		int kc = e.getKeyCode();
		
		switch (kc) {
			case 32 -> {
				if (gameCanvas.isRun()) {gameCanvas.pauseGame();} else {gameCanvas.resumeGame();}
			}
			case 61 -> {
				gameCanvas.getCamera().scale(0.1);
				gameCanvas.getCamera().updatePos();
				gameCanvas.repaint();
			}
			case 45 -> {
				gameCanvas.getCamera().scale(-0.1);
				gameCanvas.getCamera().updatePos();
				gameCanvas.repaint();
			}
			case 27 -> {
				gameCanvas.placeTemplateEnd();
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	
	}
}
