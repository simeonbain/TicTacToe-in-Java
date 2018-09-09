/* 
 * Class Invariant: all objects have username, family name, and given name strings, games played, 
 * games won, and games drawn counts, and win and draw ratios. 
 */

public class AIPlayer extends Player {

	private static final char EMPTY_SYMBOL = ' ';
	
	/* Constructors */ 
	public AIPlayer() {}

	public AIPlayer(String username, String familyName, String givenName) {

		super(username, familyName, givenName);
	}

	public AIPlayer(String username, String familyName, String givenName, int gamesPlayedCount, 
		int gamesWonCount, int gamesDrawnCount, double winRatio, double drawRatio) {

		super(username, familyName, givenName, gamesPlayedCount, gamesWonCount, gamesDrawnCount,
			winRatio, drawRatio);
	}

	/* Returns the calling AI player's next move and passes the move */ 
	public Move makeMove(char[][] gameBoard) {

		for (int row = 0; row < gameBoard.length; row++) {
			for (int column = 0; column < gameBoard[0].length; column++) {

				if (gameBoard[row][column] == EMPTY_SYMBOL) {
					Move move = new Move(row, column); 
					return move; 
				}
			}
		}

		return null; 
	}

	/* Makes a copy of the calling AI player and returns a reference to the copy */
	public AIPlayer makeCopy() {
		
		AIPlayer playerCopy = new AIPlayer(this.username, this.familyName, this.givenName,
			this.gamesPlayedCount, this.gamesWonCount, this.gamesDrawnCount, this.winRatio, 
			this.drawRatio);

		return playerCopy;
	}
}