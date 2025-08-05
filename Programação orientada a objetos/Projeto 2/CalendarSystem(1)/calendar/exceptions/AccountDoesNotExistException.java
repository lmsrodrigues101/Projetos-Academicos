package calendar.exceptions;

@SuppressWarnings("serial")
public class AccountDoesNotExistException extends RuntimeException {
	public AccountDoesNotExistException() {
		super();
	}
	
	/**
	 * @param message Name of the account that does not exist
	 */
	public AccountDoesNotExistException(String message) {
		super(message);
	}
}

