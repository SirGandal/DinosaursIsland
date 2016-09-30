package map;

public class Rand {
/**
 * Genera l'energia della vegetazione in modo random
 * @return L'energia della vegetazione
 */
	public static int generateEnergyVegetationRandom() {
		/* genero energia per la vegetazione in valore compreso tra 150 e 350 */
		int min = 150;
		int max = 350;

		return (min + Math.abs((int) (Math.random() * ((max - min) + 1))));
	}

	/**
	 * Genera l'energia delle carogne in modo random
	 * @return L'energia della carogna
	 */
	public static int generateEnergyCarrionRandom() {
		/* genero energia per la carogna in valore compreso tra 350 e 650 */
		int min = 500;
		int max = 1000;

		return (min + Math.abs((int) (Math.random() * ((max - min) + 1))));
	}

}
