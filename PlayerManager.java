/* 
 * Class that manages all the players in the Tic Tac Toe game system and executes operations on the 
 * set of players. Players are stored in an array called playerArray. When the display ranking 
 * method is called they are also stored in another array called rankingAray which is sorted 
 * according to player rank. The number of players is kept track of and stored in playerCount.
 */

import java.util.Arrays;
import java.lang.Math;
import java.io.ObjectOutputStream; 
import java.io.FileOutputStream; 
import java.io.ObjectInputStream; 
import java.io.FileInputStream; 
import java.io.IOException;
import java.io.FileNotFoundException; 

public class PlayerManager {

	private static final int MAX_PLAYER_COUNT = 100; 
	private static final int NOT_FOUND = -1;
	private static final int PERCENT_MULTIPLIER = 100;
	private static final int MAX_RANKS_TO_PRINT = 10; 
	private static final String EMPTY_STRING = "";

	private Player[] playerArray = new Player[MAX_PLAYER_COUNT];
	private Player[] rankingArray;
	private int playerCount;

	/* Constructor */
	public PlayerManager() {

		this.playerCount = 0; 
	} 

	/* Accessors */
	public Player getPlayer(String username) {

		int playerIndex = findPlayerIndex(username);
		if (playerIndex == NOT_FOUND) {
			return null; 
		}

		return playerArray[playerIndex].makeCopy();
	}

	public Player[] getPlayerArray() {

		return Arrays.copyOf(this.playerArray, this.playerArray.length);
	}

	public Player[] getRankingArray() {

		return Arrays.copyOf(this.rankingArray, this.rankingArray.length);
	}

	public int getPlayerCount() {

		int playerCountCopy = playerCount;
		return playerCountCopy;
	}

	/* Mutator */ 
	public void setPlayer(Player player) {

		int playerIndex = findPlayerIndex(player.getUsername());
		
		playerArray[playerIndex] = player; 
	}

	/* Creates a new human player with the input username, family name, and given name and then adds  
	 * it to the player array (that stores all the players in the game system) while maintaining 
	 * alphabetic order within array.
	 */
	public void addPlayer(String username, String familyName, String givenName) {

		//check if username already taken
		if (findPlayerIndex(username) != NOT_FOUND) {
			System.out.println("The username has been used already.");
			return; 
		}

		//create player
		HumanPlayer newPlayer = new HumanPlayer(username, familyName, givenName);

		//work out where to insert player into array to satisfy alphabetic order
		for (int i = 0; i < playerCount; i++) {

			if (newPlayer.getUsername().compareTo(playerArray[i].getUsername()) < 0) {
				insertElement(newPlayer, i);
				return; 
			} 
		}

		//must be last by alphabetic order 
		insertElement(newPlayer, playerCount);
	}

	/* Creates a new AI player with the input username, family name, and given name and then adds  
	 * it to the player array (that stores all the players in the game system) while maintaining 
	 * alphabetic order within array.
	 */
	public void addAIPlayer(String username, String familyName, String givenName) {

		//check if username already taken
		if (findPlayerIndex(username) != NOT_FOUND) {
			System.out.println("The username has been used already.");
			return; 
		}

		//create player
		AIPlayer newPlayer = new AIPlayer(username, familyName, givenName);

		//work out where to insert player into array to satisfy alphabetic order
		for (int i = 0; i < playerCount; i++) {

			if (newPlayer.getUsername().compareTo(playerArray[i].getUsername()) < 0) {
				insertElement(newPlayer, i);
				return; 
			} 
		}

		//must be last by alphabetic order 
		insertElement(newPlayer, playerCount);
	}

	/* Locates player corresponding to the input username and removes it from the player array that 
	 * stores all the players in the game system. If no username given, removes all players from
	 * player array. 
	 */
	public void removePlayer(String username) {

		//check for remove all players command
		if (username.equals(EMPTY_STRING)) {

			System.out.println("Are you sure you want to remove all players? (y/n)");

			//get input
			if (TicTacToe.keyboard.nextLine().toLowerCase().equals("y")) {
				//remove all players by reseting player count so all elements may be overwritten
				playerCount = 0; 
			} 
			return; 
		}

		//find player
		int playerIndex = findPlayerIndex(username);
		if (playerIndex == NOT_FOUND) {
			System.out.println("The player does not exist.");
			return; 
		}

		//remove from array
		removeElement(playerIndex);	
	}

	/* Locates player in player array corresponding to the input username and updates player with
	 * the given family name and given name.
	 */ 
	public void editPlayer(String username, String familyName, String givenName) {

		//find player in player array
		int playerIndex = findPlayerIndex(username);

		if (playerIndex == NOT_FOUND) {
			System.out.println("The player does not exist.");
			return; 
		}

		//edit parameters
		playerArray[playerIndex].setFamilyName(familyName);
		playerArray[playerIndex].setGivenName(givenName);
	}

	/* Locates player in player array corresponding to the input username and resets the player's
	 * gameplay statistics. If no username given, resets the player statistics of all players in 
	 * the player array. 
	 */ 
	public void resetStats(String username) {

		//check for reset all player stats command
		if (username.equals(EMPTY_STRING)) {

			//get input
			System.out.println("Are you sure you want to reset all player statistics? (y/n)");

			if (TicTacToe.keyboard.nextLine().toLowerCase().equals("y")) {

				for (int i = 0; i < playerCount; i++) {
					playerArray[i].resetStats();
				}
			} 
			return; 
		}

		//find player
		int playerIndex = findPlayerIndex(username);
		if (playerIndex == NOT_FOUND) {
			System.out.println("The player does not exist.");
			return; 
		}

		//reset parameters
		playerArray[playerIndex].resetStats(); 
	}

