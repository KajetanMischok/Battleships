package de.frupek.battleships.Bricklets;


/**
 * Basisklasse für Tinkerforge Bricklets
 * 
 * @author Kajetan Mischok
 *
 * @param <T> Bricklet Typ
 */

public abstract class Bricklet<T> {
	private String host;
	private int port;
	private String uid;
	private T bricklet;

	public Bricklet(String host, int port, String uid) {
		this.setHost(host);
		this.setPort(port);
		this.setUid(uid);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public T getBricklet() {
		return bricklet;
	}

	public void setBricklet(T bricklet) {
		this.bricklet = bricklet;
	}
}
