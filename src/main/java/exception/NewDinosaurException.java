package exception;

/**
 * @author  gas12n
 */
public class NewDinosaurException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4281754342082583142L;
	private String error;

	public NewDinosaurException() {
		error = "create";
	}

	/**
	 * @return
	 * @uml.property  name="error"
	 */
	public String getError() {
		return error;
	}
}
