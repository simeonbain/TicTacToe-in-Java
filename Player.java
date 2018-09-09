/* 
 * Class Invariant: all objects have username, family name, and given name strings, games played, 
 * games won, and games drawn counts, and win and draw ratios. 
 */

import java.io.Serializable; 

public abstract class Player implements Serializable {

	private static final double EPSILON = 0.00001; 
	private static final int EQUAL = 0; 
	private static final int LESS_THAN = -1; 
	private static final int GREATER_THAN = 1; 

	protected String username;
	protected String familyName; 
	protected String givenName; 
	protected int gamesPlayedCount; 
	protected int gamesWonCount; 
	protected int gamesDrawnCount; 
	protected double winRatio; //ratio between wins and games played
	protected double drawRatio; //ratio between draws and games played

	/* Constructors */ 
	protected Player() {

		this.gamesPlayedCount = 0; 
		this.gamesWonCount = 0; 
		this.gamesDrawnCount = 0; 
		this.winRatio = 0; 
		this.drawRatio = 0; 
	} 

	protected Player(String username, String familyName, String givenName) {

		this.username = username;
		this.familyName = familyName;
		this.givenName = givenName;
		this.gamesPlayedCount = 0;
		this.gamesWonCount = 0;
		this.gamesDrawnCount = 0;
		this.winRatio = 0; 
		this.drawRatio = 0; 
	}

	protected Player(String username, String familyName, String givenName, int gamesPlayedCount, 
		int gamesWonCount, int gamesDrawnCount, double winRatio, double drawRatio) {

		this.username = username;
		this.familyName = familyName;
		this.givenName = givenName;
		this.gamesPlayedCount = gamesPlayedCount;
		this.gamesWonCount = gamesWonCount;
		this.gamesDrawnCount = gamesDrawnCount;
		this.winRatio = winRatio; 
		this.drawRatio = drawRatio; 
	}    

	/* Accessors */
	public String getUsername() {

		return new String(this.username);
	}

	public String getFamilyName() {

		return new String(this.familyName); 
	}

	public String getGivenName() {

		return new String(this.givenName); 
	}

	public int getGamesPlayedCount() {

		return this.gamesPlayedCount;
	}

	public int getGamesWonCount() {

		return this.gamesWonCount; 
	}

	public int getGamesDrawnCount() {

		return this.gamesDrawnCount;
	}

	public double getWinRatio() {

		return this.winRatio;
	}

	public double getDrawRatio() {

		return this.drawRatio;
	}

	/* Mutators */
	public void setUsername(String username) {

		this.username = username; 
	}

	public void setFamilyName(String familyName) {

		this.familyName = familyName; 
	}

	public void setGivenName(String givenName) {

		this.givenName = givenName;
	}

	public void incrementGamesPlayedCount() {

		this.gamesPlayedCount++; 
	}

	public void incrementGamesWonCount() {

		this.gamesWonCount++; 
	}

	public void incrementGamesDrawnCount() {

		this.gamesDrawnCount++; 
	}

	/* Returns a string of player information */ 
	public String toString() {

		return username + "," + familyName + "," + givenName + "," + gamesPlayedCount + 
			" games," + gamesWonCount + " wins," + gamesDrawnCount + " draws";
	}

	/* Resets a player's stats */ 
	public void resetStats() {

		gamesPlayedCount = 0; 
		gamesDrawnCount = 0;
		gamesWonCount = 0; 
		winRatio = 0;
		drawRatio = 0; 
	}

	/* Recalculates win ratio based on wins and games played */ 
	public void updateWinRatio() {

		double num = (double) gamesWonCount;
		double den = (double) gamesPlayedCount;
		this.winRatio = num/den; 
	}

	/* Recalculates win ratio based on draws and games played */ 
	public void updateDrawRatio() {

		double num = (double) gamesDrawnCount;
		double den = (double) gamesPlayedCount;
		this.drawRatio = num/den; 
	}	

	/* Compares calling player with another player and returns the player with the highest win 
	 * ratio. If a tie, returns the player with the highest draw ratio. If still a tie, returns the 
	 * player whose username occurs first by alphabetical order.
	 */
	public int compareRank(Player otherPlayer) {

		if (this.compareWinRatio(otherPlayer) == GREATER_THAN) {
			return GREATER_THAN; 
		} else if (this.compareWinRatio(otherPlayer) == LESS_THAN) {
			return LESS_THAN;
		} else {
			// equal, try to break tie by draw ratio
			if (this.compareDrawRatio(otherPlayer) == GREATER_THAN) {
				return GREATER_THAN;
			} else if (this.compareDrawRatio(otherPlayer) == LESS_THAN) {
				return LESS_THAN;
			} else {
				// equal, break tie by username alphabetically
				if (this.username.compareTo(otherPlayer.username) < 0) {
					return GREATER_THAN; 
				} else {
					return LESS_THAN; 
				}
			}
		}
	}

	/* Compares calling player's win ratio with another player's win ratio. 
	 * Returns -1 if calling player's win ratio is less than the other player's.
	 * Returns 0 if win ratios are equal (within +/- epsilon of each other).
	 * Returns 1 if calling player's win ratio is greater than the other player's. 
	 */
	private int compareWinRatio(Player otherPlayer) {

		//check for equality
		if (winRatio + EPSILON > otherPlayer.winRatio &&
			winRatio - EPSILON < otherPlayer.winRatio) {
			return EQUAL; 
		} else if (winRatio < otherPlayer.winRatio) {
			return LESS_THAN;
		} else {
			return GREATER_THAN;
		}
	}

	/* Compares calling player's draw ratio with another player's draw ratio. 
	 * Returns -1 if calling player's draw ratio is less than the other player's.
	 * Returns 0 if draw ratios are equal (within +/- epsilon of each other).
	 * Returns 1 if calling player's draw ratio is greater than the other player's. 
	 */
	private int compareDrawRatio(Player otherPlayer) {

		//check for equality
		if (drawRatio + EPSILON > otherPlayer.drawRatio &&
			drawRatio - EPSILON < otherPlayer.drawRatio) {
			return EQUAL; 
		} else if (drawRatio < otherPlayer.drawRatio) {
			return LESS_THAN;
		} else {
			return GREATER_THAN;
		}
	}

	/* Returns the calling player's selected next move */ 
	protected abstract Move makeMove(char[][] gameBoard);
	
	/* Makes a copy of the calling player and returns a reference to the copy */ 
	protected abstract Player makeCopy();
}


