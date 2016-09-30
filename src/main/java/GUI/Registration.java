package GUI;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;

import javax.swing.*;

import client.*;
import exception.BusyUsernameException;

/**
 * Classe che si occupa di generare l'interfaccia grafica per permettere
 * all'utentte di creare un nuovo utente e quindi di registrarsi.
 */
public class Registration {

	private JFrame frameWindow;

	private JLabel usernameLabel;
	private JTextField usernameTextField;

	private JLabel passwordLabel1;
	private JTextField passwordField1;

	private JLabel passwordLabel2;
	private JTextField passwordField2;

	private JButton registerButton;

	private JFrame frame = new JFrame();
	private String username;
	private String password1;
	private String password2;

	/**
	 * Costruttore che costruisce l'interfaccia ed al momento opportuno invoca
	 * un metodo del comunicatore client che inviera' al server la richiesta di
	 * creare un nuovo utente.
	 * 
	 * @param cc
	 *            Comunicatore client.
	 * 
	 * @see client.ClientSocketComunicator#createNewUser(String, String)
	 * @see server.ServerSocketComunicator#createNewUser(String, String)
	 */
	public Registration(final ClientComunicator cc) {
		frameWindow = new JFrame("L'isola dei dinosauri - Registrazione");

		usernameLabel = new JLabel("Nome utente");
		usernameTextField = new JTextField();

		passwordLabel1 = new JLabel("Password");
		passwordField1 = new JPasswordField();

		passwordLabel2 = new JLabel("Conferma password");
		passwordField2 = new JPasswordField();

		registerButton = new JButton("Registrati");

		frameWindow.setLayout(null);

		usernameLabel.setBounds(25, 0, 100, 25);
		usernameTextField.setBounds(25, 25, 300, 25);
		passwordLabel1.setBounds(25, 65, 300, 25);
		passwordField1.setBounds(25, 90, 300, 25);
		passwordLabel2.setBounds(25, 130, 300, 25);
		passwordField2.setBounds(25, 155, 300, 25);
		registerButton.setBounds(130, 195, 100, 25);

		frameWindow.setPreferredSize(new Dimension(365, 280));

		/*
		 * TODO adattare i riferimenti in pixel alla risoluzione dello schermo
		 */

		frameWindow.add(usernameLabel);
		frameWindow.add(usernameTextField);
		frameWindow.add(passwordLabel1);
		frameWindow.add(passwordField1);
		frameWindow.add(passwordLabel2);
		frameWindow.add(passwordField2);
		frameWindow.add(registerButton);

		frameWindow.pack();

		frameWindow.setVisible(true);

		frameWindow.setLocationRelativeTo(null);

		frameWindow.setResizable(false);

		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				username = usernameTextField.getText();
				password1 = passwordField1.getText();
				password2 = passwordField2.getText();

				if (!username.matches("\\p{Alnum}+")) {
					JOptionPane
							.showMessageDialog(
									frame,
									"Hai inserito un carattere non valido nel campo nome utente\n"
											+ "Sono consentiti solo caratteri alfanumerici",
									"Carattere non valido",
									JOptionPane.ERROR_MESSAGE);
				} else if (!password1.matches("\\p{Alnum}+")
						&& !password2.matches("\\p{Alnum}+")) {
					JOptionPane
							.showMessageDialog(
									frame,
									"Hai inserito un carattere non valido nel campo password\n"
											+ "Sono consentiti solo caratteri alfanumerici",
									"Carattere non valido",
									JOptionPane.ERROR_MESSAGE);
				} else if (password1.equals(password2)) {

					try {
						cc.createNewUser(username, password1);
						frameWindow.dispose();
					} catch (BusyUsernameException e1) {
						e1.busyUsernameExceptionPopUp(frame);
					} catch (RemoteException e1) {
						JOptionPane
								.showMessageDialog(
										frameWindow,
										"RMI. Problema, contattare l'amministratore di rete.",
										"Errore", JOptionPane.WARNING_MESSAGE);
					}
				} else {
					JOptionPane
							.showMessageDialog(
									frame,
									"Le due password inserite non coincidono si prega di reinserirle correttamente",
									"Password non coincidenti",
									JOptionPane.ERROR_MESSAGE);
				}
			}
		});

	}

}
