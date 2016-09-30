package server;

/**
 * Interfaccia con tutti i metodi necessari al server per comunicare con il
 * client.
 */

public interface ServerComunicator {

	/**
	 * @see server.ServerSocketComunicator#start()
	 */
	public void start();

	/**
	 * 
	 * @param username
	 * @param password
	 * 
	 * @see server.ServerSocketComunicator#createNewUser(String, String)
	 */
	public void createNewUser(String username, String password);

	/**
	 * @return
	 * @see server.ServerSocketComunicator#logUsers(String, String)
	 */
	public void logUsers(String username, String password);

	/**
	 * @see server.ServerSocketComunicator#createNewRace(String, String, String)
	 */
	public void createNewRace(String token, String name, String type);

	/**
	 * @see server.ServerSocketComunicator#gameAccess(String)
	 */
	public void gameAccess(String token);

	/**
	 * @see server.ServerSocketComunicator#gameOut(String)
	 */
	public void gameOut(String token);

	/**
	 * @see server.ServerSocketComunicator#playingList(String)
	 */
	public void playingList(String token);

	/**
	 * @see server.ServerSocketComunicator#logOut(String)
	 */
	public void logOut(String token);

	/**
	 * @see server.ServerSocketComunicator#giveMeRanking(String)
	 */
	public void giveMeRanking(String token);

	/**
	 * @see server.ServerSocketComunicator#showMap(String)
	 */
	public void showMap(String token);

	/**
	 * @see server.ServerSocketComunicator#showLocalView(String, String)
	 */
	public void showLocalView(String token, String idDino);

	/**
	 * @see server.ServerSocketComunicator#denverList(String)
	 */
	public void denverList(String token);

	/**
	 * @see server.ServerSocketComunicator#denverState(String, String)
	 */
	public void denverState(String token, String idDino);

	/**
	 * @see server.ServerSocketComunicator#denverMove(String, String, int, int)
	 */
	public void denverMove(String token, String idDino, int rowDest, int colDest);

	/**
	 * @see server.ServerSocketComunicator#growingUp(String, String)
	 */
	public void growingUp(String idDino, String token);

	/**
	 * @see server.ServerSocketComunicator#layAnEgg(String, String)
	 */
	public void layAnEgg(String idDino, String token);

	/**
	 * @see server.ServerSocketComunicator#acceptRound(String)
	 */
	public void acceptRound(String token);

	/**
	 * @see server.ServerSocketComunicator#passRound(String)
	 */
	public void passRound(String token);

	public void changeRound(String currentUser);
}
