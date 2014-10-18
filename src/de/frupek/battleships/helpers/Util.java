package de.frupek.battleships.helpers;

public class Util {
	/**
	 * Prüft, ob eine Bedingung gilt und wirft ggf. eine 
	 * IllegalArgumentException.
	 * @param condition Bedingung
	 * @param message Fehlermeldung
	 */
	public static void require(boolean condition, String message) {
		if (condition == false) {
			throw new IllegalArgumentException(message);
		}
	}
}
