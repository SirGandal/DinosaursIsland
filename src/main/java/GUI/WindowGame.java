package GUI;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import client.ClientComunicator;
import exception.*;
import map.Cell;

/**
 * Classe che identifica la finestra di gioco. Da questa finestra si potra'
 * interagire con il server tramite il comunicatore client e muoversi
 * all'interno della mappa, avere una visione generale della mappa, uscire dal
 * gioco, richiedere la classifica generale o la lista dei giocatori, cambiare
 * il colore di come i dinosauri di un determinato utente vengono visti
 * all'interno della mappa generale e della vista locale, o ancora di deporre un
 * uovo, crescere o passare il turno.
 */
public class WindowGame {
	
	private int i, j;
	private int relativeStartingRowDinosaur = 0;
	private int relativeStartingColDinosaur = 0;

	private JFrame frameWindow;
	private JFrame frameNotMyTurn;
	private JLabel usernameLabel;

	private JComboBox colorsAvailable;
	private JLabel colorLabel;

	private JPanel mapPanel;
	private JButton[][] mapButtons;
	private Dimension d;
	private JLabel[][] localViewInfoLabel;
	private Color waterColor, blindColor, earthColor, borderWaterColor,
			grassColor, defaultDinosaurColor, enemyDinosaurColor;
	private Color grassLevel1Color, grassLevel2Color, grassLevel3Color,
			grassLevel4Color;
	private Color carrionLevel1Color, carrionLevel2Color, carrionLevel3Color,
			carrionLevel4Color;

	private JPanel rightPanel;

	private JButton gameOutButton;
	private JButton rankingButton;
	private JButton currentPlayersButton;

	private JPanel passPanel;
	private JPanel panelTimer30;
	private MyTimer timer30;
	private JPanel actionsPanel;
	private JPanel actionsButtonPanel;
	private JButton growButton;
	private JButton makeEggButton;
	private JButton passButton;
	private JButton confirmButton;
	private MyTimer timer120;
	private JPanel panelTimer;
	private JTextField infoActions;

	private boolean localViewEnabled = false;
	private JPanel dinosaurMapPanel;
	private JButton[][] dinosaurMapButtons;
	private ImageIcon imageBackHerbivorousLevel1 = new ImageIcon(
			"src/main/resources/DinosaurLevels/YoshiGreenLevel1.jpg");
	private ImageIcon imageBackHerbivorousLevel2 = new ImageIcon(
			"src\\main\\resources\\DinosaurLevels/YoshiGreenLevel2.jpg");
	private ImageIcon imageBackHerbivorousLevel3 = new ImageIcon(
			"src/main/resources/DinosaurLevels/YoshiGreenLevel3.jpg");
	private ImageIcon imageBackHerbivorousLevel4 = new ImageIcon(
			"src/main/resources/DinosaurLevels/YoshiGreenLevel4.jpg");
	private ImageIcon imageBackHerbivorousLevel5 = new ImageIcon(
			"src/main/resources/DinosaurLevels/YoshiGreenLevel5.jpg");
	private ImageIcon imageBackCarnivorousLevel1 = new ImageIcon(
			"src/main/resources/DinosaurLevels/YoshiRedLevel1.jpg");
	private ImageIcon imageBackCarnivorousLevel2 = new ImageIcon(
			"src/main/resources/DinosaurLevels/YoshiRedLevel2.jpg");
	private ImageIcon imageBackCarnivorousLevel3 = new ImageIcon(
			"src/main/resources/DinosaurLevels/YoshiRedLevel3.jpg");
	private ImageIcon imageBackCarnivorousLevel4 = new ImageIcon(
			"src/main/resources/DinosaurLevels/YoshiRedLevel4.jpg");
	private ImageIcon imageBackCarnivorousLevel5 = new ImageIcon(
			"src/main/resources/DinosaurLevels/YoshiRedLevel5.jpg");
	private JLabel imageDin = new JLabel();

	/*
	 * contiene la distanza massima fino a dove puo' arrivare il dinosauro. La
	 * massima distanza sara' 9 per il dinosauro di livello 4 e 5. per tutti gli
	 * altri e' inferiore
	 */
	private static final int maxLocalView = 9;
	private int borderWater;
	private int offset;
	private static final int dimMyMap = 50;
	private Cell[][] map;

	private Dimension dLocalDin;
	private int energy;

	private String[] dinosaurList;

	private static final int maxNumDin = 5;
	private int[][] coordOfDinosaurs;
	private String[] statusDinosaur;
	private JLabel localDinosaurLevelLabel;
	private JLabel localDinosaurEnergyLabel;
	private JLabel localDinosaurLivedRoundsLabel;
	private Border myRaisedBorderActive = BorderFactory.createBevelBorder(
			BevelBorder.RAISED, Color.red, Color.red);
	private Border myRaisedBorderInactive = BorderFactory.createBevelBorder(
			BevelBorder.LOWERED, Color.black, Color.black);
	/*
	 * PROVA private MyLocalMapButton[][] myLocalButtons; JFrame frameProva =
	 * new JFrame();
	 */

	/*
	 * l'array di stringhe sopra contiene le info su un dinosauro gli indici
	 * sotto indicano quali informazioni si trovano a tale indice in modo da
	 * facilitarne l'individuazione
	 */
	private static final int indexOfUser = 0, indexOfRace = 1, indexOfType = 2,
			indexOfDinosaurColumn = 3, indexOfDinosaurRow = 4,
			indexOfLevel = 5, indexOfEnergy = 6, indexOfLivedRounds = 7;

	private Dimension dim;

	public JPanel getActionsPanel() {
		return actionsPanel;
	}

	public JPanel getDinosaurMapPanel() {
		return dinosaurMapPanel;
	}

	public JLabel getImageDin() {
		return imageDin;
	}

	public JLabel getLocalDinosaurLevelLabel() {
		return localDinosaurLevelLabel;
	}

	public JLabel getLocalDinosaurEnergyLabel() {
		return localDinosaurEnergyLabel;
	}

	public JLabel getLocalDinosaurLivedRoundsLabel() {
		return localDinosaurLivedRoundsLabel;
	}

	private JFrame menuWindow;
	
	private String token;
	private ClientComunicator cc;

