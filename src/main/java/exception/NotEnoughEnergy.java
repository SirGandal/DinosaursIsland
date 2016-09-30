package exception;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class NotEnoughEnergy extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3077778950233688540L;

	public NotEnoughEnergy() {
		System.out.println("Energia insufficiente, il dinosauro verra' ucciso");
	}

	public void NotEnoughEnergyPopUp(JFrame frameWindow) {
		/*
		 * JOptionPane.showMessageDialog(frameWindow,
		 * "Il dinosauro e' morto per mancanza di energia.",
		 * "Energia insufficiente", JOptionPane.WARNING_MESSAGE);
		 */

		ImageIcon imageBack = new ImageIcon(
				"src/main/resources/deathYoshi.jpg");

		JOptionPane
				.showMessageDialog(frameWindow,
						"Il dinosauro e' morto per mancanza di energia.",
						"Energia insufficiente", JOptionPane.WARNING_MESSAGE,
						imageBack);

	}

}
