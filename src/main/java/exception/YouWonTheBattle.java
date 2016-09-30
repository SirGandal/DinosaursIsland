package exception;

import javax.swing.*;

public class YouWonTheBattle extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9206553980103979163L;

	public void youWonTheBattlePopUp(JFrame frameWindow) {
		JOptionPane
				.showMessageDialog(
						frameWindow,
						"Ti sei imbattuto in un altro dinosauro ed hai vinto il combattimento.",
						"Combattimento", JOptionPane.WARNING_MESSAGE);
	}

}
