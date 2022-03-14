import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class PictureManager {
	public final static String pathTop = "C:\\Tomonori_Develop\\PSP";

	private static HashMap<Integer, String> fileNameMap = new HashMap<>();
	private static int fileSize = 0;
	private static String[] extensions = {".png", ".jpg", ".PNG", ".JPG"};

	static{
		ArrayList<String> fileArray = new ArrayList<>();
		Queue<String> pathQueue = new ArrayDeque<String>();
		pathQueue.add(pathTop);
		while(!pathQueue.isEmpty()) {
			String now = pathQueue.poll();
			File dir = new File(now);
			if(dir.exists() == false) {
				break;
			}
			File[] list = dir.listFiles();
			for(File f : list) {
				if(f.isFile() == true) {
					if(f.canRead() == false) continue;
					String name = f.getName();
					for(String ex : extensions) {
						if(name.contains(ex) == true) {
							fileArray.add(f.toString());
							break;
						}
					}
				}else {
					pathQueue.add(f.toString());
				}
			}
		}

		for(String s : fileArray) {
			fileNameMap.put(fileSize, s);
			fileSize++;
		}
	}

	public static int getFileSize() {
		return fileSize;
	}

	public static BufferedImage cropImage(int i) {
		return PictureManager.cropImage(fileNameMap.get(i));
	}


	public static BufferedImage cropImage(String path) {
		BufferedImage res = null;
		int width, height;
		int cx, cy;
		int ps = GamePanel.panelSize;
		try {
			res = ImageIO.read(new File(path));
			width = res.getWidth();
			cx = width/2;
			height = res.getHeight();
			cy = height/2;
			int size, x = 0, y = 0;
			if(width < height) {
				size = width;
				y = cy - size/2;
			}else {
				size = height;
				x = cx - size/2;
			}
			res = res.getSubimage(x, y, size, size);

			BufferedImage nx = new BufferedImage(ps, ps, BufferedImage.TYPE_3BYTE_BGR);
			nx.createGraphics().drawImage(res.getScaledInstance(ps, ps, Image.SCALE_AREA_AVERAGING), 0, 0, ps, ps, null);
			res = nx;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return res;
	}

	public static BufferedImage cropImage(int i, int rotateFlag) {
		return PictureManager.cropImage(fileNameMap.get(i), rotateFlag);
	}

	public static BufferedImage cropImage(String path, int rotateFlag) {
		BufferedImage ci = cropImage(path);
		if(rotateFlag == 0) return ci;
		AffineTransform affine = new AffineTransform();
		affine.setToRotation(Math.toRadians(90*rotateFlag), ci.getHeight()/2, ci.getHeight()/2);
		BufferedImage res = new BufferedImage(ci.getHeight(), ci.getWidth(), ci.getType());
		AffineTransformOp op = new AffineTransformOp(affine, AffineTransformOp.TYPE_BICUBIC);
		op.filter(ci, res);
		return res;
	}

	public static ImageIcon[][] makeImageArray(BufferedImage picture){
		int ps4 = GamePanel.panelSize/4;
		ImageIcon[][] res = new ImageIcon[GamePanel.puzzleSize][GamePanel.puzzleSize];
		for(int i = 0; i < res.length; i++) {
			for(int j = 0; j < res[i].length; j++) {
				if(i == res.length-1 && j == res[i].length-1) {
					res[i][j] = null;
				}else {
					res[i][j] = new ImageIcon(picture.getSubimage(j*ps4, i*ps4, ps4, ps4));
				}
			}
		}
		return res;
	}
}
