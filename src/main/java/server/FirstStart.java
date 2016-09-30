package server;

/**
 * @author  gas12n
 */
public class FirstStart extends Thread {

	/**
	 * @uml.property  name="rm"
	 * @uml.associationEnd  
	 */
	private RoundManager rm;
	private int waitMilliSecond = 3500;
	
	public FirstStart(RoundManager rm) {
		this.rm = rm;
	}

	public static void waiting(int n) {

		long t0, t1;

		t0 = System.currentTimeMillis();

		do {
			t1 = System.currentTimeMillis();
		} while (t1 - t0 < n);
	}

	@Override
	public void run() {
		waiting(waitMilliSecond);
		rm.start();
	}

}
