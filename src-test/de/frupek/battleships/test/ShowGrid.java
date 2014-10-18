package de.frupek.battleships.test;

import java.util.ArrayList;
import java.util.List;

import de.frupek.battleships.Grid;
import de.frupek.battleships.Ship;

public class ShowGrid {
	public static void main(String[] args) {
		List<Ship> ships = new ArrayList<>();
		ships.add(new Ship(2, 0, 1, false));
		ships.add(new Ship(4, 0, 1, false));
		ships.add(new Ship(2, 0, 1, false));
		ships.add(new Ship(2, 0, 1, false));
		ships.add(new Ship(2, 0, 1, false));
		
		
		Grid grid = new Grid(3, 20, ships);
	}
}
