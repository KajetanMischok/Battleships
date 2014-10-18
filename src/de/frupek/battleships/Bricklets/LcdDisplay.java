package de.frupek.battleships.Bricklets;

import java.io.IOException;
import java.net.UnknownHostException;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import de.frupek.battleships.Grid;

/**
 * Stellt ein Battleshipsgrid auf einem Tinbkerforge BrickletLCD20x4 dar.
 * 
 * @author Kajetan Mischok
 */
public class LcdDisplay extends Bricklet<BrickletLCD20x4> {

	public LcdDisplay(String host, int port, String uid) {
		super(host, port, uid);
	}

	public void connect() throws UnknownHostException,
			AlreadyConnectedException, IOException, TimeoutException,
			NotConnectedException {
		IPConnection ipcon = new IPConnection();
		BrickletLCD20x4 lcd = new BrickletLCD20x4(this.getUid(), ipcon);
		this.setBricklet(lcd);

		ipcon.connect(this.getHost(), this.getPort());
	}

	public void writeFirstLine(String text) throws TimeoutException,
			NotConnectedException {
		this.clearLine(0);
		this.getBricklet().writeLine((short) 0, (short) 0, text);

	}
	
	public void showGrid(Grid grid){
	}

	private void clearDisplay() throws TimeoutException, NotConnectedException {
		for (int i = 0; i < 3; i++) {
			this.clearLine(i);
		}
	}

	private void clearLine(int line) throws TimeoutException,
			NotConnectedException {
		this.getBricklet().writeLine((short) line, (short) 0,
				"                    ");
	}
}