	/* Locates player in player array corresponding to the input username and displays the 
	 * information of the player. If no username given, displays the information of all players in 
	 * the player array. 
	 */
	public void displayPlayer(String username) {

		//check for display all players command
		if (username.equals(EMPTY_STRING)) {
			for (int i = 0; i < playerCount; i++) {
				System.out.println(playerArray[i].toString()); 
			}
			return; 
		}

		//find player 
		int playerIndex = findPlayerIndex(username);
		if (playerIndex == NOT_FOUND) {
			System.out.println("The player does not exist.");
			return; 
		}

		//print the player
		System.out.println(playerArray[playerIndex].toString()); 
	}

	/* Prints a leaderboard of the top players in the game system based on ranking. */
	public void displayRanking() {
		
		//copy over players from player array that stores all the players in the game system
		rankingArray = Arrays.copyOf(playerArray, playerCount);

		//sort players by ranking
		sortRankingArray();

		//print 
		printRankingArray(); 
	}

	/* Sorts the ranking array in order of descending rank using a selection sort algorithm */
	private void sortRankingArray() {

		Player topPlayer; //stores top player found so far in unsorted section of array
		int topPlayerIndex; 

		//increment size of sorted section/decrement size of unsorted section until array sorted
		for (int i = 0; i < playerCount; i++) {

			topPlayer = rankingArray[i]; //reset topPlayer
			topPlayerIndex = i; 

			//look through unsorted section of array
			for (int j = i; j < playerCount; j++) {

				//if higher ranked player found, make new top player
				if (rankingArray[j].compareRank(topPlayer) > 0) {
					
					topPlayer = rankingArray[j];
					topPlayerIndex = j;  
				}
			}

			//insert top player found in unsorted section into the next slot of the sorted section 
			rankingArray[topPlayerIndex] = rankingArray[i];
			rankingArray[i] = topPlayer; 
		}
	}

	/* Prints the ranking array, with additonal formatting, up to a predefined number of players */
	private void printRankingArray() {

		System.out.println(" WIN  | DRAW | GAME | USERNAME");

		for (int i = 0; i < playerCount && i < MAX_RANKS_TO_PRINT; i++) {
			System.out.printf(" %3d%%", 
					Math.round(rankingArray[i].getWinRatio()*PERCENT_MULTIPLIER)); 
			System.out.print(" | ");
			System.out.printf("%3d%%", 
				Math.round(rankingArray[i].getDrawRatio()*PERCENT_MULTIPLIER));
			System.out.print(" | ");
			System.out.printf("%2d  ", rankingArray[i].getGamesPlayedCount());
			System.out.print(" | ");
			System.out.println(rankingArray[i].getUsername());
		}
	}

	/* Searches the player array and returns the index of the player that corresponds to the input 
	 * username. If no player found, returns -1.
	 */ 
	private int findPlayerIndex(String username) {
 
		for (int i = 0; i < playerCount; i++) {
			if (playerArray[i].getUsername().equals(username)) {
				return i; 
			}
		}

		//no player with that username
		return NOT_FOUND; 
	}

	/* Inserts player into the player array at the specified index */
	private void insertElement(Player player, int elementIndex) {

		//shuffle up elements to make space at appropriate index
		for (int i = playerCount; i > elementIndex; i--) {
			playerArray[i] = playerArray[i-1];
		}

		//insert element
		playerArray[elementIndex] = player; 
		playerCount++; 
	}

	/* Removes player from the player array at the specified index */ 
	private void removeElement(int elementIndex) {
		
		//shuffle down elements to overwrite the element
		for (int i = elementIndex; i < playerCount-1; i++) {
			playerArray[i] = playerArray[i+1];
		}

		playerArray[playerCount-1] = null; 
		playerCount--; //decrement player count
	}

	/* Saves the player array to file in a binary format */ 
	public void exportPlayerArray() {

		try {
			FileOutputStream fileOut = new FileOutputStream("players.dat");
			ObjectOutputStream outputStream = new ObjectOutputStream(fileOut);

			outputStream.writeObject(playerArray);
			outputStream.close(); 
			fileOut.close(); 
		} 
		catch (IOException e) {
			System.out.println("Problem writing to file.");
			System.exit(0);
		}
	}

	/* Imports a previously saved player array from a file in a binary format */ 
	public void importPlayerArray() {

		try {
			FileInputStream fileIn = new FileInputStream("players.dat");
			ObjectInputStream inputStream = new ObjectInputStream(fileIn);

			playerArray = (Player[]) inputStream.readObject(); 
			
			//update player count
			for (int i = 0; i < playerArray.length && playerArray[i] != null; i++) {
				playerCount++;
			}

			inputStream.close(); 
			fileIn.close(); 
		} 
		catch (FileNotFoundException e) {
			//assume this is the first instance of TicTacToe so do nothing
		}
		catch (IOException e) {
			System.out.println("Problems with file input.");
			System.exit(0);
		} 
		catch (ClassNotFoundException e) {
			System.out.println("Problems with file input.");
			System.exit(0);
		}
	}
}
