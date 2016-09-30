package exception;

/**
 * @author  gas12n
 */
public class UserNonExistentException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 884509985484309374L;
	/**
	 * 
	 */
	private String error;

	public UserNonExistentException() {
		error = "User non esistente";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}
}
