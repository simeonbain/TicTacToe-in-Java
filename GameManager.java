/*
 * Class that manages a single game of Tic Tac Toe between two players. Stores the game grid as 
 * an array of chars, where each char element is an empty space, or a symbol. Updates stats of 
 * each player upon completion of a game. 
 */

import java.util.Arrays;

public class GameManager {

	private static final int GRID_SIZE = 3; 
	private static final int MAX_MOVE_COUNT = GRID_SIZE*GRID_SIZE;
	private static final char PLAYER_1_SYMBOL = 'O';
	private static final char PLAYER_2_SYMBOL = 'X';
	private static final char EMPTY_SYMBOL = ' ';

	private enum GameState {
		PLAYER_O_WON, PLAYER_X_WON, DRAW, GAME_CONTINUES
	}

	private char[][] grid = new char[GRID_SIZE][GRID_SIZE];

	/* Constructor */ 
	public GameManager() {}

	/* Accessor */ 
	public char[][] getGrid() {

		return Arrays.copyOf(this.grid, this.grid.length);
	}

	/* Runs a single TicTacToe game betwen the two input players. Determines winner (or draw), 
	 * and updates stats of each player. 
	 */
	public void playGame(Player player1, Player player2) {

		//start with clean grid
		resetGrid(); 
		printGrid(); 

		GameState gameState = GameState.GAME_CONTINUES; 

		Player currentPlayer = player1; //Player O gets the first move
		Move move = null; //stores coordinates of each move
		int moveCount = 0; 

		while (gameState == GameState.GAME_CONTINUES) {

			System.out.println(currentPlayer.getGivenName() +"\'s move:");

			//get move
			move = currentPlayer.makeMove(grid);

			while (isValidMove(move) == false) {
				//move not allowed, get new move
				System.out.println(currentPlayer.getGivenName() +"\'s move:");
				move = currentPlayer.makeMove(grid);
			}

			//update grid
			if (currentPlayer == player1) {
				grid[move.getRow()][move.getColumn()] = PLAYER_1_SYMBOL;
				currentPlayer = player2; //switch players
			} else {
				grid[move.getRow()][move.getColumn()] = PLAYER_2_SYMBOL;
				currentPlayer = player1; //switch players
			}
			moveCount++;

			printGrid();
			gameState = getGameState(moveCount);
		}

		//game over, print result, update player stats
		if (gameState == GameState.PLAYER_O_WON) {
			System.out.println("Game over. " + player1.getGivenName() + " won!");
			player1.incrementGamesWonCount(); 
		} else if (gameState == GameState.PLAYER_X_WON) {
			System.out.println("Game over. " + player2.getGivenName() + " won!");
			player2.incrementGamesWonCount(); 
		} else {
			System.out.println("Game over. It was a draw!");
			player1.incrementGamesDrawnCount();
			player2.incrementGamesDrawnCount();
		}

		player1.incrementGamesPlayedCount();
		player1.updateWinRatio(); 
		player1.updateDrawRatio(); 

		player2.incrementGamesPlayedCount(); 
		player2.updateWinRatio(); 
		player2.updateDrawRatio(); 
	}

	/* Clears the game grid and resets to initial state */ 
	private void resetGrid() {

		for (int row = 0; row < GRID_SIZE; row++) {
			for (int column = 0; column < GRID_SIZE; column++) {

				grid[row][column] = EMPTY_SYMBOL;
			}
		}
	}

