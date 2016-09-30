package exception;

/**
 * @author  gas12n
 */
public class RaceTooOldException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7551268325934597232L;
	String error;

	public RaceTooOldException() {
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
