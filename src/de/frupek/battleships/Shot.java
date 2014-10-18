package de.frupek.battleships;

/**
 * Repräsentiert einen Schuss
 * @author Julius Mischok
 *
 */
public class Shot {
	private final int x;
	private final int y;
	
	/**
	 * Konstruktor
	 * @param x Nullbasierte X-Koordinate des Schusses
	 * @param y Nullbasierte Y-Koordinate des Schusses
	 */
	public Shot(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
}
