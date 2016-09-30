package server;

/**
 * @author  gas12n
 */
public class LoggedUser extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3304217313293521677L;
	private String token;

/**
 *  * Costruttore con userMap della classe Logged User
 * @param username Username dell'utente loggato
 * @param password Password dell'utente loggato
 * @param token Token dell'utente loggato
 * @param access Numero di accessi
 * @param race Razza associata all'utente eventualmente con valore pari a NULL
 * @param userMap Mappa di visibilita' locale all'utente
 */
	public LoggedUser(String username, String password, String token,
			int access, String race, boolean[][] userMap) {
		super(username, password);
		this.token = token;
		super.setAccess(access);
		super.setRace(race);
	}
/**
 * Costruttore della classe Logged User
 * @param username Username dell'utente loggato
 * @param password Password dell'utente loggato
 * @param token Token dell'utente loggato
 * @param access Numero di accessi
 * @param race Razza associata all'utente eventualmente con valore pari a NULL
 */
	public LoggedUser(String username, String password, String token,
			int access, String race) {
		super(username, password);
		this.token = token;
		super.setAccess(access);
		super.setRace(race);
	}

	/**
	 * @return
	 * @uml.property  name="token"
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 * @uml.property  name="token"
	 */
	public void setToken(String token) {
		this.token = token;
	}

}
