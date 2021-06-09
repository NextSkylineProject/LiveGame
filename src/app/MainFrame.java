package app;

import app.menu.ControlBar;
import app.menu.SideBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class MainFrame extends JFrame {
	private final GameCanvas gameCanvas;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(MainFrame::new);
	}
	
	public MainFrame() {
		super("Live Game");
		Config.load();
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(Config.FRAME_WIDTH, Config.FRAME_HEIGHT));
		addComponentListener(new ComponentHandler());
		
		gameCanvas = new GameCanvas();
		ControlBar controlBar = new ControlBar(gameCanvas);
		SideBar sideBar = new SideBar(gameCanvas);
		
		add(BorderLayout.CENTER, gameCanvas);
		add(BorderLayout.PAGE_START, controlBar);
		add(BorderLayout.LINE_START, sideBar);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		gameCanvas.requestFocus();
		gameCanvas.init();
	}
	
	private class ComponentHandler extends ComponentAdapter {
		public void componentResized(ComponentEvent evt) {
			gameCanvas.updateCanvasSize();
		}
	}
}
