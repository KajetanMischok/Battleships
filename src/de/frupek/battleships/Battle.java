package de.frupek.battleships;

import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert eine Partie
 * @author Julius Mischok
 *
 */
public class Battle {
	private final Grid myGrid;
	private final List<Shot> myShots;
	
	/**
	 * Konstruktor
	 * @param myGrid Instanz des Spielfelds
	 */
	public Battle(Grid myGrid) {
		this.myGrid = myGrid;
		this.myShots = new ArrayList<>();
	}
	
	
}
