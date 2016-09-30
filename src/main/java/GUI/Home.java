package GUI;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.io.*;
import java.rmi.RemoteException;

import javax.sound.sampled.*;

import client.*;
import exception.AutenticationFailedException;
import exception.UserAlreadyLoggedException;

/**
 * Classe che identifica la schermata iniziale. In questa schermata e' possibile
 * instaurare la connessione tramite socket o RMI, creare un utente e/o
 * effettuare il login.
 */
public class Home {
	private ClientComunicator cc;
	private JLabel infoConnectionLabel;

	private AudioFormat audioFormat;
	private AudioInputStream audioInputStream;
	private SourceDataLine sourceDataLine;
	private boolean stopPlayback = false;

	private JFrame frameWindow;

	private ImageIcon imageBack;

	/*
	 * creo un booleano che mi dice se il radio button e' selezionato o meno.
	 * Servira' al metodo login per fare partire la connessione apposita
	 */
	private boolean flagSocket;
	private boolean flagRMI;

	private JRadioButton socketButton;
	private JRadioButton RMIButton;

	private JTextField usernameTextField;
	private static String username;

	private JTextField passwordField;
	private String password;

	private JButton loginButton;

	private JButton registerButton;

	private JLabel ipLabel;
	private JTextField ipTextField;

	private JLabel portLabel;
	private JTextField portTextField;

	private JButton connectionButton;

	private JButton audioPlayingButton;
	private JButton audioMuteButton;

	private JFrame frame;

	private String result;

	private JLabel phraseLabel;

