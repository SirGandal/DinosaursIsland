package GUI;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;

import javax.swing.*;

import client.*;
import exception.InvalidTokenException;
import exception.RaceAlreadyExistsException;
import exception.RaceNameTakenException;

/**
 * Questa classe si occupa di permettere ad un utente di creare una nuova razza scegliendo tra carnivora ed erbivora affibiandone un nome.
 */
public class CreateRace {

	private JFrame frameWindow;

	private JLabel nameLabel;
	private JTextField nameTextField;
	private static String nameRace;

	private ButtonGroup herbCarnButtonGroup;
	private JRadioButton herbivorousButton;
	private JRadioButton carnivorousButton;

	private boolean flagHerb;
	private boolean flagCarn;

	private JPanel herbCanJPanel;

	private JButton createRaceButton;

	/**
	 * Getter per prendere il nome della razza che l'utente ha creato.
	 * @return  Nome della razza creata.
	 * @uml.property  name="nameRace"
	 */
	public static String getNameRace() {
		return nameRace;
	}

	/**
	 * Costruttore che si occupa di creare la finestra con la quale interagira'
	 * l'utente.
	 * 
	 * @param cc
	 *            Per invocare il metodo relativo alla creazione della razza ho
	 *            necessita' di utilizzare il comunicatore lato client.
	 * @param token
	 *            Il token che ho ricevuto al momento del login.
	 * 
	 * @see server.ServerSocketComunicator#createNewRace(String, String, String)
	 */
	public CreateRace(final ClientComunicator cc, final String token) {

		frameWindow = new JFrame("Creazione nuova razza");

		nameLabel = new JLabel("Inserisci il nome della razza");
		nameTextField = new JTextField();

		herbivorousButton = new JRadioButton("Erbivoro");

		herbivorousButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				flagCarn = false;
				flagHerb = true;
			}
		});

		carnivorousButton = new JRadioButton("Carnivoro");

		carnivorousButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				flagCarn = true;
				flagHerb = false;
			}
		});

		herbCarnButtonGroup = new ButtonGroup();
		herbCarnButtonGroup.add(herbivorousButton);
		herbCarnButtonGroup.add(carnivorousButton);

		herbCanJPanel = new JPanel();
		herbCanJPanel.add(herbivorousButton);
		herbCanJPanel.add(carnivorousButton);

		createRaceButton = new JButton("Crea");

		createRaceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nameRace = nameTextField.getText();

				if (!flagCarn && !flagHerb) {
					JOptionPane
							.showMessageDialog(
									frameWindow,
									"Non hai selezionato la tipologia di razza da creare",
									"Seleziona tipo",
									JOptionPane.WARNING_MESSAGE);
				} else if (nameRace == null) {
					JOptionPane
							.showMessageDialog(
									frameWindow,
									"Non e' stato inserito alcun nome per la razza da creare",
									"Inserire nome razza",
									JOptionPane.WARNING_MESSAGE);
				} else {
					String type;
					if (flagHerb) {
						type = "e";
					} else {
						type = "c";
					}

					if (nameTextField.getText().matches("\\p{Alnum}+")) {

						try {
							cc.createNewRace(token, nameRace, type);
							frameWindow.dispose();
						} catch (RaceNameTakenException e1) {
							e1.raceNameTakenExceptionPopUp(frameWindow);
						} catch (InvalidTokenException e1) {
							e1.invalidTokenExceptionPopUp(frameWindow);
						} catch (RaceAlreadyExistsException e1) {
							e1.raceAlreadyExistsExceptionPopUp(frameWindow);
						} catch (RemoteException e1) {
							JOptionPane
									.showMessageDialog(
											frameWindow,
											"RMI. Problema, contattare l'amministratore di rete.",
											"Errore",
											JOptionPane.WARNING_MESSAGE);
						}
					} else {
						JOptionPane
								.showMessageDialog(
										frameWindow,
										"Il campo non deve essere vuoto e sono consentiti solo caratteri alfanumerici.",
										"Campo nome non valido.",
										JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		frameWindow.setLayout(new GridLayout(4, 1));

		frameWindow.add(nameLabel);
		frameWindow.add(nameTextField);
		frameWindow.add(herbCanJPanel);
		frameWindow.add(createRaceButton);

		frameWindow.pack();

		frameWindow.setVisible(true);

		frameWindow.setLocationRelativeTo(null);

		frameWindow.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		frameWindow.setResizable(false);
	}
}
