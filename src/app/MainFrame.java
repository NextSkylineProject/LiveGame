package app;

import app.menu.ControlBar;
import app.menu.TemplateBar;

import javax.swing.*;
import java.awt.*;


/*TODO залить все на git и разобраться с ним
 */

public class MainFrame extends JFrame {
	GameCanvas gameCanvas;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(MainFrame::new);
	}
	
	public MainFrame() {
		super("Live Game");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		gameCanvas = new GameCanvas();
		gameCanvas.setPreferredSize(new Dimension(Config.FRAME_WIDTH, Config.FRAME_HEIGHT));
		ControlBar controlBar = new ControlBar(gameCanvas);
		TemplateBar templateBar = new TemplateBar(gameCanvas);
		
		add(BorderLayout.CENTER, gameCanvas);
		add(BorderLayout.PAGE_START, controlBar);
		add(BorderLayout.LINE_START, templateBar);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		gameCanvas.requestFocus();
		gameCanvas.init();
	}
}
