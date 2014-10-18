package de.frupek.battleships;

/**
 * Repräsentiert ein Schiff auf dem Display
 * @author Julius Mischok
 *
 */
public class Ship {
	private final int length;
	private final int startX;
	private final int startY;
	private final boolean horizontal;
	
	/**
	 * Konstruktor
	 * @param length Länge des Schiffs
	 * @param startX Nullbasierte X-Koordinate
	 * @param startY Nullbasierte Y-Koordinate
	 * @param horizontal true, falls das Schiff waagerecht liegt, sonst false
	 */
	public Ship(int length, int startX, int startY, boolean horizontal) {
		this.length = length;
		this.startX = startX;
		this.startY = startY;
		this.horizontal = horizontal;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return the startX
	 */
	public int getStartX() {
		return startX;
	}

	/**
	 * @return the startY
	 */
	public int getStartY() {
		return startY;
	}

	/**
	 * @return the horizontal
	 */
	public boolean isHorizontal() {
		return horizontal;
	}
}
