package app.menu;

import app.Config;
import app.GameCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ControlBar extends JPanel {
	private static final String BTN_PLAY = "play";
	private static final String BTN_STEP = "step";
	private static GameCanvas gameCanvas;
	private static JLabel labelStepN;
	
	public ControlBar(GameCanvas gameCanvas) {
		super();
		ControlBar.gameCanvas = gameCanvas;
		setBackground(Color.LIGHT_GRAY);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JLabel labelStep = new JLabel("Step: ");
		labelStepN = new JLabel("0");
		labelStepN.setPreferredSize(new Dimension(40, 20));
		JLabel labelGameSpeed = new JLabel("Speed: ");
		JLabel labelGameSpeedN = new JLabel(String.valueOf(Config.FRAME_PER_SECOND));
		labelGameSpeedN.setPreferredSize(new Dimension(30, 20));
		JScrollBar scrollGameSpeed = new JScrollBar(Adjustable.HORIZONTAL,
													Config.FRAME_PER_SECOND,
													1,
													1,
													120);
		scrollGameSpeed.addAdjustmentListener(e -> {
			labelGameSpeedN.setText(String.valueOf(scrollGameSpeed.getValue()));
			gameCanvas.setFrameCountPerSecond(scrollGameSpeed.getValue());
		});
		JButton btnPlay = new JButton(new BtnAction());
		btnPlay.setName(BTN_PLAY);
		btnPlay.setText("Play");
		JButton btnStep = new JButton(new BtnAction());
		btnStep.setName(BTN_STEP);
		btnStep.setText("One step");
		
		add(Box.createHorizontalStrut(5));
		add(labelStep);
		add(labelStepN);
		add(Box.createHorizontalGlue());
		add(btnPlay);
		add(btnStep);
		add(Box.createHorizontalGlue());
		add(Box.createHorizontalGlue());
		add(Box.createHorizontalGlue());
		add(Box.createHorizontalGlue());
		add(labelGameSpeed);
		add(labelGameSpeedN);
		add(Box.createHorizontalStrut(5));
		add(scrollGameSpeed);
		add(Box.createHorizontalStrut(5));
	}
	
	public static void updateStepLabel(int steps) {
		labelStepN.setText(String.valueOf(steps));
	}
	
	private static class BtnAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			
			if (btn.getName().equals(BTN_PLAY)) {
				if (gameCanvas.isRun()) {
					gameCanvas.pauseGame();
					btn.setText("Play");
					btn.setSelected(false);
				} else {
					gameCanvas.resumeGame();
					btn.setSelected(true);
					btn.setText("Stop");
				}
			} else if (btn.getName().equals(BTN_STEP)) {
				gameCanvas.gameNextStep();
				gameCanvas.repaint();
			}
		}
	}
}
