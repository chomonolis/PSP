import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
	public final static int mPanelSize = 800;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				frame.setSize(mPanelSize/2*3, mPanelSize+50);
				frame.setTitle("Picture Slide Puzzle");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().setLayout(new BorderLayout());
				GamePanel gamePanel =  new GamePanel(frame.getWidth()*2/3, frame.getHeight());
				UIPanel uIPanel = new UIPanel(frame.getWidth()/3, frame.getHeight(), gamePanel);
				gamePanel.setGP2UI(uIPanel);
				frame.add(BorderLayout.WEST, gamePanel);
				frame.add(BorderLayout.EAST, uIPanel);
				frame.setVisible(true);
			}
		});
	}
}
