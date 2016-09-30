package GUI;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.*;
import javax.swing.*;

import client.*;
import exception.InvalidTokenException;
import exception.RaceNullException;
import exception.TooPlayingUserException;
import exception.UserAlreadyEnteredInGame;

/**
 * Classe che si occupa di fare partire l'interfaccia grafica per il menu' del
 * gioco dalla quale sara' possibile accedere al gioco, creare una nuova razza,
 * osservare la classifica, richiedere la lista degli attuali giocatori o di
 * effettuare il logout.
 */
public class MenuGame {

	private JFrame frameWindow;

	private ImageIcon imageBack;
	private JLabel background;

	private JLabel usernameLabel;

	private JButton createRaceButton;
	private JButton logoutButton;
	private JButton playButton;
	private JButton rankingButton;
	private JButton currentPlayersButton;

	private JTextArea playInfoTextArea;
	private JTextArea createRaceTextArea;
	private JTextArea rankingTextArea;
	private JTextArea playingListTextArea;
	private JTextArea logoutTextArea;

	private Toolkit toolkit; /* prendo la dimensione dello schermo */

	private Dimension dim; /*
							 * variabile che conterra' la dimensione dello
							 * schermo
							 */

	public MenuGame(final ClientComunicator cc, final String token,
			final JFrame frameWindowHome) {
		/*
		 * prendo info sulla risoluzione dello schermo
		 */
		toolkit = Toolkit.getDefaultToolkit();
		dim = toolkit.getScreenSize();

		frameWindow = new JFrame(
				"L'isola dei dinosauri - Menu' - Created by Gioele Salvatore Antoci & Sergio Andaloro (Politecnico di Milano 2010/2011)");

		frameWindow.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				try {
					cc.logOut(token);
					frameWindowHome.setVisible(true);
					frameWindow.dispose();
				} catch (InvalidTokenException e1) {
				} catch (RemoteException e1) {
					JOptionPane
							.showMessageDialog(
									frameWindow,
									"RMI. Problema, contattare l'amministratore di rete.",
									"Errore", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		imageBack = new ImageIcon("src/main/resources/menu1024x600.jpg");

		background = new JLabel(imageBack);
		background.setBounds(0, 0, imageBack.getIconWidth(),
				imageBack.getIconHeight());
		background.setSize(dim.width, dim.height);

		usernameLabel = new JLabel("Utente: " + Home.getUsername());

		createRaceButton = new JButton("Crea una nuova razza");
		createRaceTextArea = new JTextArea(
				"Se non l'hai gia' fatto crea una nuova razza ed entra\n"
						+ "anche tu a far parte del mondo de L'isola dei Dinosauri");
		createRaceTextArea.setVisible(false);

		createRaceButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseEntered(MouseEvent e) {
				createRaceTextArea.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				createRaceTextArea.setVisible(false);
			}

			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});

		createRaceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new CreateRace(cc, token);
			}
		});

		logoutButton = new JButton("Logout");
		logoutTextArea = new JTextArea(
				"Esci dal menu per tornare alla home ed avrai la\n"
						+ "possibilita' di effettuare un'altra registrazione o accedere\n"
						+ "nuovamente al menu' de L'isola dei dinosauri.");
		logoutTextArea.setVisible(false);

		logoutButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseEntered(MouseEvent e) {
				logoutTextArea.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				logoutTextArea.setVisible(false);
			}

			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});

		logoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					cc.logOut(token);
					frameWindowHome.setVisible(true);
					frameWindow.dispose();
				} catch (InvalidTokenException e1) {
					e1.invalidTokenExceptionPopUp(frameWindow);
				} catch (RemoteException e1) {
					JOptionPane
							.showMessageDialog(
									frameWindow,
									"RMI. Problema, contattare l'amministratore di rete.",
									"Errore", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		playButton = new JButton("Gioca");
		playInfoTextArea = new JTextArea(
				"Entra a far parte del mondo dei dinosauri.\n"
						+ "Il mondo non sara' piu' lo stesso!");
		playInfoTextArea.setVisible(false);

		playButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseEntered(MouseEvent e) {
				playInfoTextArea.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				playInfoTextArea.setVisible(false);
			}

			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});

		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					cc.gameAccess(token);
					frameWindow.setVisible(false);
					new WindowGame(cc, token, frameWindow);
				} catch (UserAlreadyEnteredInGame e1) {
					e1.userAlreadyEnteredInGamePopUp(frameWindow);
				} catch (InvalidTokenException e1) {
					e1.invalidTokenExceptionPopUp(frameWindow);
				} catch (TooPlayingUserException e1) {
					e1.tooPlayingUserExceptionPopUp(frameWindow);
				} catch (RaceNullException e1) {
					e1.raceNullExceptionPopUp(frameWindow);
				} catch (RemoteException e1) {
					JOptionPane
							.showMessageDialog(
									frameWindow,
									"RMI. Problema, contattare l'amministratore di rete.",
									"Errore", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		rankingButton = new JButton("Classifica");
		rankingTextArea = new JTextArea(
				"Visualizza la classifica globale con tutti i risultati\n"
						+ "fino ad oggi ottenuti da tutti gli utenti che hanno giocato.");
		rankingTextArea.setVisible(false);

		rankingButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseEntered(MouseEvent e) {
				rankingTextArea.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				rankingTextArea.setVisible(false);
			}

			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});

		rankingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new rankingWindow(cc.giveMeRanking(token));
				} catch (InvalidTokenException e1) {
					e1.invalidTokenExceptionPopUp(frameWindow);
				} catch (RemoteException e1) {
					JOptionPane
							.showMessageDialog(
									frameWindow,
									"RMI. Problema, contattare l'amministratore di rete.",
									"Errore", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		currentPlayersButton = new JButton("Chi sta giocando");
		playingListTextArea = new JTextArea(
				"Visualizza chi attualmente sta giocando.");
		playingListTextArea.setVisible(false);

		currentPlayersButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseEntered(MouseEvent e) {
				playingListTextArea.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				playingListTextArea.setVisible(false);
			}

			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});

		currentPlayersButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				ArrayList<String> playingList = new ArrayList<String>();
				try {
					playingList = cc.playingList(token);
					new playingListWindow(playingList);
				} catch (InvalidTokenException e1) {
					e1.invalidTokenExceptionPopUp(frameWindow);
				} catch (RemoteException e1) {
					JOptionPane
							.showMessageDialog(
									frameWindow,
									"RMI. Problema, contattare l'amministratore di rete.",
									"Errore", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		frameWindow.setLayout(new FlowLayout());

		background.setLayout(null);

		usernameLabel.setBounds(5, 0, 260, 40);
		playButton.setBounds(386, 180, 260, 40);
		playInfoTextArea.setBounds(686, 180, 300, 40);
		createRaceButton.setBounds(386, 230, 260, 40);
		createRaceTextArea.setBounds(686, 230, 300, 40);
		rankingButton.setBounds(386, 280, 260, 40);
		rankingTextArea.setBounds(686, 280, 300, 40);
		currentPlayersButton.setBounds(386, 330, 260, 40);
		playingListTextArea.setBounds(686, 330, 300, 40);
		logoutButton.setBounds(386, 380, 260, 40);
		logoutTextArea.setBounds(686, 380, 310, 60);

		/*
		 * TODO adattare le dimensioni alla risoluzione dello schermo
		 */

		background.add(usernameLabel);
		background.add(createRaceButton);
		background.add(createRaceTextArea);
		background.add(logoutButton);
		background.add(logoutTextArea);
		background.add(playButton);
		background.add(playInfoTextArea);
		background.add(rankingButton);
		background.add(rankingTextArea);
		background.add(currentPlayersButton);
		background.add(playingListTextArea);

		frameWindow.add(background);

		frameWindow.setPreferredSize(new Dimension(dim.width, dim.width));

		frameWindow.pack();

		frameWindow.setVisible(true);

		// frameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frameWindow.setResizable(false);
	}
}
