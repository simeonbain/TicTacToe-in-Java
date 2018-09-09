/* 
 * Class that handles exceptions thrown by not having enough input arguments for a TicTacToe command. 
 */ 

public class NumberOfCommandArgumentsException extends Exception {
	
	/* Constructors */ 
	public NumberOfCommandArgumentsException() {
		super();
	}

	public NumberOfCommandArgumentsException(String message) {
		super(message);
	} 

	public NumberOfCommandArgumentsException(Throwable cause) {
		super(cause);
	}

	public NumberOfCommandArgumentsException(String message, Throwable cause) {
		super(message, cause);
	}
}