package en.bleamblema.java2dgame;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import en.bleamblema.java2dgame.net.packets.Packet01Disconnect;

public class WindowHandler implements WindowListener {
	
	private final Game game;
	public WindowHandler(Game game){
		this.game = game;
		this.game.frame.addWindowListener(this);
	}

	public void windowActivated(WindowEvent event) {
		
	}

	public void windowClosed(WindowEvent event) {
		
	}

	public void windowClosing(WindowEvent event) {
		Packet01Disconnect packet = new Packet01Disconnect(this.game.player.getUsername());
		packet.writeData(this.game.socketClient);
	}

	public void windowDeactivated(WindowEvent event) {
		
	}

	public void windowDeiconified(WindowEvent event) {
		
	}

	public void windowIconified(WindowEvent event) {
		
	}

	public void windowOpened(WindowEvent event) {
		
	}

}
