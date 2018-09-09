/* 
 * Class Invariant: all objects have username, family name, and given name strings, games played, 
 * games won, and games drawn counts, and win and draw ratios. 
 */

public class HumanPlayer extends Player {
	
	/* Constructors */ 
	public HumanPlayer() {}

	public HumanPlayer(String username, String familyName, String givenName) {

		super(username, familyName, givenName);
	}

	public HumanPlayer(String username, String familyName, String givenName, int gamesPlayedCount, 
		int gamesWonCount, int gamesDrawnCount, double winRatio, double drawRatio) {

		super(username, familyName, givenName, gamesPlayedCount, gamesWonCount, gamesDrawnCount,
			winRatio, drawRatio);
	}

	/* Gets a move entered by a human player from the command line and returns the move */ 
	public Move makeMove(char[][] gameBoard) {

		//get move from user input
		Move move = new Move(); 
		move.setRow(TicTacToe.keyboard.nextInt());
		move.setColumn(TicTacToe.keyboard.nextInt()); 

		TicTacToe.keyboard.nextLine(); //consume rest of line
		
		return move; 
	}

	/* Makes a copy of the calling human player and returns a reference to the copy */ 
	public HumanPlayer makeCopy() {

		HumanPlayer playerCopy = new HumanPlayer(this.username, this.familyName, this.givenName,
			this.gamesPlayedCount, this.gamesWonCount, this.gamesDrawnCount, this.winRatio, 
			this.drawRatio);

		return playerCopy;
	}
}