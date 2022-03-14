
public class PuzzleController {
	private Pieces[][] piecesArray = new Pieces[4][4];

	public PuzzleController() {
		this.puzzleReset();
	}

	public void puzzleReset() {
		for(int i = 0; i < this.piecesArray.length; i++) {
			for(int j = 0; j < this.piecesArray[i].length; j++) {
				if(i == this.piecesArray.length-1 && j == this.piecesArray[i].length-1) {
					this.piecesArray[i][j] = new Pieces(j, i, true);
				}else {
					this.piecesArray[i][j] = new Pieces(j, i);
				}
			}
		}
	}

	public void puzzleShuffle(int n) {
		Pair emptyPair = this.getEmptyPieces();
		int m = (int)(Math.random() * 2);
		for(int i = 0; i < n; i++) {
			int index = (int)(Math.random() * 3);
			if(i % 2 == m) {
				int sx = emptyPair.second + index+1;
				if(sx >= GamePanel.puzzleSize) sx -= GamePanel.puzzleSize;
				emptyPair.second = sx;
				this.update(sx, emptyPair.first);
			}else {
				int sy = emptyPair.first + index + 1;
				if(sy >= GamePanel.puzzleSize) sy -= GamePanel.puzzleSize;
				emptyPair.first = sy;
				this.update(emptyPair.second, sy);
			}
		}
	}

	public Pair getEmptyPieces() {
		for(int i = 0; i < this.piecesArray.length; i++) {
			for(int j = 0; j < this.piecesArray[i].length; j++) {
				if(this.piecesArray[i][j].getIsEmpty() == true) return new Pair(i, j);
			}
		}
		return null;
	}

	public Pair getPiecesNumber(int i,int j) {
		if(i < 0 || this.piecesArray.length <= i || j < 0 || this.piecesArray[i].length <= j) return null;
		return new Pair(this.piecesArray[i][j].getY(), this.piecesArray[i][j].getX());
	}

	public boolean endGameJudge() {
		for(int i = 0; i < this.piecesArray.length; i++) {
			for(int j = 0; j < this.piecesArray[i].length; j++) {
				if(this.piecesArray[i][j].getX() != j || this.piecesArray[i][j].getY() != i) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean update(int x,int y) {
		for(int i = y-1; i >= 0; i--) {
			if(this.piecesArray[i][x].getIsEmpty() == true) {
				Pieces tmp = piecesArray[i][x];
				for(int ii = i+1; ii <= y; ii++) {
					this.piecesArray[ii-1][x] = this.piecesArray[ii][x];
				}
				this.piecesArray[y][x] = tmp;
				return true;
			}
		}

		for(int i = y+1; i < this.piecesArray.length; i++) {
			if(this.piecesArray[i][x].getIsEmpty() == true) {
				Pieces tmp = piecesArray[i][x];
				for(int ii = i-1; ii >= y; ii--) {
					this.piecesArray[ii+1][x] = this.piecesArray[ii][x];
				}
				this.piecesArray[y][x] = tmp;
				return true;
			}
		}

		for(int j = x-1; j >= 0; j--) {
			if(this.piecesArray[y][j].getIsEmpty() == true) {
				Pieces tmp = piecesArray[y][j];
				for(int jj = j+1; jj <= x; jj++) {
					this.piecesArray[y][jj-1] = this.piecesArray[y][jj];
				}
				this.piecesArray[y][x] = tmp;
				return true;
			}
		}

		for(int j = x+1; j < this.piecesArray[y].length; j++) {
			if(this.piecesArray[y][j].getIsEmpty() == true) {
				Pieces tmp = piecesArray[y][j];
				for(int jj = j-1; jj >= x; jj--) {
					this.piecesArray[y][jj+1] = this.piecesArray[y][jj];
				}
				this.piecesArray[y][x] = tmp;
				return true;
			}
		}

		return false;
	}
}
