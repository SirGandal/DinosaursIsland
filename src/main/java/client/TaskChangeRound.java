package client;

import GUI.WindowGame;

/**
 * @author  gas12n
 */
public class TaskChangeRound extends Thread {

	private String username;
	/**
	 * @uml.property  name="winGame"
	 * @uml.associationEnd  
	 */
	private WindowGame winGame;

	public TaskChangeRound(String username, WindowGame winGame) {
		super();
		this.username = username;
		this.winGame = winGame;
	}

	@Override
	public void run() {
		winGame.changeRound(username);
	}

}
