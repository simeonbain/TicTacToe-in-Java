/* 
 * Class Invariant: all objects have username, family name, and given name strings, games played, 
 * games won, and games drawn counts, and win and draw ratios. 
 */

public class AdvancedAIPlayer extends Player {
	
	private static final int GRID_SIZE = 3; 
	private static final char PLAYER_O_SYMBOL = 'O';
	private static final char PLAYER_X_SYMBOL = 'X';
	private static final char EMPTY_SYMBOL = ' ';
	private static final int MAX_AVAILABLE_MOVES = GRID_SIZE*GRID_SIZE;  
	
	private enum Outcome {
		WIN, LOSE, DRAW
	}

	private enum Turn {
		OPPOSITION, AI
	}

	/* Constructors */ 
	public AdvancedAIPlayer() {}

	public AdvancedAIPlayer(String username, String familyName, String givenName) {

		super(username, familyName, givenName);
	}

	public AdvancedAIPlayer(String username, String familyName, String givenName, 
		int gamesPlayedCount, int gamesWonCount, int gamesDrawnCount, double winRatio, 
		double drawRatio) {

		super(username, familyName, givenName, gamesPlayedCount, gamesWonCount, gamesDrawnCount,
			winRatio, drawRatio);
	}

	/* Returns the best possible move for the AI player from the available moves by calling a method
	 * that implements a Minimax algorithm. 
	 */ 
	public Move makeMove(char[][] gameBoard) {

		Move bestMove = new Move(); 

		minimax(gameBoard, bestMove, Turn.AI);

		return bestMove; 
	}

	/* Implements the Minimax algorithm that recursively determines the (equal) best possible
	 * outcome for any given turn, returns this outcome, and updates the bestMove variable with the 
	 * available move that leads to this outcome. 
	 * 
	 * The Minimax algorithm chooses moves that maximize the outcome of the AI player, and minimize
	 * the outcome of the Opposition player in any given recursion. Each recursion alternates the 
	 * player who holds the current turn. So if the AI holds the current turn, the maximum outcome 
	 * possible from this position will be returned, otherwise, the minimum outcome will be returned
	 */ 
	private Outcome minimax(char[][] gameBoard, Move bestMove, Turn turn) {

		//check if win has occured 
		if (isWin(gameBoard) && turn == Turn.OPPOSITION) {

			return Outcome.WIN;
			
		} else if (isWin(gameBoard) && turn == Turn.AI) {
			printGrid(gameBoard);
			System.out.println(); 
			return Outcome.LOSE; 
		} else if (isGameOver(gameBoard)) {
			//must be draw
			return Outcome.DRAW; 
		} else {
			if (gameBoard[0][1] == PLAYER_O_SYMBOL && 
				gameBoard[1][1] == PLAYER_O_SYMBOL && 
				gameBoard[2][1] == PLAYER_O_SYMBOL) {
				return Outcome.LOSE; 
			}
		}

		Move[] moves = new Move[MAX_AVAILABLE_MOVES];
		int moveIndex = 0; 
		int symbolOCount = 0; 
		int symbolXcount = 0; 

		//get all available moves 
		for (int row = 0; row < GRID_SIZE; row++) {
			for (int column = 0; column < GRID_SIZE; column++) {

				if (gameBoard[row][column] == EMPTY_SYMBOL) {
					//empty, place into moves array
					Move move = new Move(row,column);
					moves[moveIndex] = move; 
					moveIndex++; 
				} else if (gameBoard[row][column] == PLAYER_O_SYMBOL) { 
					symbolOCount++; 
				} else {
					symbolXcount++; 
				}
			}
		}

		//work out which symbol the current player should use
		char symbol = PLAYER_O_SYMBOL;
		if (symbolOCount > symbolXcount) {
			symbol = PLAYER_X_SYMBOL;
		} 

		//Min-Max calculation

		Outcome outcome = null; 
		Outcome bestOutcome = null; 

		if (turn == Turn.AI) {
			bestOutcome = Outcome.LOSE; 

			for (int i = 0; i < moveIndex; i++) {
				//generate the game board resulting from each possible move
				char[][] possibleGameboard = makeGameBoardCopy(gameBoard);
				possibleGameboard[moves[i].getRow()][moves[i].getColumn()] = symbol;

				//recurse
				outcome = minimax(possibleGameboard, bestMove, Turn.OPPOSITION);

				//maximize outcome
				if (outcome == Outcome.WIN) {
					bestOutcome = outcome; 
					bestMove.setRow(moves[i].getRow()); 
					bestMove.setColumn(moves[i].getColumn());
				} else if ((bestOutcome != Outcome.WIN) && (outcome == Outcome.DRAW)) {
					bestOutcome = outcome; 
					bestMove.setRow(moves[i].getRow()); 
					bestMove.setColumn(moves[i].getColumn());
				}	
			}

			return bestOutcome; 

		} else {
			bestOutcome = Outcome.WIN; 

			for (int i = 0; i < moveIndex; i++) {
				//generate the game board resulting from each possible move
				char[][] possibleGameboard = makeGameBoardCopy(gameBoard);
				possibleGameboard[moves[i].getRow()][moves[i].getColumn()] = symbol;

				//recurse
				outcome = minimax(possibleGameboard, bestMove, Turn.AI);

				//maximize outcome
				if (outcome == Outcome.LOSE) {
					bestOutcome = outcome; 
					bestMove.setRow(moves[i].getRow()); 
					bestMove.setColumn(moves[i].getColumn());
				} else if ((bestOutcome != Outcome.LOSE) && (outcome == Outcome.DRAW)) {
					bestOutcome = outcome; 
					bestMove.setRow(moves[i].getRow()); 
					bestMove.setColumn(moves[i].getColumn());
				}	
			}

			return bestOutcome; 
		}
	}

