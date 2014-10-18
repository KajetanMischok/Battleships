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
import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.BrickletMultiTouch;
import com.tinkerforge.BrickletMultiTouch.TouchStateListener;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * Klasse, die auf eine Spielanfrage wartet oder eine eingehende 
 * Spielanfrage bearbeitet
 * @author Julius Mischok
 *
 */
public class Battle implements MqttCallback, TouchStateListener {
	private final MqttAsyncClient mqttClient;
	
	private static final String HOST = "localhost";
    private static final int PORT = 4223;
    private static final String UID_LCD = "od5";
    private static final String UID_TOUCH = "jVL";
    
    public final BrickletLCD20x4 lcd;
    public final BrickletMultiTouch touch;
    public final IPConnection ipcon;
	
	public Battle(String host, int port, String brokerUri) throws MqttException, UnknownHostException, AlreadyConnectedException, IOException {
		this.ipcon = new IPConnection();
        this.lcd = new BrickletLCD20x4(UID_LCD, this.getIpcon());
        this.touch = new BrickletMultiTouch(UID_TOUCH, this.getIpcon());
        
        this.getIpcon().connect(host, port);
        
        this.mqttClient = new MqttAsyncClient(brokerUri, MqttClient.generateClientId());
		
		this.getMqttClient().connect().waitForCompletion();
		this.getMqttClient().setCallback(this);
		this.getMqttClient().subscribe("games/#", 0);
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
		System.out.println("MESSAGE " + arg1.getPayload().toString() + " RECEIVED IN " + arg0);
	}
	

	@Override
	public void touchState(int state) {
		for(int i = 0; i < 12; i++) {
            if((state & (1 << i)) == (1 << i)) {
            	try {
					this.writeLine(2, "key " + i + " touched");
					
					// Set to value
					if (i>=2) {
						this.setCallbackPeriod(250 * i);
					}
					
					// Decrease
					if (i == 0) {
						this.setCallbackPeriod(Math.max(0, this.getCallbackPeriod()-250));
					}
					
					// Increase
					if (i == 1) {
						this.setCallbackPeriod(Math.max(0, this.getCallbackPeriod()+250));
					}
					
					// Configure an show new period
					this.getAl().setIlluminanceCallbackPeriod(this.getCallbackPeriod());
					this.writeLine(1, "period: " + this.getCallbackPeriod());
					
				} catch (TimeoutException e) {
					e.printStackTrace();
				} catch (NotConnectedException e) {
					e.printStackTrace();
				}
            }
        }
	}
}
