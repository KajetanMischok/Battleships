package de.frupek.battleships;

import java.util.ArrayList;
import java.util.List;

import de.frupek.battleships.helpers.Util;


public class Grid {
	private final List<Ship> ships = new ArrayList<>();
	private final List<Shot> shots = new ArrayList<>();
	private final int rows;
	private final int cols;
	
	/**
	 * Konstruktor
	 * @param rows Zeilen
	 * @param cols Spalten
	 */
	public Grid(int rows, int cols) {
		Util.require(rows >= 0, "Rowcount has to be positive");
		Util.require(cols >= 0, "Columncount has to be positive");
		
		this.rows = rows;
		this.cols = cols;
	}

	/**
	 * @return the ships
	 */
	private List<Ship> getShips() {
		return ships;
	}

	/**
	 * @return the shots
	 */
	private List<Shot> getShots() {
		return shots;
	}

	/**
	 * @return the rows
	 */
	private int getRows() {
		return rows;
	}

	/**
	 * @return the cols
	 */
	private int getCols() {
		return cols;
	}
}
