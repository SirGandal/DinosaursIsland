package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.Timer;

/**
 * La classe crea un timer e visualizza una barra grafica che ne mostra l'andamento.
 * @author  Simone Parisi e Nicola Smacchia
 * @version  1.0
 */
public class MyTimer {
	private static final int PASSO_MILLISECONDI = 1000;
	private Timer timer;
	private JProgressBar waiter;
	private int secondi;
	private JFrame frame;
	private int progress = 0;

	/**
	 * Costruttore della classe.
	 * 
	 * @param frame
	 *            il {@code JFrame} a cui il timer e' associato
	 * @param secondi
	 *            la durata del timer
	 */
	public MyTimer(JFrame frame, int secondi) {
		this.frame = frame;
		this.secondi = secondi;
		waiter = new JProgressBar(0, secondi);
		timer = null;
	}

	/**
	 * @return
	 * @uml.property  name="waiter"
	 */
	public final JProgressBar getWaiter() {
		return waiter;
	}

	/**
	 * @param waiter
	 * @uml.property  name="waiter"
	 */
	public final void setWaiter(JProgressBar waiter) {
		this.waiter = waiter;
	}

	/**
	 * @return
	 * @uml.property  name="frame"
	 */
	public final JFrame getFrame() {
		return frame;
	}

	/**
	 * @param frame
	 * @uml.property  name="frame"
	 */
	public final void setFrame(JFrame frame) {
		this.frame = frame;
	}

	/**
	 * Avvia il timer, definendo l'azione da eseguire ad ogni passo.
	 */
	public final void startTimer() {
		if (timer == null) {
			timer = new Timer(PASSO_MILLISECONDI, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					progress++;
					waiter.setValue(progress);

					if (progress >= secondi) {
						progress = 0;
						stopTimer();
					}
				}
			});
		}
		if (timer.isRunning()) {
			timer.stop();
		}
		timer.start();
	}

	/**
	 * Ferma il timer, resetta la barra e chiude il frame associato al timer.
	 */
	public final void stopTimer() {
		if (timer != null) {
			timer.stop();
		}
		waiter.setValue(0);
		progress = 0;
		/*
		 * if(frame != null) { frame.dispose(); }
		 */
	}

}