	/**
	 * Costruttore che si occupa di far capire la schermata di login.
	 */
	public Home() {
		JLabel background;
		JLabel resolutionLabel;
		ButtonGroup SocketRMIButtonGroup;
		JLabel usernameLabel;
		JLabel passwordLabel;
		JButton infoButton;

		final int res1280 = 1280;
		final int res720 = 720;
		final int res1024 = 1024;
		final int res768 = 768;
		final int res600 = 600;
		final int res1440 = 1440;
		final int res900 = 900;
		phraseLabel = new JLabel("''Make simple things easy. Difficult things possible.''");
		phraseLabel.setVisible(false);

		/*
		 * prendo info sulla risoluzione dello schermo
		 */
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dim = toolkit.getScreenSize();

		/*
		 * inizializzo le string username e password che serviranno al campo di
		 * login che le passera al metodo opportuno per effettuare il login
		 */

		audioPlayingButton = new JButton(new ImageIcon("src/main/resources/audioButtonPlaying.jpg"));
		audioMuteButton = new JButton(new ImageIcon("src/main/resources/audioButtonMute.jpg"));

		audioMuteButton.setVisible(true);
		audioMuteButton.setEnabled(true);
		audioPlayingButton.setVisible(false);
		audioPlayingButton.setEnabled(false);

		/*
		 * all'avvio del programma faccio partire la musica in sottofondo
		 */
		playAudio();

		audioMuteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Terminate playback before EOF
				stopPlayback = true;
			}
		});

		// Instantiate and register action listeners
		// on the Play and Stop buttons.
		audioPlayingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				audioMuteButton.setVisible(true);
				audioMuteButton.setEnabled(true);
				audioPlayingButton.setVisible(false);
				audioPlayingButton.setEnabled(false);
				playAudio();
			}
		});

		/*
		 * getContentPane().add(audioPlayingButton, "West");
		 * getContentPane().add(audioMuteButton, "East");
		 * 
		 * setTitle("Prova audio dino");
		 * setDefaultCloseOperation(EXIT_ON_CLOSE); setSize(250, 70);
		 * setVisible(true);
		 */

		/*
		 * creo la finestra principale che conterra' il tutto
		 */
		frameWindow = new JFrame(
				"L'isola dei dinosauri - Created by Gioele Salvatore Antoci & Sergio Andaloro (Politecnico di Milano 2010/2011)");

		/*
		 * setto lo sfondo con un immagine personalizzata se il pc che lancia il
		 * gioco non ha settato una delle risoluzioni supportate il gioco non
		 * parte lanciando una finestra d'errore
		 */
		if ((dim.width == res1280) && (dim.height == res720)) {
			imageBack = new ImageIcon("src/main/resources/startDino1280x720.jpg");
		} else if ((dim.width == res1024) && (dim.height == res768)) {
			imageBack = new ImageIcon("src/main/resources/startDino1024x768.jpg");
		} else if ((dim.width == res1024) && (dim.height == res600)) {
			imageBack = new ImageIcon("src/main/resources/startDino1024x600.jpg");
		} else if ((dim.width == res1440) && (dim.height == res900)) {
			imageBack = new ImageIcon("src/main/resources/startDino1440x900.jpg");
		} else {
			imageBack = new ImageIcon("src/main/resources/startDino1024x768.jpg");
			JOptionPane.showMessageDialog(frame,
					"La risoluzione non e' tra quelle supportate (1024x600 - 1024x768 - 1280x720 - 1440x900)\n"
							+ "Per una migliore esperienza di gioco passa ad una delle risoluzioni supportate.",
					"Problema risoluzione schermo", JOptionPane.INFORMATION_MESSAGE);
		}

		background = new JLabel(imageBack);
		background.setBounds(0, 0, imageBack.getIconWidth(), imageBack.getIconHeight());
		background.setSize(dim.width, dim.height);

		/*
		 * instanzio dei bottoni in modo tale da permettere all'utente di
		 * selezionare che tipo di "modalita'" utilizzare
		 */

		/*
		 * aggiungo una riga di testo che consiglia all'utente che risoluzione
		 * impostare per giocare al meglio
		 */
		Font f = new Font("Dialog", Font.PLAIN, 11);
		resolutionLabel = new JLabel("Risoluzioni supportate: 1024x600 - 1024x768 - 1280x720 - 1440x900");
		resolutionLabel.setFont(f);

		/*
		 * alla selezione del radio button local i campi relativi a ip del
		 * server e porta scompaiono nel caso in cui gia' in precedenza si fosse
		 * selezionato il radio button socket
		 */

		Font f2 = new Font("Dialog", Font.PLAIN, 12);
		infoConnectionLabel = new JLabel("Scegli il tipo di connessione prima di iniziare");
		infoConnectionLabel.setFont(f2);

		socketButton = new JRadioButton("socket");

		/*
		 * alla selezione del radio button socket compaiono i campi relativi ad
		 * ip server e porta
		 */
		socketButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				phraseLabel.setVisible(true);
				phraseLabel.setText("''Make simple things easy. Difficult things possible.''");
				ipLabel.setVisible(true);
				ipTextField.setVisible(true);
				ipTextField.setEnabled(true);
				portLabel.setVisible(true);
				portTextField.setVisible(true);
				portTextField.setEnabled(true);
				portTextField.setText("1288");
				connectionButton.setVisible(true);
				infoConnectionLabel.setVisible(false);
				flagSocket = true;
				flagRMI = false;
			}
		});

		RMIButton = new JRadioButton("RMI");

		/*
		 * alla selezione del radio button RMI i campi relativi a ip del server
		 * e porta scompaiono nel caso in cui gia' in precedenza si fosse
		 * selezionato il radio button socket
		 */
		RMIButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				phraseLabel.setVisible(true);
				phraseLabel.setText("''Questo RMI e' socket-oriented'' [cit.]");
				ipLabel.setVisible(true);
				ipTextField.setVisible(true);
				ipTextField.setEnabled(false);
				portLabel.setVisible(true);
				portTextField.setVisible(true);
				portTextField.setEnabled(false);
				portTextField.setText("8812");
				connectionButton.setVisible(true);
				infoConnectionLabel.setVisible(false);
				flagSocket = false;
				flagRMI = true;
			}
		});

		/*
		 * aggiungo i radio button ad un ButtonGroup in modo da renderne la
		 * selezione esclusiva
		 */
		SocketRMIButtonGroup = new ButtonGroup();
		SocketRMIButtonGroup.add(socketButton);
		SocketRMIButtonGroup.add(RMIButton);

		usernameLabel = new JLabel("Nome utente");
		usernameTextField = new JTextField();
		usernameTextField.setEnabled(false);

		passwordLabel = new JLabel("Password");
		passwordField = new JPasswordField();
		passwordField.setEnabled(false);

		ipLabel = new JLabel("Indirizzo IP");
		ipTextField = new JTextField("127.0.0.1");

		portLabel = new JLabel("Porta");
		portTextField = new JTextField("1288");

		connectionButton = new JButton("Connettiti");

		connectionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					if (flagSocket) {
						cc = new ClientSocketComunicator(ipTextField.getText(), portTextField.getText());
					} else {
						cc = new ClientRmiComunicator(ipTextField.getText(), portTextField.getText());
					}

					/*
					 * una volta effettuata correttamente la connessione abilito
					 * i tasti per fare login o registrarmi e faccio scomparire
					 * i campi ip e port il tasto di connessione e i bottoni che
					 * selezionano socket e rmi
					 */
					usernameTextField.setEnabled(true);
					passwordField.setEnabled(true);
					loginButton.setEnabled(true);
					registerButton.setEnabled(true);
					ipLabel.setVisible(false);
					ipTextField.setVisible(false);
					portLabel.setVisible(false);
					portTextField.setVisible(false);
					connectionButton.setVisible(false);
					socketButton.setVisible(false);
					RMIButton.setVisible(false);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(frame, "La connessione e' fallita, controllare i campi Ip e porta",
							"Connessione fallita", JOptionPane.ERROR_MESSAGE);
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(frame, "La connessione e' fallita, controllare i campi Ip e porta",
							"Connessione fallita", JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		/*
		 * questi sono i campi che permettono di inserire l'ip del server e la
		 * porta di ascolto vengono settati inizialmente a false perche'
		 * compariranno solo quando il radio button "socket" verra' selezionato
		 */
		ipLabel.setVisible(false);
		ipTextField.setVisible(false);
		portLabel.setVisible(false);
		portTextField.setVisible(false);
		connectionButton.setVisible(false);

		loginButton = new JButton("Login");
		loginButton.setEnabled(false);
		/*
		 * comportamento in caso di pressione del tasto "Login"
		 */
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame = new JFrame();

				if ((usernameTextField.getText().length() == 0) && (passwordField.getText().length() != 0)) {
					JOptionPane.showMessageDialog(frame,
							"Il campo nome utente e' vuoto, si prega di reinseririlo correttamente",
							"Campo nome utente mancante", JOptionPane.WARNING_MESSAGE);
				} else if ((usernameTextField.getText().length() != 0) && (passwordField.getText().length() == 0)) {
					JOptionPane.showMessageDialog(frame,
							"Il campo password e' vuoto, si prega di reinseririlo correttamente",
							"Campo password mancante", JOptionPane.WARNING_MESSAGE);
				} else if ((usernameTextField.getText().length() == 0) && (passwordField.getText().length() == 0)) {
					JOptionPane.showMessageDialog(frame,
							"I campi nome utente e password sono vuoti, si prega di reinserirli correttamente",
							"Campi nome utente e password mancanti", JOptionPane.WARNING_MESSAGE);
				} else if (!usernameTextField.getText().matches("\\p{Alnum}+")) {
					JOptionPane.showMessageDialog(frame,
							"Hai inserito un carattere non valido nel campo nome utente\n"
									+ "Sono consentiti solo caratteri alfanumerici",
							"Carattere non valido", JOptionPane.ERROR_MESSAGE);
				} else if (!passwordField.getText().matches("\\p{Alnum}+")) {
					JOptionPane.showMessageDialog(frame,
							"Hai inserito un carattere non valido nel campo password\n"
									+ "Sono consentiti solo caratteri alfanumerici",
							"Carattere non valido", JOptionPane.ERROR_MESSAGE);
				} else {
					/*
					 * qui effettuo il login sia se sono socket che RMI
					 */
					username = usernameTextField.getText();
					password = passwordField.getText();

					try {
						result = cc.logYourUser(username, password);
						/*
						 * faccio scomparire la finestra principale di login per
						 * fare comparire la finestra di gioco. Non la chiudo
						 * perche' voglio che la musica continui ad andare ed
						 * essendo su un thread legato alla finestra principale
						 * se chiudo quella chiudo tutto
						 */
						frameWindow.setVisible(false);
						/*
						 * passo anche frameWindow poiche' in caso di logout
						 * devo farla ridiventare visibile
						 */
						usernameTextField.setText("");
						passwordField.setText("");
						new MenuGame(cc, result, frameWindow);

					} catch (AutenticationFailedException e1) {
						e1.autenticationFailedExceptionPopUp(frame);
					} catch (UserAlreadyLoggedException e1) {
						e1.userAlreadyLoggedExceptionPopUp(frame, username);
					} catch (RemoteException e1) {
						JOptionPane.showMessageDialog(frameWindow,
								"RMI. Problema, contattare l'amministratore di rete.", "Errore",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});

		registerButton = new JButton("Registrati");
		registerButton.setEnabled(false);
		/*
		 * comportamento in caso di pressione del tasto "Registrati" nella home
		 * apro una nuova finestra che permette di registrarsi inserendo
		 * username e password
		 */

		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Registration(cc);
			}
		});

		infoButton = new JButton("Info");

		infoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame,
						"L'isola dei dinosauri -  Politenico di Milano\n"
								+ "Realizzato da Gioele Salvatore Antoci e Sergio Andaloro\n"
								+ "per il Progetto finale di ingegneria del software 2010/2011",
						"Informazioni", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		/*
		 * setto il layout a null in modo tale da impostare i punti di anchor a
		 * mano per dire a tutti i campi dove andare a spostarsi
		 */
		frameWindow.setLayout(null);

		/*
		 * setto la posizione di tutti i campi istanziati
		 */
		int heightProportion = (dim.height - res600) / 2;
		int widthProportion = (dim.width - res1024) / 2;
		int proportion = dim.width / dim.height;

		int heightButtonAndLabel = 25;
		int labelAndTextFieldLenght = 300;
		int borderDistanceMainField = 80;

		resolutionLabel.setBounds(5, 5, 500 * proportion, 20 * proportion);
		infoConnectionLabel.setBounds(115 + widthProportion, 145 + heightProportion, 250 * proportion,
				heightButtonAndLabel * proportion);
		phraseLabel.setBounds(85 + widthProportion, 145 + heightProportion, labelAndTextFieldLenght * proportion,
				heightButtonAndLabel * proportion);
		socketButton.setBounds(175 + widthProportion, 170 + heightProportion, 75 * proportion,
				heightButtonAndLabel * proportion);
		RMIButton.setBounds(250 + widthProportion, 170 + heightProportion, 50 * proportion,
				heightButtonAndLabel * proportion);
		usernameLabel.setBounds(borderDistanceMainField + widthProportion, 200 + heightProportion,
				labelAndTextFieldLenght * proportion, heightButtonAndLabel * proportion);
		usernameTextField.setBounds(borderDistanceMainField + widthProportion, 225 + heightProportion,
				labelAndTextFieldLenght * proportion, heightButtonAndLabel * proportion);
		passwordLabel.setBounds(borderDistanceMainField + widthProportion, 270 + heightProportion,
				labelAndTextFieldLenght * proportion, heightButtonAndLabel * proportion);
		passwordField.setBounds(borderDistanceMainField + widthProportion, 295 + heightProportion,
				labelAndTextFieldLenght * proportion, heightButtonAndLabel * proportion);
		loginButton.setBounds(borderDistanceMainField + widthProportion, 340 + heightProportion, 100 * proportion,
				30 * proportion);
		registerButton.setBounds(280 + widthProportion, 340 + heightProportion, 100 * proportion, 30 * proportion);
		ipLabel.setBounds(borderDistanceMainField + widthProportion, 400 + heightProportion, 100 * proportion,
				heightButtonAndLabel * proportion);
		ipTextField.setBounds(borderDistanceMainField + widthProportion, 425 + heightProportion, 120 * proportion,
				heightButtonAndLabel * proportion);
		portLabel.setBounds(215 + widthProportion, 400 + heightProportion, 100 * proportion,
				heightButtonAndLabel * proportion);
		portTextField.setBounds(215 + widthProportion, 425 + heightProportion, 50 * proportion,
				heightButtonAndLabel * proportion);
		connectionButton.setBounds(borderDistanceMainField + widthProportion, 465 + heightProportion, 90 * proportion,
				heightButtonAndLabel * proportion);
		if ((dim.width == res1280) && (dim.height == res720)) {
			audioPlayingButton.setBounds(1236, 15, 30, 30);
			audioMuteButton.setBounds(1236, 15, 30, 30);
			infoButton.setBounds(1190, 685, 60, 15);
		} else if ((dim.width == res1024) && (dim.height == res768)) {
			audioPlayingButton.setBounds(980, 15, 30, 30);
			audioMuteButton.setBounds(980, 15, 30, 30);
			infoButton.setBounds(945, 715, 60, 15);
		} else if ((dim.width == res1440) && (dim.height == res900)) {
			audioPlayingButton.setBounds(1396, 15, 30, 30);
			audioMuteButton.setBounds(1396, 15, 30, 30);
			infoButton.setBounds(1350, 865, 60, 15);
		} else {
			audioPlayingButton.setBounds(965, 15, 30, 30);
			audioMuteButton.setBounds(965, 15, 30, 30);
			infoButton.setBounds(935, 550, 60, 15);
		}

		/*
		 * aggiungo i campi al frame in modo tale da poter visualizzare tutti i
		 * campi inizializzati
		 */
		background.add(resolutionLabel);
		background.add(infoConnectionLabel);
		background.add(phraseLabel);
		background.add(socketButton);
		background.add(RMIButton);
		background.add(usernameLabel);
		background.add(usernameTextField);
		background.add(passwordLabel);
		background.add(passwordField);
		background.add(loginButton);
		background.add(registerButton);
		background.add(ipLabel);
		background.add(ipTextField);
		background.add(portLabel);
		background.add(portTextField);
		background.add(connectionButton);
		background.add(audioPlayingButton);
		background.add(audioMuteButton);
		background.add(infoButton);

		/*
		 * aggiungo il background personalizzato alla finestre principale
		 */
		frameWindow.add(background);

		/*
		 * setto la dimensione predefinita della finestra
		 */
		frameWindow.setPreferredSize(new Dimension(dim.width, dim.width));

		frameWindow.pack();

		frameWindow.setVisible(true);

		frameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frameWindow.setResizable(false);

	}

	/**
	 * Questo metodo metodo fa partire la musica a partire da un file audio.
	 */
	private void playAudio() {

		try {
			File soundFile = new File("src/main/resources/backgroundSound.au");
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
			audioFormat = audioInputStream.getFormat();
			// System.out.println(audioFormat);

			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);

			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);

			/*
			 * Crea un thread che lancia la canzone. La canzone suonera' per
			 * tutta la sua durata o stoppera' se verra' pressato il bottone per
			 * stoppare. A causa dei buffer dei dati ci sara' un ritardo tra il
			 * click del bottone per stoppare e l'attuale terminazione del suono
			 * della canzone.
			 */
			new PlayThread().start();
		} catch (Exception e) {
			System.exit(0);
		}
	}

	/**
	 * Inner class per far partire i dati dal file audio.
	 */
	class PlayThread extends Thread {
		private byte tempBuffer[] = new byte[10000];

		@Override
		public void run() {
			try {
				sourceDataLine.open(audioFormat);
				sourceDataLine.start();

				int cnt;
				// Keep looping until the input read method
				// returns -1 for empty stream or the
				// user clicks the Stop button causing
				// stopPlayback to switch from false to
				// true.
				while (((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) && !stopPlayback) {
					if (cnt > 0) {
						// Write data to the internal buffer of
						// the data line where it will be
						// delivered to the speaker.
						sourceDataLine.write(tempBuffer, 0, cnt);
					}
				}
				// Block and wait for internal buffer of the
				// data line to empty.
				sourceDataLine.drain();
				sourceDataLine.close();
				// sourceDataLine.stop();

				// Prepare to playback another file
				audioMuteButton.setEnabled(false);
				audioMuteButton.setVisible(false);
				audioPlayingButton.setEnabled(true);
				audioPlayingButton.setVisible(true);
				stopPlayback = false;
			} catch (Exception e) {
				System.exit(0);
			}
		}
	}

	/**
	 * Metodo per avere accesso allo username dell'utente. Servira' alle
	 * finestre successive per potere visualizzare il nome dell'utente
	 * all'interno della finestra stessa.
	 */
	public static String getUsername() {
		return username;
	}

	/**
	 * Main per fare partire l'interfaccia grafica.
	 */
	public static void main(String[] args) {
		new Home();
	}

}