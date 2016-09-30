package server;

import java.util.*;

/**
 * Classe che si occupa di gestire i turni.
 */
public class RoundManager extends Observable {

	/**
	 * @uml.property  name="gameCurrent"
	 * @uml.associationEnd  
	 */
	private Game gameCurrent;
	private boolean roundConfirmed;
	private Timer t30;
	private Timer t2;

	/**
	 * Serve per capire se l'utente di turno ha confermato effettivamente il turno.
	 * @return  True se ha accettato, false contrariamente.
	 * @uml.property  name="roundConfirmed"
	 */
	public boolean isRoundConfirmed() {
		return roundConfirmed;
	}

	/**
	 * Metodo che server al RoundManager per potere avere un'istanza di Game per
	 * potere chiamare l'apposito metodo del gioco che serve a notificare ai
	 * giocatori il cambio del turno.
	 * 
	 * @param gameCurrent
	 *            L'istanza di Game creata al momento dell'inizializzazione del
	 *            server.
	 */
	public RoundManager(Game gameCurrent) {
		this.gameCurrent = gameCurrent;
	}

	/**
	 * Metodo che si occupa di stoppare e far ripartire coerentemente i timer e
	 * di chiamare il metodo di cambio turno che risiede nel gioco.
	 */
	public void changeRound() {

		gameCurrent.changeRound();// metodo nel gioco che invecchia mappa e
									// schifezze varie

		roundConfirmed = false;

		if (t2 != null) {
			t2.cancel();
		}
		if (t30 != null) {
			t30.cancel();
		}
		t30 = new Timer();
		t30.schedule(new TimerTaskRound(this), 30 * 1000);

	}

	/**
	 * Invocato nel momento in cui un utente ha accettato il turno. Si occupa di
	 * stoppare il timer da 30 secondi per fare partire quello da 120.
	 */
	public void acceptRound() {

		if (roundConfirmed) {
			return;
		}

		if (t30 != null) {
			t30.cancel();
		}
		roundConfirmed = true;
		t2 = new Timer();
		t2.schedule(new TimerTaskRound(this), 120 * 1000);
	}

	/**
	 * Metodo invocato dal Game quando un utente passa il turno. Non fa altro
	 * che chiamare changeround poiche' quest'ultimo stesso si occupa di
	 * resettare i timer in modo opportuno.
	 */
	public void passRound() {
		changeRound();
	}

	/**
	 * Metodo usato dal Game che viene chiamato all'accesso del primo utente.
	 * Chiama il changeRound che al primo avvio si occupa di settare i timer in
	 * modo opportuno.
	 */
	public void start() {
		changeRound();
	}

	/**
	 * Si occupa di stoppare tutti i timer. Questo metodo viene chiamato dal
	 * Game solo nel momento in cui esce dal gioco l'ultimo giocatore.
	 */
	public void stop() {

		roundConfirmed = false;

		if (t30 != null) {
			t30.cancel();
		}

		if (t2 != null) {
			t2.cancel();
		}
	}
}
