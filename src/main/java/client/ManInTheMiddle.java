package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedOutputStream;
import java.net.Socket;

import GUI.WindowGame;

/**
 * @author  gas12n
 */
public class ManInTheMiddle extends Thread {

	// iSR = new InputStreamReader(socket.getInputStream());
	// buffReader = new BufferedReader(iSR);

	/**
	 * @uml.property  name="winGame"
	 * @uml.associationEnd  
	 */
	private WindowGame winGame;
	private OutputStreamWriter oSW;
	private BufferedWriter buffWriter;
	private BufferedReader buffReader;
	private InputStreamReader iSR;
	private PipedOutputStream pipeOutput;

	public ManInTheMiddle(Socket socket, PipedOutputStream pipeOutput) {
		this.pipeOutput = pipeOutput;

		try {
			iSR = new InputStreamReader(socket.getInputStream());
			buffReader = new BufferedReader(iSR);
			oSW = new OutputStreamWriter(pipeOutput);
			buffWriter = new BufferedWriter(oSW);
		} catch (IOException e) {
			//System.out.println("Server Crashed");
		}

	}

	public void setWindowGame(WindowGame windowGame) {
		this.winGame = windowGame;
	}

	@Override
	public void run() {
		while (true) {

			String buf = null;

			try {
				buf = buffReader.readLine();
			} catch (IOException e) {
				buf = "";
			}

			if(buf!=null){
			if (buf.matches("^@cambioTurno,\\w+")) {
				if (winGame != null) {
					(new TaskChangeRound(buf.substring(13), winGame)).start();
				}
			} else {
				try {
					buffWriter.write(buf);
					buffWriter.newLine();
					buffWriter.flush();
				} catch (IOException e) {
				}
			}
			}
		}
	}
}
