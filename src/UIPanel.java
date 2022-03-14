import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class UIPanel extends JPanel implements ActionListener, GP2UICallBack {
	private JPanel topPanel = new JPanel();
	private JPanel midlePanel = new JPanel();
	private JPanel midlePanel1 = new JPanel();
	private JPanel midlePanel2 = new JPanel();
	private JPanel bottomPanel = new JPanel();
	private JLabel modeLabel = new JLabel("Time");
	private JLabel timerLabel= new JLabel("00:00.00");
	private int tM = 0, tS = 0, tm = 0;
	private Timer timer = new Timer(10, this);
	private JButton resetButton = new JButton("Reset");
	private JButton multiButton = new JButton("Shuffle");
	private JButton rRotateButton = new JButton("Rotate:R");
	private JButton lRotateButton = new JButton("Rotate:L");
	private int rotateFlag = 0;
	private UI2GPCallBack gp = null;

	public UIPanel(int x, int y, UI2GPCallBack gp) {
		this.gp = gp;
		this.setPreferredSize(new Dimension(x, y));
		this.setLayout(new GridLayout(3, 1));
		this.add(this.topPanel);
		this.midlePanel.setLayout(new GridLayout(1, 2));
		this.add(this.midlePanel);
		this.midlePanel.add(midlePanel1);
		this.midlePanel.add(midlePanel2);
		this.add(this.bottomPanel);
		this.modeLabel.setFont(new Font("Arial", Font.BOLD, 20));
		this.timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
		this.topPanel.add(this.modeLabel);
		this.topPanel.add(this.timerLabel);
		this.midlePanel1.add(this.resetButton);
		this.midlePanel2.add(this.multiButton);
		this.bottomPanel.add(this.lRotateButton);
		this.bottomPanel.add(this.rRotateButton);
		this.resetButton.addActionListener(this);
		this.multiButton.addActionListener(this);
		this.rRotateButton.addActionListener(this);
		this.lRotateButton.addActionListener(this);
		this.setVisible(true);
	}

	private void nextTime() {
		this.tm++;
		if(tm >= 100) {
			this.tS++;
			tm %= 100;
		}
		if(this.tS >= 60) {
			this.tM++;
			tS %= 60;
		}
		this.timerLabel.setText(String.format("%02d:%02d.%02d", this.tM, this.tS, this.tm));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.resetButton) == true) {
			this.gp.resetButtonPushed();
			this.timer.stop();
			this.timerLabel.setText("00:00.00");
			this.tm = 0;
			this.tM = 0;
			this.tS = 0;
			this.multiButton.setText("Shuffle");
			this.multiButton.setEnabled(true);
			this.rotateFlag = 0;
		}else if(e.getSource().equals(this.multiButton) == true) {
			int res = this.gp.multiButtonPushed(this.multiButton);
			if(res == 1) {
				this.timer.start();
			}
		}else if(e.getSource().equals(this.timer) == true) {
			this.nextTime();
		}else if(e.getSource().equals(this.rRotateButton) == true) {
			if(this.multiButton.getText().equals("Shuffle") == true) {
				this.rotateFlag++;
				this.rotateFlag %= 4;
				this.gp.rotateButtonPushed(this.rotateFlag);
			}
		}else if(e.getSource().equals(this.lRotateButton) == true) {
			if(this.multiButton.getText().equals("Shuffle") == true) {
				this.rotateFlag = (this.rotateFlag - 1 + 4) % 4;
				this.gp.rotateButtonPushed(this.rotateFlag);
			}
		}
	}

	@Override
	public void endGamed() {
		this.timer.stop();
		this.multiButton.setText("Clear!");
	}
}