package server;

import java.io.Serializable;

/**
 * @author  gas12n
 */
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6959568284778907577L;
	private String username = new String();
	private String password = new String();
	private String race;
	protected boolean[][] userMap;

	private int access = 0; // numero di accessi univoco

	/**
	 * @param username
	 * @param password
	 * @param race
	 * @param type
	 */
/*	public User(String username, String password, String race) {
		super();
		this.username = username;
		this.password = password;
		this.race = race;
		setUserMapToFalse();
	}*/

	/**
	 * @param username
	 * @param password
	 */
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
		setUserMap(setUserMapToFalse());
	}

	/**
	 * @return
	 * @uml.property  name="user"
	 */
	public boolean[][] getUserMap() {
		return userMap;
	}

	/**
	 * @param userMap
	 * @uml.property  name="user"
	 */
	public void setUserMap(boolean[][] userMap) {
		this.userMap = userMap.clone();
	}

	/**
	 * @return
	 * @uml.property  name="race"
	 */
	public String getRace() {
		return race;
	}

	/**
	 * @param race
	 * @uml.property  name="race"
	 */
	public void setRace(String race) {
		this.race = race;
	}

	/**
	 * @return
	 * @uml.property  name="access"
	 */
	public int getAccess() {
		return access;
	}

	/**
	 * @param access
	 * @uml.property  name="access"
	 */
	public void setAccess(int access) {
		this.access = access;
	}

	/**
	 * @return
	 * @uml.property  name="username"
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return
	 * @uml.property  name="password"
	 */
	public String getPassword() {
		return password;
	}
/**
 * Setta la mappa delle visibilita' dell'utente a false
 * @return Ritorna la mappa delle visibitilita' aggiornata
 */
	public boolean[][] setUserMapToFalse() {
		userMap = new boolean[ServerSocketComunicator.D][ServerSocketComunicator.D];
		for (int i = 0; i < userMap.length; i++) {
			for (int j = 0; j < userMap.length; j++) {
				userMap[i][j] = false;
			}
		}
		return userMap;
	}
}
