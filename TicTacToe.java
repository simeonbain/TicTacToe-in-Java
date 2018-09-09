/*
 * COMP90041 Project C
 * @author Simeon Bain <bainsimeon@gmail.com> 
 * @version 1.0
 * @since 2016-05-20
 */ 

import java.util.Scanner;

public class TicTacToe {

	public static Scanner keyboard = new Scanner(System.in);

	private static final int USERNAME_TOKEN = 0;
	private static final int FAMILY_NAME_TOKEN = 1;
	private static final int GIVEN_NAME_TOKEN = 2;
	private static final int PLAYER_1_TOKEN = 0; 
	private static final int PLAYER_2_TOKEN = 1;
	private static final int NUM_ADD_PLAYER_ARGUMENTS = 3;  
	private static final int NUM_ADD_AI_PLAYER_ARGUMENTS = 3;  
	private static final int NUM_EDIT_PLAYER_ARGUMENTS = 3; 
	private static final int NUM_PLAY_GAME_ARGUMENTS = 2; 
	private static final String COMMAND_ARGUMENT_DELIMITER = ",";

	private enum Command {
		EXIT("exit"),
		ADD_PLAYER("addplayer"), 
		ADD_AI_PLAYER("addaiplayer"),
		REMOVE_PLAYER("removeplayer"), 
		EDIT_PLAYER("editplayer"),  
		RESET_STATS("resetstats"), 
		DISPLAY_PLAYER("displayplayer"),
		RANKINGS("rankings"),
		PLAY_GAME("playgame");

		private final String input; 

		Command(String input) {
			this.input = input; 
		}
	}

	public static void main(String[] args) {

		TicTacToe gameSystem = new TicTacToe(); 
		gameSystem.run();
	}

	/* Manages the running of a game system of TicTacToe */ 
	private void run() {

		PlayerManager playerManager = new PlayerManager(); 
		GameManager gameManager = new GameManager(); 

		System.out.println("Welcome to Tic Tac Toe!");
		System.out.println(); 

		playerManager.importPlayerArray(); //import player data from file
		
		while(true) { 
			//keep prompting user for command
			System.out.print(">");

			try {
				Command command = readInCommand();	
				executeCommand(command, playerManager, gameManager); 
			}
			catch (InvalidCommandException e) {
				System.out.println(e.getMessage());
				keyboard.nextLine(); //consume rest of line
			}
			catch (NumberOfCommandArgumentsException e) {
				System.out.println(e.getMessage());
			}

			System.out.println(); 
		}
	}

	/* Reads in user input and returns the corresponding command. */ 
	private Command readInCommand() throws InvalidCommandException { 
		
		String userInput = keyboard.next(); 

		if (userInput.equals(Command.EXIT.input)) {
			return Command.EXIT;  
		} else if (userInput.equals(Command.ADD_PLAYER.input)) {
			return Command.ADD_PLAYER;
		} else if (userInput.equals(Command.ADD_AI_PLAYER.input)) {
			return Command.ADD_AI_PLAYER;
		} else if (userInput.equals(Command.REMOVE_PLAYER.input)) {
			return Command.REMOVE_PLAYER;
		} else if (userInput.equals(Command.EDIT_PLAYER.input)) {
			return Command.EDIT_PLAYER; 
		} else if (userInput.equals(Command.RESET_STATS.input)) {
			return Command.RESET_STATS; 
		} else if (userInput.equals(Command.DISPLAY_PLAYER.input)) {
			return Command.DISPLAY_PLAYER; 
		} else if (userInput.equals(Command.RANKINGS.input)) {
			return Command.RANKINGS;
		} else if (userInput.equals(Command.PLAY_GAME.input)) {
			return Command.PLAY_GAME;
		} else {
			throw new InvalidCommandException(
				"\'" + userInput + "\' is not a valid command.");
		}
	}

