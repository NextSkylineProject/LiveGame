package app.menu;

import app.GameCanvas;
import app.GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TemplateBar extends JPanel {
	private static final String BTN_CLEAR = "Clear";
	private static final String BTN_RANDOM = "Random";
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
		
		add(btnClear);
		add(btnRandom);
	}
	
	private static class BtnAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			
			if (btn.getName().equals(BTN_CLEAR)) {
				gameCanvas.clearField();
			} else if (btn.getName().equals(BTN_RANDOM)) {
				gameCanvas.randomField();
			}
		}
	}
	
}
