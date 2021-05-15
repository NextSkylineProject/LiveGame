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
//		System.out.println(e.getKeyCode());
		// space 32
		// + - 61 45
		int kc = e.getKeyCode();
		
		switch (kc) {
			case 32 -> {
				if (gameCanvas.isRun()) {gameCanvas.pauseGame();} else {gameCanvas.resumeGame();}
			}
			case 61 -> {
//				gameCanvas.setOneFrameTime(gameCanvas.getOneFrameTime() - 20);
			}
			case 45 -> {
//				gameCanvas.setOneFrameTime(gameCanvas.getOneFrameTime() + 20);
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