	/**
	 * Costruttuore che crea l'interfaccia grafica della finestra di gioco.
	 * 
	 * @param cc
	 *            Comunicatore client per chiamare i metodi opportuni al momento
	 *            dell'intereazione dell'utente con l'interfaccia.
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * @param menuWindow
	 *            Finestra del menu'. (Se l'utente effettua il logout verra'
	 *            resa visibile.)
	 */
	public WindowGame(final ClientComunicator cc, final String token,
			final JFrame menuWindow) {
		
		this.cc = cc;
		this.token = token;
		
		this.menuWindow = menuWindow;

		/*
		 * prendo info sulla risoluzione dello schermo
		 */
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		dim = toolkit.getScreenSize();

		frameWindow = new JFrame(
				"L'isola dei dinosauri - Ambiente di gioco - Created by Gioele Salvatore Antoci & Sergio Andaloro (Politecnico di Milano 2010/2011)");

		frameWindow.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowDeactivated(WindowEvent e) {
			}

			public void windowClosed(WindowEvent e) {
			}

			public void windowActivated(WindowEvent e) {
			}

			public void windowClosing(WindowEvent e) {
				try {
					cc.gameOut(token);
					frameWindow.dispose();
					menuWindow.setVisible(true);
				} catch (UserAlreadyExited e1) {
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

		grassColor = new Color(127, 255, 0);
		waterColor = new Color(0, 178, 238);
		blindColor = new Color(128, 128, 128);
		earthColor = new Color(139, 69, 19);
		borderWaterColor = new Color(28, 134, 238);
		defaultDinosaurColor = new Color(255, 255, 255);
		enemyDinosaurColor = new Color(0, 0, 0);

		buildFirstMainMap(cc, token);

		actionsPanel = new JPanel();
		TitledBorder actionsTitle = BorderFactory
				.createTitledBorder("Puoi eseguire le seguenti azioni");
		;
		actionsTitle.setTitleJustification(TitledBorder.CENTER);
		actionsPanel.setBorder(actionsTitle);

		infoActions = new JTextField("info");
		infoActions.setEditable(false);

		growButton = new JButton("Cresci");
		makeEggButton = new JButton("Deposita uovo");
		passButton = new JButton("Passa turno");
		confirmButton = new JButton("Conferma");

		actionsButtonPanel = new JPanel();

		actionsButtonPanel.setLayout(new GridLayout(4, 1));
		actionsPanel.setLayout(new GridLayout(1, 1));

		JFrame frameTimer120 = new JFrame();
		timer120 = new MyTimer(frameTimer120, 120);
		panelTimer = new JPanel();
		panelTimer.setLayout(new GridLayout(1, 1));
		TitledBorder timerTitle = BorderFactory
				.createTitledBorder("Tempo rimanente alla fine del tuo turno");
		timerTitle.setTitleJustification(TitledBorder.CENTER);
		panelTimer.setBorder(timerTitle);
		panelTimer.add(timer120.getWaiter());

		confirmButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					cc.confirm(token);
					confirmButton.setBorder(myRaisedBorderInactive);
					passButton.setBorder(myRaisedBorderInactive);
					timer30.stopTimer();
					timer120.startTimer();
				} catch (InvalidTokenException e1) {
				} catch (NotYourTurnException e1) {
					e1.notYourTurnExceptionPopUp(frameWindow);
				} catch (UserNotPlayingException e1) {
					JOptionPane.showMessageDialog(frameWindow,
							"GAME OVER! La tua razza si e' estinta.",
							"Razza estinta", JOptionPane.ERROR_MESSAGE);
					frameWindow.dispose();
				} catch (RemoteException e1) {
					JOptionPane
							.showMessageDialog(
									frameWindow,
									"RMI. Problema, contattare l'amministratore di rete.",
									"Errore", JOptionPane.WARNING_MESSAGE);
				}

			}
		});

		passButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					timer30.stopTimer();
					timer120.stopTimer();
					cc.pass(token);
				} catch (InvalidTokenException e1) {
				} catch (NotYourTurnException e1) {
					e1.notYourTurnExceptionPopUp(frameWindow);
				} catch (UserNotPlayingException e1) {
					JOptionPane.showMessageDialog(frameWindow,
							"GAME OVER! La tua razza si e' estinta.",
							"Razza estinta", JOptionPane.ERROR_MESSAGE);
					frameWindow.dispose();
				} catch (RemoteException e1) {
					JOptionPane
							.showMessageDialog(
									frameWindow,
									"RMI. Problema, contattare l'amministratore di rete.",
									"Errore", JOptionPane.WARNING_MESSAGE);
				}

			}
		});

		passPanel = new JPanel();
		passPanel.setLayout(new GridLayout(1, 3));

		JFrame frameTimer30 = new JFrame();
		timer30 = new MyTimer(frameTimer30, 30);
		panelTimer30 = new JPanel();
		panelTimer30.setLayout(new GridLayout(1, 1));
		TitledBorder timerTitle30 = BorderFactory
				.createTitledBorder("Accetta turno");
		timerTitle30.setTitleJustification(TitledBorder.CENTER);
		panelTimer30.setBorder(timerTitle30);
		panelTimer30.add(timer30.getWaiter());

		passPanel.add(panelTimer30);
		passPanel.add(confirmButton);
		passPanel.add(passButton);

		actionsButtonPanel.add(growButton);
		actionsButtonPanel.add(makeEggButton);
		actionsButtonPanel.add(passPanel);
		actionsButtonPanel.add(panelTimer);

		actionsPanel.add(actionsButtonPanel);
		// actionsPanel.add(infoActions);

		actionsPanel.setVisible(false);

		localViewInfoLabel = new JLabel[maxLocalView][maxLocalView];
		dinosaurMapPanel = new JPanel(
				new GridLayout(maxLocalView, maxLocalView));
		dinosaurMapButtons = new JButton[maxLocalView][maxLocalView];
		for (i = 0; i < maxLocalView; i++) {
			for (j = 0; j < maxLocalView; j++) {
				dinosaurMapButtons[i][j] = new JButton();
				localViewInfoLabel[i][j] = new JLabel();
				dinosaurMapPanel.add(dinosaurMapButtons[i][j]);
				dinosaurMapButtons[i][j].setPreferredSize(dLocalDin);
			}
		}
		localDinosaurLevelLabel = new JLabel();
		localDinosaurEnergyLabel = new JLabel();
		localDinosaurLivedRoundsLabel = new JLabel();

		/*
		 * PROVA myLocalButtons = new MyLocalMapButton[9][9];
		 * frameProva.setLayout(new GridLayout(9, 9));
		 * 
		 * for(i=0;i<maxLocalView;i++){ for(j=0;j<maxLocalView;j++){
		 * myLocalButtons[i][j] = new MyLocalMapButton();
		 * frameProva.add(myLocalButtons[i][j]); } }
		 * 
		 * frameProva.pack(); frameProva.setVisible(true);
		 * frameProva.setResizable(true);
		 */

		setDinosaursOnMap(cc, token);

		/*
		 * coloro i bordi di un colore per l'acqua differente dal resto
		 * dell'acqua
		 */
		borderWaterPainter();

		usernameLabel = new JLabel("Utente: " + Home.getUsername());

		colorLabel = new JLabel("di colore ");
		colorLabel.setVisible(false);

		String[] colors = { "bianco", "giallo", "arancione", "rosa" };
		colorsAvailable = new JComboBox(colors);
		colorsAvailable.setSelectedIndex(0);
		colorsAvailable.setVisible(false);

		colorsAvailable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				String c = (String) cb.getSelectedItem();
				// colorsAvailable.setVisible(false);
				// colorLabel.setVisible(false);

				if (c.equals("giallo")) {
					defaultDinosaurColor = new Color(255, 255, 0);
					setDinosaursOnMap(cc, token);
					if (localViewEnabled) {
						//System.out.println("Giallo");
						dinosaurMapButtons[maxLocalView / 2][maxLocalView / 2]
								.setBackground(defaultDinosaurColor);
					}
				} else if (c.equals("arancione")) {
					defaultDinosaurColor = new Color(255, 140, 0);
					setDinosaursOnMap(cc, token);
					if (localViewEnabled) {
						//System.out.println("Arancione");
						dinosaurMapButtons[maxLocalView / 2][maxLocalView / 2]
								.setBackground(defaultDinosaurColor);
					}
				} else if (c.equals("rosa")) {
					defaultDinosaurColor = new Color(255, 105, 180);
					setDinosaursOnMap(cc, token);
					if (localViewEnabled) {
						//System.out.println("Rosa");
						dinosaurMapButtons[maxLocalView / 2][maxLocalView / 2]
								.setBackground(defaultDinosaurColor);
					}
				} else if (c.equals("bianco")) {
					defaultDinosaurColor = new Color(255, 255, 255);
					setDinosaursOnMap(cc, token);
					if (localViewEnabled) {
						//System.out.println("Bianco");
						dinosaurMapButtons[maxLocalView / 2][maxLocalView / 2]
								.setBackground(defaultDinosaurColor);
					}
				}
			}
		});

		gameOutButton = new JButton("Esci");

		gameOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					cc.pass(token);
					cc.gameOut(token);
					frameWindow.dispose();
					menuWindow.setVisible(true);
				} catch (UserAlreadyExited e1) {
					e1.userAlreadyExitedPopUp(frameWindow);
				} catch (InvalidTokenException e1) {
					e1.invalidTokenExceptionPopUp(frameWindow);
				} catch (RemoteException e1) {
					JOptionPane
							.showMessageDialog(
									frameWindow,
									"RMI. Problema, contattare l'amministratore di rete.",
									"Errore", JOptionPane.WARNING_MESSAGE);
				} catch (NotYourTurnException e1) {
				} catch (UserNotPlayingException e1) {
					JOptionPane.showMessageDialog(frameWindow,
							"GAME OVER! La tua razza si e' estinta.",
							"Razza estinta", JOptionPane.ERROR_MESSAGE);
					frameWindow.dispose();
				}
			}
		});

		rankingButton = new JButton("Classifica");

		rankingButton.addActionListener(new ActionListener() {
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

		currentPlayersButton.addActionListener(new ActionListener() {
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

		rightPanel = new JPanel();
		rightPanel.setLayout(null);

		usernameLabel.setBounds(5, 5, 220, 20);
		colorLabel.setBounds(245, 5, 70, 20);
		colorsAvailable.setBounds(310, 5, 100, 20);
		gameOutButton.setBounds(5, 55, 130, 30);
		rankingButton.setBounds(143, 55, 130, 30);
		currentPlayersButton.setBounds(280, 55, 130, 30);
		actionsPanel.setBounds(5, 95, dim.width - dim.height - 20,
				(dim.height / 3) - 20);

		rightPanel.add(actionsPanel);
		rightPanel.add(usernameLabel);
		rightPanel.add(colorLabel);
		rightPanel.add(colorsAvailable);
		rightPanel.add(gameOutButton);
		rightPanel.add(rankingButton);
		rightPanel.add(currentPlayersButton);

		frameWindow.setLayout(null);

		mapPanel.setBounds(0, 0, dim.height, dim.height);
		rightPanel.setBounds(dim.height, 0, dim.width - dim.height, dim.height);

		frameWindow.add(mapPanel);
		frameWindow.add(rightPanel);

		frameWindow.setPreferredSize(new Dimension(dim.width, dim.width));

		frameWindow.pack();

		frameWindow.setVisible(true);

		// frameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frameWindow.setResizable(false);

		cc.setWindowGame(this);
	}

	public void changeRound(String username) {
		if (Home.getUsername().equals(username)) {

			new SoundAlert("src\\main\\resources\\Popup.wav").start();
			actionsPanel.setVisible(true);

			confirmButton.setBorder(myRaisedBorderActive);
			passButton.setBorder(myRaisedBorderActive);

			timer120.stopTimer();
			timer30.startTimer();
			
			try {
				cc.askForList(token);
				/*
				 * se quando non e' il suo turno la razza del giocatore
				 * si estingue viene lanciata l'eccezione dell'utente che non sta giocando
				 */
			} catch (RemoteException e) {
			} catch (InvalidTokenException e) {
			} catch (UserNotPlayingException e) {
				JOptionPane.showMessageDialog(frameWindow,
						  "GAME OVER! La tua razza si e' estinta.", "Razza estinta",
						  JOptionPane.ERROR_MESSAGE); getFrameWindow().dispose();
						  getMenuWindow().setVisible(true);
						  frameWindow.dispose();
			} catch (InvalidIdException e) {
			}
			
			/*
			 * se durante il turno di qualcun altro si estingue la razza viene
			 * lanciata un'eccezione
			 */
			localViewEnabled = false;
			dinosaurMapPanel.setVisible(false);
			localDinosaurEnergyLabel.setVisible(false);
			localDinosaurLevelLabel.setVisible(false);
			localDinosaurLivedRoundsLabel.setVisible(false);
			imageDin.setVisible(false);
		} else {
			confirmButton.setBorder(myRaisedBorderInactive);
			passButton.setBorder(myRaisedBorderInactive);

			timer30.stopTimer();
			timer120.stopTimer();
			
			JOptionPane
					.showMessageDialog(frameNotMyTurn, "E' il turno di "
							+ username, "Cambio Turno",
							JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Metodo che si occupa di costruire la prima mappa generale per un utente
	 * con un determinato token.
	 * 
	 * @param cc
	 *            Comunicatore client per chiamare i metodi relativi alle
	 *            richieste al server.
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * 
	 * @see client.ClientSocketComunicator#askForList(String)
	 * @see client.ClientSocketComunicator#askForMap(String)
	 */
	private void buildFirstMainMap(ClientComunicator cc, String token) {

		/*
		 * conterra' tutti gli id dei dinosauri dell'utente
		 */
		try {
			dinosaurList = cc.askForList(token);
		} catch (InvalidTokenException e2) {
			e2.invalidTokenExceptionPopUp(frameWindow);
		} catch (UserNotPlayingException e2) {
			// e2.userNotPlayingExceptionPopUp(frameWindow);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(frameWindow,
					"RMI. Problema, contattare l'amministratore di rete.",
					"Errore", JOptionPane.WARNING_MESSAGE);
		} catch (InvalidIdException e) {
		}

		/*
		 * ricevo un'array bidimensionale che conterra' solo 'a' 't' 'v' 'b' che
		 * mi servira' a costruire la topologia della mappa
		 */
		try {
			map = cc.askForMap(token);
			offset = (dimMyMap - map.length) / 2;
		} catch (InvalidTokenException e) {
			e.invalidTokenExceptionPopUp(frameWindow);
		} catch (UserNotPlayingException e) {
			// e.userNotPlayingExceptionPopUp(frameWindow);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(frameWindow,
					"RMI. Problema, contattare l'amministratore di rete.",
					"Errore", JOptionPane.WARNING_MESSAGE);
		}

		/*
		 * al dinosauro in di riga iesimo corrisponderanno le coordinate iesime
		 * nell'array di coordinate
		 */
		coordOfDinosaurs = new int[maxNumDin][2];

		/*
		 * per ogni dinosauro memorizzo nell'array coordOfDinosaur le coordinate
		 * di dove si trovano i miei dinosauri nella mappa
		 */
		for (i = 0; (i < dinosaurList.length) && (i < maxNumDin)
				&& (dinosaurList[i] != null); i++) {
			try {
				//System.out.println("LUNGHEZZA: " + map.length);
				statusDinosaur = cc.askForStatus(token, dinosaurList[i]);
				coordOfDinosaurs[i][0] = offset
						+ (map.length - 1 - Integer
								.parseInt(statusDinosaur[indexOfDinosaurRow]));
				/*
				 * da protoccolo in 4 posizione c'e' la Y - RIGA
				 */
				coordOfDinosaurs[i][1] = offset
						+ (Integer
								.parseInt(statusDinosaur[indexOfDinosaurColumn])); 
				/*
				 * da protoccolo in 3 posizione c'e' la X - COLONNA
				 */
				/*
				System.out.println("Dinosauro in " + coordOfDinosaurs[i][0]
						+ " " + coordOfDinosaurs[i][1]);
 				*/
			} catch (UserNotPlayingException e1) {
				// e1.userNotPlayingExceptionPopUp(frameWindow);
			} catch (InvalidTokenException e1) {
				e1.invalidTokenExceptionPopUp(frameWindow);
			} catch (InvalidIdException e1) {
				// e1.invalidIdExceptionPopUp(frameWindow);
			} catch (RemoteException e) {
				JOptionPane.showMessageDialog(frameWindow,
						"RMI. Problema, contattare l'amministratore di rete.",
						"Errore", JOptionPane.WARNING_MESSAGE);
			}
		}

		d = new Dimension(dim.height / dimMyMap, dim.height / dimMyMap);

		dLocalDin = d;

		dLocalDin.setSize(dLocalDin.width * 2.5, dLocalDin.height * 2.5);

		mapButtons = new JButton[dimMyMap][dimMyMap];
		for (i = dimMyMap - 1; i >= 0; i--) {
			for (j = 0; j < dimMyMap; j++) {
				mapButtons[i][j] = new JButton();
				mapButtons[i][j].setPreferredSize(d);
				mapButtons[i][j].setEnabled(false);
			}
		}

		mapPanel = new JPanel(new GridLayout(dimMyMap, dimMyMap));

		/*
		 * inizializzo la mappa aggiungendo i bottoni al pannello adatto
		 * inizializzando il colore di background in base alla topologia
		 */
		for (i = 0; i < dimMyMap; i++) {
			for (j = 0; j < dimMyMap; j++) {
				mapButtons[i][j].setBackground(blindColor);
				mapPanel.add(mapButtons[i][j]);
			}
		}

		for (i = offset; i < dimMyMap - offset; i++) {
			for (j = offset; j < dimMyMap - offset; j++) {
				if (!map[i - offset][j - offset].isVisible()) {
					mapButtons[i][j].setBackground(blindColor);
				} else if (map[i - offset][j - offset].isWater()) {
					mapButtons[i][j].setBackground(waterColor);
				} else if (map[i - offset][j - offset].isEarth()) {
					mapButtons[i][j].setBackground(earthColor);
				} else if (map[i - offset][j - offset].isVegetation()) {
					mapButtons[i][j].setBackground(grassColor);
				}
			}
		}
	}

	/**
	 * Metodo che si occupa di costruire la mappa generale, dopo la prima volta,
	 * per un utente con un determinato token.
	 * 
	 * @param cc
	 *            Comunicatore client per chiamare i metodi relativi alle
	 *            richieste al server.
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * 
	 * @see client.ClientSocketComunicator#askForList(String)
	 * @see client.ClientSocketComunicator#askForStatus(String, String)
	 * @see client.ClientSocketComunicator#askForMap(String)
	 */
	void buildMainMap(ClientComunicator cc, String token) {

		/*
		 * conterra' tutti gli id dei dinosauri dell'utente
		 */
		try {
			dinosaurList = cc.askForList(token);
		} catch (InvalidTokenException e2) {
			e2.invalidTokenExceptionPopUp(frameWindow);
		} catch (UserNotPlayingException e2) {
			// e2.userNotPlayingExceptionPopUp(frameWindow);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(frameWindow,
					"RMI. Problema, contattare l'amministratore di rete.",
					"Errore", JOptionPane.WARNING_MESSAGE);
		} catch (InvalidIdException e) {
		}

		/*
		 * ricevo un'array bidimensionale che conterra' solo 'a' 't' 'v' 'b' che
		 * mi servira' a costruire la topologia della mappa
		 */
		try {
			map = cc.askForMap(token);
			offset = (dimMyMap - map.length) / 2;
		} catch (InvalidTokenException e) {
			e.invalidTokenExceptionPopUp(frameWindow);
		} catch (UserNotPlayingException e) {
			// e.userNotPlayingExceptionPopUp(frameWindow);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(frameWindow,
					"RMI. Problema, contattare l'amministratore di rete.",
					"Errore", JOptionPane.WARNING_MESSAGE);
		}

		/*
		 * al dinosauro in di riga iesimo corrisponderanno le coordinate iesime
		 * nell'array di coordinate
		 */
		coordOfDinosaurs = new int[maxNumDin][2];

		/*
		 * per ogni dinosauro memorizzo nell'array coordOfDinosaur le coordinate
		 * di dove si trovano i miei dinosauri nella mappa
		 */
		for (i = 0; (i < dinosaurList.length) && (i < maxNumDin)
				&& (dinosaurList[i] != null); i++) {
			try {
				statusDinosaur = cc.askForStatus(token, dinosaurList[i]);
				coordOfDinosaurs[i][0] = offset
						+ (map.length - 1 - Integer
								.parseInt(statusDinosaur[indexOfDinosaurRow])); 
				/*
				 * da protoccolo in 4 posizione c'e' la Y - RIGA
				 */
				coordOfDinosaurs[i][1] = offset
						+ (Integer
								.parseInt(statusDinosaur[indexOfDinosaurColumn])); 
				/*
				 * da protoccolo in 3 posizione c'e' la X - COLONNA
				 */

				System.out.println("Dinosauro in " + coordOfDinosaurs[i][0]
						+ " " + coordOfDinosaurs[i][1]);

			} catch (UserNotPlayingException e1) {
				// e1.userNotPlayingExceptionPopUp(frameWindow);
			} catch (InvalidTokenException e1) {
				e1.invalidTokenExceptionPopUp(frameWindow);
			} catch (InvalidIdException e1) {
				// e1.invalidIdExceptionPopUp(frameWindow);
			} catch (RemoteException e) {
				JOptionPane.showMessageDialog(frameWindow,
						"RMI. Problema, contattare l'amministratore di rete.",
						"Errore", JOptionPane.WARNING_MESSAGE);
			}
		}

		/*
		 * d = new Dimension(dim.height/map.length, dim.height/map.length);
		 * 
		 * dLocalDin = d;
		 * 
		 * dLocalDin.setSize(dLocalDin.width*2.5, dLocalDin.height*2.5);
		 * 
		 * 
		 * mapButtons = new JButton[map.length][map.length];
		 * for(i=map.length-1;i>=0;i--){ for(j=0;j<map.length;j++){
		 * mapButtons[i][j] = new JButton();
		 * mapButtons[i][j].setPreferredSize(d); } }
		 * 
		 * mapPanel = new JPanel(new GridLayout(map.length, map.length));
		 * 
		 * /* inizializzo la mappa aggiungendo i bottoni al pannello adatto
		 * inizializzando il colore di background in base alla topologia
		 */
		for (i = 0; i < dimMyMap; i++) {
			for (j = 0; j < dimMyMap; j++) {
				mapButtons[i][j].setBackground(blindColor);
				mapPanel.add(mapButtons[i][j]);
			}
		}

		for (i = offset; i < dimMyMap - offset; i++) {
			for (j = offset; j < dimMyMap - offset; j++) {
				if (!map[i - offset][j - offset].isVisible()) {
					mapButtons[i][j].setBackground(blindColor);
				} else if (map[i - offset][j - offset].isWater()) {
					mapButtons[i][j].setBackground(waterColor);
				} else if (map[i - offset][j - offset].isEarth()) {
					mapButtons[i][j].setBackground(earthColor);
				} else if (map[i - offset][j - offset].isVegetation()) {
					mapButtons[i][j].setBackground(grassColor);
				}
			}
		}
		borderWaterPainter();
		setDinosaursOnMap(cc, token);

	}

	/**
	 * Metodo che posiziona i dinosauri sulla mappa generale dichiarando per
	 * quel bottone un action listener che andra' a chiamare la vista locale per
	 * il dinosauro corrispondendte al bottone cliccato.
	 * 
	 * @param cc
	 *            Comunicatore client per chiamare i metodi relativi alle
	 *            richieste al server. In questo caso per potere sistemare i
	 *            dinosauri sulla mappa generale dovro' chiedere al server la
	 *            lista e lo stato di tutti i dinosauri di un determianto
	 *            utente.
	 * @param token
	 *            Il token ricevuto al momento del login.
	 * 
	 * @see client.ClientSocketComunicator#askForList(String)
	 */
	void setDinosaursOnMap(ClientComunicator cc, String token) {

		for (int i = 0; i < dimMyMap; i++) {
			for (int j = 0; j < dimMyMap; j++) {
				ActionListener[] allActionListenerInAbutton = mapButtons[i][j]
						.getActionListeners();

				for (ActionListener a : allActionListenerInAbutton) {
					mapButtons[i][j].removeActionListener(a);
					mapButtons[i][j].setEnabled(false);
				}
			}
		}

		/*
		 * "posiziono" i miei dinosauri sulla mappa colorando il background ed
		 * attivo gli actionlistener per la vista locale solo su quei bottoni
		 */
		for (i = 0; (i < dinosaurList.length) && (i < maxNumDin)
				&& (dinosaurList[i] != null); i++) {
			// coordOfDinosaurs[i][0] contiene la riga
			// coordOfDinosaurs[i][1] contiene la colonna

			mapButtons[coordOfDinosaurs[i][0]][coordOfDinosaurs[i][1]]
					.setBackground(defaultDinosaurColor);
			mapButtons[coordOfDinosaurs[i][0]][coordOfDinosaurs[i][1]]
					.setEnabled(true);
			try {
				statusDinosaur = cc.askForStatus(token, dinosaurList[i]);
			} catch (UserNotPlayingException e1) {
				/*
				 * vorra' dire che la razza si e' estinta
				 */
				e1.userNotPlayingExceptionPopUp(frameWindow);
				frameWindow.dispose();
				menuWindow.setVisible(true);
			} catch (InvalidTokenException e1) {
				e1.invalidTokenExceptionPopUp(frameWindow);
			} catch (InvalidIdException e1) {
				// e1.invalidIdExceptionPopUp(frameWindow);
			} catch (RemoteException e) {
				JOptionPane.showMessageDialog(frameWindow,
						"RMI. Problema, contattare l'amministratore di rete.",
						"Errore", JOptionPane.WARNING_MESSAGE);
			}

			mapButtons[coordOfDinosaurs[i][0]][coordOfDinosaurs[i][1]]
					.addActionListener(new DinoActionListener(
							coordOfDinosaurs[i][0], coordOfDinosaurs[i][1],
							statusDinosaur[indexOfType], dinosaurList,
							coordOfDinosaurs, cc, token, this));

			mapButtons[coordOfDinosaurs[i][0]][coordOfDinosaurs[i][1]]
					.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							actionsPanel.setVisible(true);
						}
					});

		}
	}

	/**
	 * Metodo che si occupa di colorare il bordo della mappa generale con un
	 * colore dell'acqua un po' piu' scuro rispetto a quello che si incontra
	 * nell'ambiente di gioco.
	 */
	private void borderWaterPainter() {

		/*
		 * setto il colore dell'acqua di bordo piu' scuro e li disabilito
		 * perche' non potro' cliccarli durante la partita
		 */

		borderWater = offset;

		for (i = 0; i < borderWater; i++) {
			for (j = 0; j < dimMyMap; j++) {
				mapButtons[i][j].setBackground(borderWaterColor);
				mapButtons[i][j].setEnabled(false);
			}
		}

		for (i = dimMyMap - borderWater; i < dimMyMap; i++) {
			for (j = 0; j < dimMyMap; j++) {
				mapButtons[i][j].setBackground(borderWaterColor);
				mapButtons[i][j].setEnabled(false);
			}
		}

		for (i = 0; i < dimMyMap; i++) {
			for (j = 0; j < borderWater; j++) {
				mapButtons[i][j].setBackground(borderWaterColor);
				mapButtons[i][j].setEnabled(false);
			}
		}

		for (i = 0; i < dimMyMap; i++) {
			for (j = dimMyMap - borderWater; j < dimMyMap; j++) {
				mapButtons[i][j].setBackground(borderWaterColor);
				mapButtons[i][j].setEnabled(false);
			}
		}

	}

	/**
	 * Metodo che si occupa della parte di costruzione della vista locale.
	 * Gestisce la mappa locale, l'immagine del dinosauro e i campi informativi
	 * sui turni vissuti, l'energia residua ed il livello del dinosauro stesso.
	 * 
	 * @param IdDinosaur
	 *            Id identificativo del dinosauro.
	 * @param type
	 *            Tipo del dinosauro: erbivoro o carnivoro.
	 * @param cc
	 *            Comunicatore Client.
	 * @param token
	 *            Token ricevuto al momento del login.
	 * @param absoluteRow
	 *            Coordinata di riga rispetto alla mappa generale del dinosauro.
	 * @param absoluteCol
	 *            Coordinata di colonna rispetto alla mappa generale del
	 *            dinosauro.
	 * @throws InvalidIdException
	 *             Eccezione lanciata quando l'id del dinosauro passato come
	 *             parametro non e' valido.
	 */
	void giveMelocalView(final String IdDinosaur, final String type,
			final ClientComunicator cc, final String token, int absoluteRow,
			int absoluteCol) throws InvalidIdException {

		/*
		 * abilito la local view per permettere all'utente tramite questo
		 * booleano di potere modificare il colore di come vedo il suo dinosauro
		 * in qualsiasi momento
		 */
		localViewEnabled = true;

		/*
		 * faccio comparire il campo per potere scegliere il colore
		 */
		colorLabel.setVisible(true);
		colorsAvailable.setVisible(true);

		/*
		 * inizializzo gli action listener per i bottoni che posso utilizzare
		 * durante la vista locale cresci deposita uovo passa turno
		 */

		ActionListener[] allActionListenerInMakeEggButton = makeEggButton
				.getActionListeners();

		for (ActionListener a : allActionListenerInMakeEggButton) {
			makeEggButton.removeActionListener(a);
		}

		makeEggButton.addActionListener(new makeEggActionListener(IdDinosaur,
				type, absoluteRow, absoluteCol, cc, token, this));
		makeEggButton.setText("Deponi uovo (-1500)");
		/*
		 * ActionListener[] allActionListenerInPassButton =
		 * passButton.getActionListeners();
		 * 
		 * for(ActionListener a : allActionListenerInPassButton){
		 * passButton.removeActionListener(a); }
		 * 
		 * passButton.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { try { cc.pass(token); } catch
		 * (InvalidTokenException e1) {
		 * e1.invalidTokenExceptionPopUp(frameWindow); } catch
		 * (NotYourTurnException e1) {
		 * e1.notYourTurnExceptionPopUp(frameWindow); } catch
		 * (UserNotPlayingException e1) {
		 * e1.userNotPlayingExceptionPopUp(frameWindow); } } });
		 */
		Cell[][] localMap = null;
		try {
			localMap = cc.askForlocalView(token, IdDinosaur);
		} catch (InvalidTokenException e1) {
			e1.invalidTokenExceptionPopUp(frameWindow);
		} catch (UserNotPlayingException e1) {
			// e1.userNotPlayingExceptionPopUp(frameWindow);
		} catch (InvalidIdException e1) {
			// e1.invalidIdExceptionPopUp(frameWindow);
			throw e1;
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(frameWindow,
					"RMI. Problema, contattare l'amministratore di rete.",
					"Errore", JOptionPane.WARNING_MESSAGE);
		}

		/*
		 * in base al livello dell'energia nella vista locale mostro un colore
		 * piu' o meno vivace: piu' energia c'e' piu' luminoso sara' il colore
		 * del bottone
		 */
		grassLevel1Color = new Color(69, 139, 0);
		grassLevel2Color = new Color(102, 205, 0);
		grassLevel3Color = new Color(118, 238, 0);
		grassLevel4Color = new Color(127, 255, 0);

		carrionLevel1Color = new Color(139, 0, 0);
		carrionLevel2Color = new Color(205, 0, 0);
		carrionLevel3Color = new Color(238, 0, 0);
		carrionLevel4Color = new Color(255, 0, 0);

		// Font f = new Font("Dialog", Font.PLAIN, 11);

		/*
		 * contiene le dimensioni della vista locale del dinosauro
		 */
		if (localMap != null) {
			

			int dinoRow = 0;
			int dinoCol = 0;
			/*
			 * prima ancora di costruire la vista locale devo controllare dove
			 * e' il dinosauro nella vista locale e' nel caso ci siano piu'
			 * dinosauri della specie del richiedente devo controllare che chi
			 * ha richiesto la vista sia di chi memorizzo le coordinate
			 */
			for (int i = 0; i < localMap.length; i++) {
				for (int j = 0; j < localMap[0].length; j++) {
					if (localMap[i][j].getThereIsDinosaur()
							&& localMap[i][j].getIdDinosaur()
									.equals(IdDinosaur)) {
						dinoRow = i;
						dinoCol = j;
						relativeStartingRowDinosaur = dinoRow;
						relativeStartingColDinosaur = dinoCol;
					}
				}
			}

			/*
			 * calcolo le distanze della vista locale dalla mappa massima 9x9 in
			 * modo tale da centrare il dinosauro
			 */
			int distanceTop = (maxLocalView / 2) - dinoRow;
			int distanceLeft = (maxLocalView / 2) - dinoCol;
			int distanceBottom = maxLocalView - (localMap.length + distanceTop);
			int distanceRight = maxLocalView
					- (localMap[0].length + distanceLeft);

			/*
			System.out.println("Mappa: " + localMap[0].length + "x"
					+ localMap.length);
			System.out.println("Dinosauro in: " + dinoRow + " " + dinoCol);
			System.out.println("DT: " + distanceTop + " DB: " + distanceBottom
					+ " DL: " + distanceLeft + " DR: " + distanceRight);
			*/
			
			for (i = 0; i < maxLocalView; i++) {
				for (j = 0; j < maxLocalView; j++) {
					/*
					 * elimino tutti gli action listener precedenti all'interno
					 * di un determinato bottone
					 */
					ActionListener[] allActionListenerInAbutton = dinosaurMapButtons[i][j]
							.getActionListeners();

					for (ActionListener a : allActionListenerInAbutton) {
						dinosaurMapButtons[i][j].removeActionListener(a);
					}

					dinosaurMapButtons[i][j]
							.addActionListener(new MovementActionListener(
									IdDinosaur, type, map.length, maxLocalView,
									absoluteRow, absoluteCol, i, j, cc, token,
									this));

					dinosaurMapButtons[i][j].setBorder(BorderFactory
							.createRaisedBevelBorder());
					dinosaurMapButtons[i][j].setEnabled(true);
					// dinosaurMapButtons[i][j].setBorderPainted(false);

					if ((i < distanceTop)
							|| (i >= maxLocalView - distanceBottom)
							|| (j < distanceLeft)
							|| (j >= maxLocalView - distanceRight)) {

						/*
						 * PROVA myLocalButtons[i][j] = new
						 * MyLocalMapButton("b");
						 */

						dinosaurMapButtons[i][j].setBackground(blindColor);
					} else {
						if (localMap[i - distanceTop][j - distanceLeft]
								.getThereIsDinosaur()) {
							try {
								String[] statusDino = cc
										.askForStatus(token,
												localMap[i - distanceTop][j
														- distanceLeft]
														.getIdDinosaur());

								ActionListener[] allActionListenerIngrowButton = growButton
										.getActionListeners();

								for (ActionListener a : allActionListenerIngrowButton) {
									growButton.removeActionListener(a);
								}

								growButton
										.addActionListener(new GrowActionListener(
												IdDinosaur, type, absoluteRow,
												absoluteCol, cc, token, this));
								/*int energyToGrow = (Integer.parseInt(statusDino[indexOfLevel]) * 1000) / 2;*/
								
								
								//statusDino = cc.askForStatus(token, IdDinosaur);
								
								
								growButton.setText("Cresci "/* + "(-"+ energyToGrow + ")"*/);
								if (statusDino[indexOfUser].equals(Home
										.getUsername())) {

									/*
									 * PROVA myLocalButtons[i][j] = new
									 * MyLocalMapButton("d");
									 */

									localViewInfoLabel[i][j]
											.setText("Tuo dinosauro. Energia: "
													+ statusDino[indexOfEnergy]
													+ " Livello: "
													+ statusDino[indexOfLevel]
													+ " Eta': "
													+ statusDino[indexOfLivedRounds]);
									dinosaurMapButtons[i][j]
											.setBackground(defaultDinosaurColor);
								} else {
									localViewInfoLabel[i][j]
											.setText("Dinosauro di "
													+ statusDino[indexOfUser]
													+ ", razza "
													+ statusDino[indexOfRace]
													+ ", Livello: "
													+ statusDino[indexOfLevel]);
									dinosaurMapButtons[i][j]
											.setBackground(enemyDinosaurColor);
								}
							} catch (UserNotPlayingException e1) {
								JOptionPane.showMessageDialog(frameWindow,
										"GAME OVER! La tua razza si e' estinta.",
										"Razza estinta", JOptionPane.ERROR_MESSAGE);
							} catch (InvalidTokenException e1) {
							} catch (RemoteException e) {
								JOptionPane
										.showMessageDialog(
												frameWindow,
												"RMI. Problema, contattare l'amministratore di rete.",
												"Errore",
												JOptionPane.WARNING_MESSAGE);
							}
						} else {
							if (localMap[i - distanceTop][j - distanceLeft]
									.isVegetation()) {
								energy = localMap[i - distanceTop][j
										- distanceLeft].getCurrentEnergyVegetation();
								localViewInfoLabel[i][j]
										.setText("Vegetazione: " + energy
												+ " energia ricavabile");

								/*
								 * PROVA myLocalButtons[i][j] = new
								 * MyLocalMapButton("v1");
								 */

								if ((energy >= 0) && (energy <= 90)) {

									dinosaurMapButtons[i][j]
											.setBackground(grassLevel1Color);
								} else if ((energy > 90) && (energy <= 180)) {
									dinosaurMapButtons[i][j]
											.setBackground(grassLevel2Color);
								} else if ((energy > 180) && (energy <= 270)) {
									dinosaurMapButtons[i][j]
											.setBackground(grassLevel3Color);
								} else if ((energy > 270) && (energy <= 350)) {
									dinosaurMapButtons[i][j]
											.setBackground(grassLevel4Color);
								}
							} else {
								if (localMap[i - distanceTop][j - distanceLeft]
										.isCarrion()) {
									energy = localMap[i - distanceTop][j
											- distanceLeft].getCurrentEnergyCarrion();
									localViewInfoLabel[i][j]
											.setText("Carogna: " + energy
													+ " energia ricavabile");

									/*
									 * PROVA myLocalButtons[i][j] = new
									 * MyLocalMapButton("t");
									 */

									if ((energy >= 0) && (energy <= 250)) {
										dinosaurMapButtons[i][j]
												.setBackground(carrionLevel1Color);
									} else if ((energy > 250)
											&& (energy <= 500)) {
										dinosaurMapButtons[i][j]
												.setBackground(carrionLevel2Color);
									} else if ((energy > 500)
											&& (energy <= 750)) {
										dinosaurMapButtons[i][j]
												.setBackground(carrionLevel3Color);
									} else if ((energy > 750)
											&& (energy <= 1000)) {
										dinosaurMapButtons[i][j]
												.setBackground(carrionLevel4Color);
									}
								} else {
									if (localMap[i - distanceTop][j
											- distanceLeft].isWater()) {

										/*
										 * PROVA myLocalButtons[i][j] = new
										 * MyLocalMapButton("a");
										 */

										localViewInfoLabel[i][j]
												.setText("Acqua");
										dinosaurMapButtons[i][j]
												.setBackground(waterColor);
									} else {
										if (localMap[i - distanceTop][j
												- distanceLeft].isEarth()) {

											/*
											 * PROVA myLocalButtons[i][j] = new
											 * MyLocalMapButton("t");
											 */

											localViewInfoLabel[i][j]
													.setText("Terra");
											dinosaurMapButtons[i][j]
													.setBackground(earthColor);
										}
									}
								}
							}

						}
					}
					/*
					 * PROVA myLocalButtons[i][j].setBorderPainted(false);
					 * myLocalButtons[i][j].setVisible(true);
					 * myLocalButtons[i][j].setEnabled(true);
					 */
					localViewInfoLabel[i][j]
							.setBounds(
									10,
									(rightPanel.getHeight() - (dLocalDin.height * (maxLocalView + 2))),
									500, 30);
					rightPanel.add(localViewInfoLabel[i][j]);
					dinosaurMapButtons[i][j]
							.addMouseListener(new LocalMapMouseListener(
									localViewInfoLabel[i][j]));
					localViewInfoLabel[i][j].setVisible(false);
				}
			}
		}

		// dinosaurMapButtons[i][j].setEnabled(false);
		// dinosaurMapButtons[i][j].setBorder(BorderFactory.createLoweredBevelBorder());

		try {
			String[] satusIdDinosaur = cc.askForStatus(token, IdDinosaur);

			switch (Integer.parseInt(satusIdDinosaur[indexOfLevel])) {
			case 1: {
				if (satusIdDinosaur[indexOfType].equals("e")) {
					imageDin.setIcon(imageBackHerbivorousLevel1);
					} else {
					imageDin.setIcon(imageBackCarnivorousLevel1);
					}
				break;
			}
			case 2: {
				if (satusIdDinosaur[indexOfType].equals("e")) {
					imageDin.setIcon(imageBackHerbivorousLevel2);
					} else {
					imageDin.setIcon(imageBackCarnivorousLevel2);
					}
				break;
			}
			case 3: {
				if (satusIdDinosaur[indexOfType].equals("e")) {
					imageDin.setIcon(imageBackHerbivorousLevel3);
					} else {
					imageDin.setIcon(imageBackCarnivorousLevel3);
					}
				break;
			}
			case 4: {
				if (satusIdDinosaur[indexOfType].equals("e")) {
					imageDin.setIcon(imageBackHerbivorousLevel4);
					} else {
					imageDin.setIcon(imageBackCarnivorousLevel4);
					}
				break;
			}
			case 5: {
				if (satusIdDinosaur[indexOfType].equals("e")) {
					imageDin.setIcon(imageBackHerbivorousLevel5);
					} else {
					imageDin.setIcon(imageBackCarnivorousLevel5);
					}
				break;
			}
			default:
				break;
			}

			if (type.equals("c")) {
				localDinosaurLevelLabel
						.setText("Livello: "
								+ satusIdDinosaur[indexOfLevel]
								+ " (Forza:"
								+ 2
								* Integer
										.parseInt(satusIdDinosaur[indexOfEnergy])
								* Integer
										.parseInt(satusIdDinosaur[indexOfLevel])
								+ ")");
			} else {
				localDinosaurLevelLabel
						.setText("Livello: "+ satusIdDinosaur[indexOfLevel]+ " (Forza:"+ Integer.parseInt(satusIdDinosaur[indexOfEnergy])	* Integer.parseInt(satusIdDinosaur[indexOfLevel])+ ")");
			}
			localDinosaurEnergyLabel.setText("Energia: "
					+ satusIdDinosaur[indexOfEnergy]);
			localDinosaurLivedRoundsLabel.setText("Eta': "
					+ satusIdDinosaur[indexOfLivedRounds]);

			localDinosaurLevelLabel.setVisible(true);
			localDinosaurEnergyLabel.setVisible(true);
			localDinosaurLivedRoundsLabel.setVisible(true);

		} catch (UserNotPlayingException e) {
			JOptionPane.showMessageDialog(frameWindow,
				"GAME OVER! La tua razza si e' estinta.",
				"Razza estinta", JOptionPane.ERROR_MESSAGE);
		} catch (InvalidTokenException e) {
			e.invalidTokenExceptionPopUp(frameWindow);
		} catch (InvalidIdException e) {
			// e.invalidIdExceptionPopUp(frameWindow);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(frameWindow,
					"RMI. Problema, contattare l'amministratore di rete.",
					"Errore", JOptionPane.WARNING_MESSAGE);
		}

		// imageDin = new JLabel(imageBack);
		imageDin.setVisible(true);
		TitledBorder title = BorderFactory
				.createTitledBorder("Dino selezionato");
		title.setTitleJustification(TitledBorder.CENTER);
		imageDin.setBorder(title);
		// imageDin.setBorder(BorderFactory.createLineBorder(Color.gray));
		imageDin.setBounds((maxLocalView * dLocalDin.height) + 10, dim.height
				- ((maxLocalView + 1) * dLocalDin.height),
				imageBackCarnivorousLevel1.getIconWidth(),
				imageBackCarnivorousLevel1.getIconHeight());
		rightPanel.add(imageDin);

		dinosaurMapPanel
				.setBounds(5, dim.height - 10 * dLocalDin.height, maxLocalView
						* dLocalDin.width, maxLocalView * dLocalDin.height);
		title = BorderFactory
				.createTitledBorder("Vista per il dinosauro selezionato");
		title.setTitleJustification(TitledBorder.CENTER);
		dinosaurMapPanel.setBorder(title);

		localDinosaurLevelLabel.setBounds(dinosaurMapPanel.getWidth() + 10,
				rightPanel.getHeight() - dinosaurMapPanel.getHeight() + 5
						* dLocalDin.height, 150, 30);
		localDinosaurEnergyLabel.setBounds(dinosaurMapPanel.getWidth() + 10,
				rightPanel.getHeight() - dinosaurMapPanel.getHeight() + 6
						* dLocalDin.height, 100, 30);
		localDinosaurLivedRoundsLabel.setBounds(
				dinosaurMapPanel.getWidth() + 10, rightPanel.getHeight()
						- dinosaurMapPanel.getHeight() + 7 * dLocalDin.height,
				100, 30);

		rightPanel.add(dinosaurMapPanel);
		rightPanel.add(localDinosaurLevelLabel);
		rightPanel.add(localDinosaurEnergyLabel);
		rightPanel.add(localDinosaurLivedRoundsLabel);
		// refresh del pannello per fare comparire i campi aggiunti
		rightPanel.revalidate();
		rightPanel.repaint();
		// System.out.println("spazio per immagine: "+(rightPanel.getWidth()-dinosaurMapPanel.getWidth())+"x"+(dinosaurMapPanel.getHeight()-3*dLocalDin.height));
	}

	public int getRelativeStartingRowDinosaur() {
		return relativeStartingRowDinosaur;
	}

	public int getRelativeStartingColDinosaur() {
		return relativeStartingColDinosaur;
	}

	public int getOffset() {
		return offset;
	}

	public void paint() {

	}

	public JFrame getFrameWindow() {
		return frameWindow;
	}

	public JFrame getMenuWindow() {
		return menuWindow;
	}


}