import javax.swing.JButton;

public interface UI2GPCallBack {
	void resetButtonPushed();
	int multiButtonPushed(JButton jb);
	void rotateButtonPushed(int flag);
}
