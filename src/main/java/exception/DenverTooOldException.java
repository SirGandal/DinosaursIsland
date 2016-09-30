package exception;

/**
 * @author  gas12n
 */
public class DenverTooOldException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8443286079677011198L;
	private String error;

	public DenverTooOldException() {
		error = "remove";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}
}