	/* Executes command using the Player Manager and Game Manager */ 
	private void executeCommand(Command command, PlayerManager playerManager, 
		GameManager gameManager) throws NumberOfCommandArgumentsException {

		String[] commandArguments = null; //stores tokens for each user input command argument

		//make sure command isn't null
		if (command == null) {
			return; 
		}

		switch (command) {
			case EXIT: 
				playerManager.exportPlayerArray(); //save player data to file
				exitProgram();
				break; 

			case ADD_PLAYER:
				commandArguments = readInCommandArguments(); 

				//check for erroneus input
				if (commandArguments.length < NUM_ADD_PLAYER_ARGUMENTS) {
					throw new NumberOfCommandArgumentsException(
						"Incorrect number of arguments supplied to command."); 
				}

				playerManager.addPlayer(commandArguments[USERNAME_TOKEN], 
					commandArguments[FAMILY_NAME_TOKEN], 
					commandArguments[GIVEN_NAME_TOKEN]);
				break; 

			case ADD_AI_PLAYER:
				commandArguments = readInCommandArguments(); 

				//check for erroneus input
				if (commandArguments.length < NUM_ADD_AI_PLAYER_ARGUMENTS) {
					throw new NumberOfCommandArgumentsException(
						"Incorrect number of arguments supplied to command."); 
				}

				playerManager.addAIPlayer(commandArguments[USERNAME_TOKEN],
					commandArguments[FAMILY_NAME_TOKEN], 
					commandArguments[GIVEN_NAME_TOKEN]);
				break; 

			case REMOVE_PLAYER:
				commandArguments = readInCommandArguments(); 

				playerManager.removePlayer(commandArguments[USERNAME_TOKEN]);
				break; 

			case EDIT_PLAYER:
				commandArguments = readInCommandArguments(); 

				//check for erroneus input
				if (commandArguments.length < NUM_EDIT_PLAYER_ARGUMENTS) {
					throw new NumberOfCommandArgumentsException(
						"Incorrect number of arguments supplied to command."); 
				}

				playerManager.editPlayer(commandArguments[USERNAME_TOKEN], 
					commandArguments[FAMILY_NAME_TOKEN], 
					commandArguments[GIVEN_NAME_TOKEN]);
				break; 

			case RESET_STATS:
				commandArguments = readInCommandArguments(); 

				playerManager.resetStats(commandArguments[USERNAME_TOKEN]);
				break; 

			case DISPLAY_PLAYER:
				commandArguments = readInCommandArguments(); 

				playerManager.displayPlayer(commandArguments[USERNAME_TOKEN]);
				break; 

			case RANKINGS: 
				playerManager.displayRanking();
				break;

			case PLAY_GAME:
				commandArguments = readInCommandArguments(); 
				
				//check for erroneus input
				if (commandArguments.length < NUM_PLAY_GAME_ARGUMENTS) {
					throw new NumberOfCommandArgumentsException(
						"Incorrect number of arguments supplied to command."); 
				}

				//get copies of both players involved in game from playerManager 
				Player player1 = playerManager.getPlayer(
					commandArguments[PLAYER_1_TOKEN]);
				Player player2 = playerManager.getPlayer(
					commandArguments[PLAYER_2_TOKEN]);

				//check both players exist
				if (player1 == null || player2 == null) {
					System.out.println("Player does not exist.");
					return; 
				}

				gameManager.playGame(player1, player2);

				//update playerManager
				playerManager.setPlayer(player1);
				playerManager.setPlayer(player2);
				break; 

			default: 
				//none of the expected commands, do nothing
		}
	}

	/* Reads in user input and returns a tokenized string array */ 
	private String[] readInCommandArguments() {
		
		//read in command arguments as tokens, removing excess whitespace at ends
		String[] commandArguments = keyboard.nextLine().trim().split(COMMAND_ARGUMENT_DELIMITER);
		return commandArguments; 
	}

	/* Prints a line, then safely exits */ 
	private void exitProgram() {

		System.out.println(); 
		System.exit(0);
	}
}