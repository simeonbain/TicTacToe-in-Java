/* 
 * Class that handles exceptions thrown by not having a valid input command. 
 */ 

public class InvalidCommandException extends Exception {
	
	/* Constructors */ 
	public InvalidCommandException() {
		super();
	}

	public InvalidCommandException(String message) {
		super(message);
	} 

	public InvalidCommandException(Throwable cause) {
		super(cause);
	}

	public InvalidCommandException(String message, Throwable cause) {
		super(message, cause);
	}
}