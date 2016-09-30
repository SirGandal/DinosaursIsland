package server;

import java.util.TimerTask;

/**
 * @author  gas12n
 */
public class TimerTaskRound extends TimerTask {

	/**
	 * @uml.property  name="rm"
	 * @uml.associationEnd  
	 */
	private RoundManager rm;

	public TimerTaskRound(RoundManager rm) {
		this.rm = rm;
	}

	@Override
	public void run() {
		rm.changeRound();
	}

}
