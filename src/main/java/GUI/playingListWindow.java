package GUI;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * Classe che genera un'interfaccia grafica per visualizzare la lista degli
 * attuali giocatori.
 */
public class playingListWindow {

	private JFrame frame;
	private JTextArea textArea;
	private int i;

	/**
	 * Costruttore che si occupa di generare, a partire da un arrayList di
	 * stringhe, la lista dei giocatori attuali.
	 */
	public playingListWindow(ArrayList<String> playingList) {

		frame = new JFrame("Lista giocatori attuali");

		StringBuffer textAreaStringBuf = new StringBuffer();
		for (i = 1; i < playingList.size(); i++) {
			String temp = playingList.get(i) + "\n";
			textAreaStringBuf.append(temp);
		}
		String textAreaString = textAreaStringBuf.toString();

		textArea = new JTextArea(textAreaString);
		textArea.setEditable(false);

		frame.getContentPane().add(new JScrollPane(textArea),
				BorderLayout.CENTER);

		frame.pack();

		frame.setVisible(true);

		frame.setLocationRelativeTo(null);

		frame.setResizable(false);
	}

}
