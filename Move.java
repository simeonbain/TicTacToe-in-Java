/* 
 * Class that stores the row and column coordinates of a TicTacToe move. 
 */ 

public class Move {
	
	private int moveRow;
	private int moveColumn;

	/* Constructors */
	public Move() {}

	public Move(int newMoveRow, int newMoveColumn) {
		this.moveRow = newMoveRow; 
		this.moveColumn = newMoveColumn;
	}

	/* Accessors */
	int getRow() {
		return this.moveRow;
	}

	int getColumn() {
		return this.moveColumn;
	}

	/* Mutators */ 
	void setRow(int newMoveRow) {
		this.moveRow = newMoveRow; 
	}

	void setColumn(int newMoveColumn) {
		this.moveColumn = newMoveColumn;
	}

}