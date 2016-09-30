package GUI;

import java.awt.event.*;
import javax.swing.*;

/**
 * Action listener che viene dichiarato su ogni bottone della mappa locale.
 * Quando verra' chiamato in causa chiamera' mostrera' le informazioni relative
 * alla mappa locale.
 */
public class LocalMapMouseListener implements MouseListener {

	private JLabel infoLocalMapLabel;

	public LocalMapMouseListener(JLabel localViewInfoLabel) {
		this.infoLocalMapLabel = localViewInfoLabel;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		infoLocalMapLabel.setVisible(true);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		infoLocalMapLabel.setVisible(false);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

}
