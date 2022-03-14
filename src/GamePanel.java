import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener, UI2GPCallBack{
	public final static int puzzleSize = 4;
	public final static int panelSize = Main.mPanelSize;
	public final static String defaultPicturePath = "./Picture/default.JPG";

	private JButton[][] buttonArray = new JButton[puzzleSize][puzzleSize];
	private ImageIcon[][] imageArray = new ImageIcon[puzzleSize][puzzleSize];
	private PuzzleController puzzleController = new PuzzleController();
	private Pair empty = null;
	private boolean canPlay = false;
	private GP2UICallBack gp2ui = null;
	private int PMfilesize = 0;
	private int nowPicture = 0;

	public GamePanel(int x, int y) {
		this.setPreferredSize(new Dimension(x, y));
		this.setLayout(new GridLayout(4, 4));
		for(int i = 0; i < this.buttonArray.length; i++) {
			for(int j = 0; j < this.buttonArray[i].length; j++) {
				this.buttonArray[i][j] = new JButton();
				this.buttonArray[i][j].addActionListener(this);
				this.add(this.buttonArray[i][j]);
			}
		}
		this.buttonArray[puzzleSize-1][puzzleSize-1].setEnabled(false);
		this.empty = new Pair(puzzleSize-1, puzzleSize-1);
		this.PMfilesize = PictureManager.getFileSize();
		this.setPicture();
		this.buttonUpdate();
	}

	public void setGP2UI(GP2UICallBack g) {
		this.gp2ui = g;
	}

	private void setPicture() {
		this.nowPicture = (int)(Math.random()*this.PMfilesize);
		BufferedImage all;
		if(this.PMfilesize == 0) {
			all = PictureManager.cropImage(defaultPicturePath);
		}else {
			all = PictureManager.cropImage(this.nowPicture);
		}
		this.imageArray = PictureManager.makeImageArray(all);
	}

	private void preStart() {
		if(this.empty != null) {
			this.buttonArray[this.empty.first][this.empty.second].setEnabled(true);
		}
		this.puzzleController.puzzleShuffle(20);
		this.empty = this.puzzleController.getEmptyPieces();
		this.buttonArray[this.empty.first][this.empty.second].setEnabled(false);
		this.buttonUpdate();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.canPlay == false) return ;
		for(int i = 0; i < this.buttonArray.length; i++) {
			for(int j = 0; j < this.buttonArray[i].length; j++) {
				if(e.getSource().equals(this.buttonArray[i][j]) == true) {
					if(this.puzzleController.update(j, i) == true) {
						Pair p = this.puzzleController.getEmptyPieces();
						this.buttonArray[p.first][p.second].setEnabled(false);
						this.buttonArray[this.empty.first][this.empty.second].setEnabled(true);
						this.empty = p;
						this.buttonUpdate();
						if(this.puzzleController.endGameJudge() == true){
							this.gp2ui.endGamed();
							this.canPlay = false;
						}
					}
				}
			}
		}
	}

	private void buttonUpdate() {
		for(int i = 0; i < this.buttonArray.length; i++) {
			for(int j = 0; j < this.buttonArray[i].length; j++) {
				Pair p = this.puzzleController.getPiecesNumber(i, j);
				this.buttonArray[i][j].setIcon(this.imageArray[p.first][p.second]);
			}
		}
	}

	@Override
	public void resetButtonPushed() {
		this.puzzleController.puzzleReset();
		if(this.empty != null) this.buttonArray[this.empty.first][this.empty.second].setEnabled(true);
		this.canPlay = false;
		this.buttonArray[puzzleSize-1][puzzleSize-1].setEnabled(false);
		this.empty = new Pair(puzzleSize-1, puzzleSize-1);
		this.setPicture();
		this.buttonUpdate();
	}

	@Override
	public int multiButtonPushed(JButton jb) {
		if(jb.getText().equals("Shuffle") == true) {
			this.preStart();
			jb.setText("Start");
		}else if(jb.getText().equals("Start") == true) {
			this.canPlay = true;
			jb.setText("Playing");
			jb.setEnabled(false);
			return 1;
		}
		return 0;
	}

	@Override
	public void rotateButtonPushed(int flag) {
		BufferedImage all;
		if(this.PMfilesize == 0) {
			all = PictureManager.cropImage(defaultPicturePath, flag);
		}else {
			all = PictureManager.cropImage(this.nowPicture, flag);
		}
		this.imageArray = PictureManager.makeImageArray(all);
		this.buttonUpdate();
	}
}
