package server;

import java.io.*;

public class mainServer {
/**
 * Il metodo starta un nuovo oggetto di tipo Server
 * @param args
 * @throws IOException
 */
	public static void main(String[] args) throws IOException {

		new Server().run();

	}

}
