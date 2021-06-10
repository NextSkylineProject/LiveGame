package app.menu;

import app.Cell;
import app.GameCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;


public class TemplateList extends JPanel {
	HashMap<String, Template> templates;
	ArrayList<JButton> buttons;
	private boolean btnOn;
	private GameCanvas gameCanvas;
	
	TemplateList(GameCanvas gameCanvas) {
		super();
		setBackground(Color.lightGray);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel label = new JLabel("Templates");
		add(label);
		
		this.gameCanvas = gameCanvas;
		templates = new HashMap<>(20);
		buttons = new ArrayList<>(20);
		btnOn = true;
	}
	
	public void addTemplate(String name, Template template) {
		templates.put(name, template);
		
		JButton btn = new JButton(new BtnAction());
		btn.setName(name);
		btn.setText(name);
		btn.setFocusable(false);
		btn.setEnabled(btnOn);
		this.add(btn);
		buttons.add(btn);
	}
	
	public void buttonsOn(boolean on) {
		btnOn = on;
		for (JButton b : buttons) {
			b.setEnabled(on);
		}
	}
	
	
	public static class Template {
		private final ArrayList<Cell> array = new ArrayList<>(10);
		
		public void addCell(int x, int y) {
			Cell cell = new Cell(x, y);
			cell.setAlive(true);
			array.add(cell);
		}
		
		public ArrayList<Cell> getCells() {
			return array;
		}
	}
	
	private class BtnAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			String key = btn.getName();
			Template temp = templates.get(key);
			gameCanvas.placeTemplateStart(temp);
		}
	}
}
