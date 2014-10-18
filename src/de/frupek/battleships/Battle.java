package de.frupek.battleships;

import java.io.IOException;
import java.net.UnknownHostException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.BrickletMultiTouch;
import com.tinkerforge.BrickletMultiTouch.TouchStateListener;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * Klasse, die auf eine Spielanfrage wartet oder eine eingehende Spielanfrage
 * bearbeitet
 * 
 * @author Julius Mischok
 * 
 */
public class Battle implements MqttCallback, TouchStateListener {
	private final MqttAsyncClient mqttClient;
	public final BrickletLCD20x4 lcd;
	public final BrickletMultiTouch touch;
	public final IPConnection ipcon;

	private Role actualRole;
	private String actualGameTopic;
	private int secretNumber;

	private enum Role {
		INITIATOR, PLAYER
	}

	private static final String HOST = "localhost";
	private static final int PORT = 4223;

	public Battle(String host, int port, String brokerUri)
			throws MqttException, UnknownHostException,
			AlreadyConnectedException, IOException, TimeoutException,
			NotConnectedException {
		this.ipcon = new IPConnection();
		this.lcd = new BrickletLCD20x4(Conf.UID_LCD, this.getIpcon());
		this.touch = new BrickletMultiTouch(Conf.UID_TOUCH, this.getIpcon());
		this.getTouch().addTouchStateListener(this);

		this.getIpcon().connect(host, port);

		this.getLcd().backlightOn();
		this.getLcd().clearDisplay();

		this.mqttClient = new MqttAsyncClient(brokerUri,
				MqttClient.generateClientId());

		this.getMqttClient().connect().waitForCompletion();
		this.getMqttClient().setCallback(this);
		this.getMqttClient().subscribe("games/#", 0);

		this.writeLine(0, "Zum Starten tippen");
	}

	private int getSecretNumber() {
		return secretNumber;
	}

	private void setSecretNumber(int secretNumber) {
		this.secretNumber = secretNumber;
	}

	/**
	 * @return the lcd
	 */
	private BrickletLCD20x4 getLcd() {
		return lcd;
	}

	/**
	 * @return the touch
	 */
	private BrickletMultiTouch getTouch() {
		return touch;
	}

	/**
	 * @return the ipcon
	 */
	private IPConnection getIpcon() {
		return ipcon;
	}

	/**
	 * @return the mqttClient
	 */
	private MqttAsyncClient getMqttClient() {
		return mqttClient;
	}

	@Override
	public void connectionLost(Throwable arg0) {
		System.out.println("CONNECTION LOST");
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {

	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		if (this.getActualRole() == null) {
			// Das ist eine neue Challenge
			Integer i = Integer.parseInt(new String(arg1.getPayload()));
			this.setSecretNumber(i);
			this.writeLine(0, "Bitte raten!");
			this.writeLine(1, "Geratene Zahl tippen.");
			this.setActualRole(Role.PLAYER);
		}
		if (this.getActualRole().equals(Role.INITIATOR)) {

		}

		this.writeLine(1, new String(arg1.getPayload()) + " empfangen");
	}

	/**
	 * @return the actualRole
	 */
	private Role getActualRole() {
		return actualRole;
	}

	/**
	 * @param actualRole
	 *            the actualRole to set
	 */
	private void setActualRole(Role actualRole) {
		this.actualRole = actualRole;
	}

	@Override
	public void touchState(int state) {
		for (int i = 0; i < 12; i++) {
			if ((state & (1 << i)) == (1 << i)) {
				try {
					if (this.getActualRole() == null) {
						this.setActualGameTopic("games/"
								+ System.currentTimeMillis());
						this.getMqttClient().publish(this.getActualGameTopic(),
								("" + i).getBytes(), 0, false);
						this.writeLine(0, "Warte auf Antwort.");
					} else if (this.getActualRole().equals(Role.INITIATOR)) {

					} else if (this.getActualRole().equals(Role.PLAYER)) {
						if (this.getSecretNumber() == i) {
							this.getMqttClient().publish(this.getActualGameTopic() + "/response",
									("Du hast verloren!").getBytes(), 0, false);
							this.writeLine(0, "Richtig! Du hast gewonnen!");
						} else {
							this.getMqttClient().publish(
									this.getActualGameTopic() + "/response",
									("Du hast gewonnen!").getBytes(), 0, false);
							this.writeLine(0, "Falsch! Du hast verloren!");
						}
					}
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void writeLine(int line, String str) {
		try {
			// Clear line
			this.getLcd().writeLine((short) line, (short) 0,
					"                          ");

			// Show new String
			this.getLcd().writeLine((short) line, (short) 0, str);
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		Battle bat = new Battle(HOST, PORT, Conf.BROKER_URI);

		System.out.println("Press key to exit");
		System.in.read();
		bat.ipcon.disconnect();
	}

	public String getActualGameTopic() {
		return actualGameTopic;
	}

	public void setActualGameTopic(String actualGameTopic) {
		this.actualGameTopic = actualGameTopic;
	}
}
