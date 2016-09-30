package client;

import server.Game;
import server.Server;

public class ServerForTest extends Thread {
	private Server s = new Server();
	
	public void run(){
		s.run();
	}
	public Game getGame(){
		return s.getGame();
	}
	public void setLoop(boolean loop){
		s.setLoop(loop);
	}
	public void setPort(int port){
		s.setPort(port);
	}
	
	public void setRMIport(int port){
		s.setRMIport(port);
	}
}
