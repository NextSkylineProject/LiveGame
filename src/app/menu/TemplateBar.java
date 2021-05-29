package app.menu;

import app.GameCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TemplateBar extends JPanel {
	private static final String BTN_CLEAR = "Clear";
	private static final String BTN_RANDOM = "Random";
	private static final String BTN_DEBUG = "Debug";
	private static GameCanvas gameCanvas;
	
	public TemplateBar(GameCanvas gameCanvas) {
		super();
		setBackground(Color.darkGray);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		TemplateBar.gameCanvas = gameCanvas;
		
		JButton btnClear = new JButton(new BtnAction());
		btnClear.setName(BTN_CLEAR);
		btnClear.setText(BTN_CLEAR);
		btnClear.setFocusable(false);
		
		JButton btnRandom = new JButton(new BtnAction());
		btnRandom.setName(BTN_RANDOM);
		btnRandom.setText(BTN_RANDOM);
		btnRandom.setFocusable(false);
		
		JButton btnDebug = new JButton(new BtnAction());
		btnDebug.setName(BTN_DEBUG);
		btnDebug.setText(BTN_DEBUG);
		btnDebug.setFocusable(false);
		
		
		add(btnClear);
		add(btnRandom);
		add(btnDebug);
	}
	
	private static class BtnAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			
			switch (btn.getName()) {
				case BTN_CLEAR -> gameCanvas.clearField();
				case BTN_RANDOM -> gameCanvas.randomField();
				case BTN_DEBUG -> Debug.On(!Debug.isOn());
			}
		}
	}
	
}
