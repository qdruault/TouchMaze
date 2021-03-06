package tacticon;

public class Circle extends Tacticon {

	// Pour connaitre l'�tape de dessin du motif.
	static int compteur = 0;

	/**
	 * Fonction appel�e toutes les 100 ms pour lever des picots qui simulent le
	 * mouvement d'une spirale.
	 * 
	 * @return le tableau � transmettre � l'appli bluetooth.
	 */
	static byte[] SetToByte() {
		boolean[] rightTouches = { false, false, false, false, false, false, false, false };
		boolean[] leftTouches = { false, false, false, false, false, false, false, false };

		// Leve le picot qu'il faut.
		switch (compteur) {
		case 0:
			leftTouches[0] = true;
			break;
		case 1:
			leftTouches[1] = true;
			break;
		case 2:
			rightTouches[0] = true;
			break;
		case 3:
			rightTouches[1] = true;
			break;
		case 4:
			rightTouches[3] = true;
			break;
		case 5:
			rightTouches[5] = true;
			break;
		case 6:
			rightTouches[7] = true;
			break;
		case 7:
			rightTouches[6] = true;
			break;
		case 8:
			leftTouches[7] = true;
			break;
		case 9:
			leftTouches[6] = true;
			break;
		case 10:
			leftTouches[4] = true;
			break;
		case 11:
			leftTouches[2] = true;
			break;
		}

		// On attend 100 ms.
		stopThread();
		// On passe � l'�tape suivante.
		compteur = (compteur + 1) % 12;

		// On pr�pare les donn�es � envoyer.
		byte[] data = new byte[4];
		data[0] = 0x1b;
		data[1] = 0x01;
		data[2] = regularBoolToByte(rectifyTouches(leftTouches));
		data[3] = regularBoolToByte(rectifyTouches(rightTouches));

		return data;
	}

	// Permet l'attente du Thread pour un temps donn�.
	static void stopThread() {
		try {
			// Pause de 100 ms.
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Convertir le tableau de bool en byte.
	 * 
	 * @param p_tab
	 *            : le tableau de bool � convertir
	 * @return
	 */
	static byte regularBoolToByte(boolean[] p_tab) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			// Remplace les false par 0 et les true par 1.
			output.append(p_tab[i] ? '1' : '0');
		}
		return (byte) Integer.parseInt(output.toString(), 2);
	}

	/**
	 * Remet le tableau dans l'ordre. Don't ask me why. I don't know
	 */
	static boolean[] rectifyTouches(boolean[] t) {
		return new boolean[] { t[1], t[0], t[3], t[5], t[7], t[2], t[4], t[6] };
	}

}