	/* Prints the game grid, formatted as a TicTacToe board */ 
	private void printGrid() {

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

	/* Checks if input row and column represents a valid move. Returns boolean true if valid, 
	 * false otherwise.
	 */
	private boolean isValidMove(Move move) {

		if (move.getRow() > GRID_SIZE-1 || move.getColumn() > GRID_SIZE-1 ||
			move.getRow() < 0 || move.getColumn() < 0) {
			//outside the grid
			System.out.print("Invalid move. You must place at a cell within "); 
			printValidDimensions(); 
			System.out.println(".");
			return false;  
		}

		if (grid[move.getRow()][move.getColumn()] != EMPTY_SYMBOL) { 
			//space already occupied
			System.out.println("Invalid move. The cell has been occupied."); 
			return false;  
		} 

		return true; 
	}

	/* Prints out the dimensions of the game grid in the format, "{0,1,...,n} {0,1,...,n}", where 
	 * n is the number of rows and columns in the grid.
	 */ 
	private void printValidDimensions() {

			System.out.print("{");

			for (int i = 0; i < GRID_SIZE; i++) {
				if (i < GRID_SIZE-1) {
					System.out.print(i + ",");
				} else {
					System.out.print(i + "} {");
				}
			}

			for (int i = 0; i < GRID_SIZE; i++) {
				if (i < GRID_SIZE-1) {
					System.out.print(i + ",");
				} else {
					System.out.print(i + "}");
				}
			} 
	}
	
	/* Checks for win or draw and returns appropriate game state. If no win or draw, returns the 
	 * 'game continues' game state. 
	 */ 
	private GameState getGameState(int moveCount) {
		
		//check each row for win
		for (int row = 0; row < GRID_SIZE; row++) {

			if (isWinInRow(row) == true) {
				if (grid[row][0] == PLAYER_1_SYMBOL) {
					return GameState.PLAYER_O_WON;
				} else {
					return GameState.PLAYER_X_WON;
				} 
			}
		}

		//check each column for win
		for (int column = 0; column < GRID_SIZE; column++) {

			if (isWinInColumn(column) == true) {
				if (grid[0][column] == PLAYER_1_SYMBOL) {
					return GameState.PLAYER_O_WON;
				} else {
					return GameState.PLAYER_X_WON;					
				} 
			}
		}
		
		//check diagonals for win
		if (isWinInLeftDiagonal() == true) {

			if (grid[0][0] == PLAYER_1_SYMBOL) {
				return GameState.PLAYER_O_WON;
			} else {
				return GameState.PLAYER_X_WON;
			} 
		}

		if (isWinInRightDiagonal() == true) {

			if (grid[0][GRID_SIZE-1] == PLAYER_1_SYMBOL) {
				return GameState.PLAYER_O_WON;
			} else {
				return GameState.PLAYER_X_WON;
			} 
		}
		
		//no win, check if draw
		if (moveCount >= MAX_MOVE_COUNT) {
			return GameState.DRAW; 
		} else {
			return GameState.GAME_CONTINUES;
		}
	}

	/* Checks each row and returns true if a win has occured, false otherwise */
	private boolean isWinInRow(int row) {

		for (int column = 0; column < GRID_SIZE-1; column++) {

			if (grid[row][column] == EMPTY_SYMBOL ||
				grid[row][column] != grid[row][column+1]) {
				
				return false;
			}
		}

		return true; 
	}

	/* Checks each column and returns true if a win has occured, false otherwise */
	private boolean isWinInColumn(int column) {

		for (int row = 0; row < GRID_SIZE-1; row++) {

			if (grid[row][column] == EMPTY_SYMBOL ||
				grid[row][column] != grid[row+1][column]) {

				return false;
			}
		}

		return true; 
	}

	/* Checks left diagonal (top left to bottom right) and returns true if a win has occured, 
	 * false otherwise.
	 */
	private boolean isWinInLeftDiagonal() {

		int count = 1; 
		for (int i = 0; i < GRID_SIZE-1; i++) {

			if (grid[i][i] != EMPTY_SYMBOL && grid[i][i] == grid[i+1][i+1]) {
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
	private boolean isWinInRightDiagonal() {
 
		int count = 1; 
		for (int i = 0, j = GRID_SIZE-1; i < GRID_SIZE-1; i++, j--) {

			if (grid[i][j] != EMPTY_SYMBOL && grid[i][j] == grid[i+1][j-1]) {
				count++; 
			}
		}

		if (count == GRID_SIZE) {
			return true; 
		} else {
			return false; 
		}
	}
}