	private char switchSymbol(char symbol) { 

		if (symbol == EMPTY_SYMBOL) {
			return EMPTY_SYMBOL; 
		} else if (symbol == PLAYER_O_SYMBOL) {
			return PLAYER_X_SYMBOL;
		} else {
			return PLAYER_O_SYMBOL;
		}
	}

	/* Makes a copy of the gameboard and returns the reference to the copy */ 
	private char[][] makeGameBoardCopy(char[][] gameBoard) {

		char[][] gameBoardCopy = new char[GRID_SIZE][GRID_SIZE]; 

		for (int row = 0; row < GRID_SIZE; row++) {
			for (int column = 0; column < GRID_SIZE; column++) {

				gameBoardCopy[row][column] = gameBoard[row][column];
			}
		}

		return gameBoardCopy; 
	}

	/* Checks gameboard to see if the game has been completed. Returns true if game is over, false
	 * otherwise.
	 */ 
	private boolean isGameOver(char[][] gameBoard) {

		//check each row and column to see if any moves still available
		for (int row = 0; row < GRID_SIZE; row++) {
			for (int column = 0; column < GRID_SIZE; column++) {

				if (gameBoard[row][column] == EMPTY_SYMBOL) {
					return false; 
				}
			}
		}

		// no moves available, game must be over
		return true; 
	}

	/* Checks gameboard to see if a win has occured. Returns true if win has occured, false 
	 * otherwise.
	 */ 
	private boolean isWin(char[][] gameBoard) {
		
		//check each row for win
		for (int row = 0; row < GRID_SIZE; row++) {
			if (isWinInRow(gameBoard, row) == true) {
				return true; 
			}
		}

		//check each column for win
		for (int column = 0; column < GRID_SIZE; column++) {
			if (isWinInColumn(gameBoard, column) == true) {
				return true; 
			}
		}
		
		//check diagonals for win
		if (isWinInLeftDiagonal(gameBoard) == true) {
			return true; 
		}

		if (isWinInRightDiagonal(gameBoard) == true) {
			return true; 
		}
		
		//no win
		return false; 
	}

	/* Checks each row and returns true if a win has occured, false otherwise */
	private boolean isWinInRow(char[][] gameBoard, int row) {

		for (int column = 0; column < GRID_SIZE-1; column++) {

			if (gameBoard[row][column] == EMPTY_SYMBOL ||
				gameBoard[row][column] != gameBoard[row][column+1]) {
				
				return false;
			}
		}

		return true; 
	}

	/* Checks each column and returns true if a win has occured, false otherwise */
	private boolean isWinInColumn(char[][] gameBoard, int column) {

		for (int row = 0; row < GRID_SIZE-1; row++) {

			if (gameBoard[row][column] == EMPTY_SYMBOL ||
				gameBoard[row][column] != gameBoard[row+1][column]) {

				return false;
			}
		}

		return true; 
	}

	/* Checks left diagonal (top left to bottom right) and returns true if a win has occured, 
	 * false otherwise.
	 */
	private boolean isWinInLeftDiagonal(char[][] gameBoard) {

		int count = 1; 
		for (int i = 0; i < GRID_SIZE-1; i++) {

			if (gameBoard[i][i] != EMPTY_SYMBOL && gameBoard[i][i] == gameBoard[i+1][i+1]) {
				count++; 
			}
		}

		if (count == GRID_SIZE) {
			return true; 
		} else {
			return false; 
		}
	}

	/* Checks right diagonal (top right to bottom left) and returns true if a win has occured, 
	 * false otherwise.
	 */
	private boolean isWinInRightDiagonal(char[][] gameBoard) {
 
		int count = 1; 
		for (int i = 0, j = GRID_SIZE-1; i < GRID_SIZE-1; i++, j--) {

			if (gameBoard[i][j] != EMPTY_SYMBOL && gameBoard[i][j] == gameBoard[i+1][j-1]) {
				count++; 
			}
		}

		if (count == GRID_SIZE) {
			return true; 
		} else {
			return false; 
		}
	}

	/* Makes a copy of the calling Advanced AI Player and returns a reference to the copy */ 
	public AdvancedAIPlayer makeCopy() {

		AdvancedAIPlayer playerCopy = new AdvancedAIPlayer(this.username, this.familyName, 
			this.givenName, this.gamesPlayedCount, this.gamesWonCount, this.gamesDrawnCount, 
			this.winRatio, this.drawRatio);

		return playerCopy;
	}	

	/* Prints the game grid, formatted as a TicTacToe board */ 
	private void printGrid(char[][] grid) {

		for (int row = 0; row < GRID_SIZE; row++) {
			for (int column = 0; column < GRID_SIZE; column++) {

				System.out.print(grid[row][column]);

				if (column < GRID_SIZE-1) {
					System.out.print("|");
				}
			}

			System.out.println();

			if (row < GRID_SIZE-1) {
				for (int i = 0; i < 2*GRID_SIZE-1; i++) {
					System.out.print("-");
				}
				System.out.println(); 
			}
		}
	}
}
