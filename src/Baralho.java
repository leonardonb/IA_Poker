public class Baralho {

	private Carta[] baralho;
	private int posicao;

	public Baralho() {
		baralho = Carta.getBaralho();
		posicao = 0;

	}

	// puxa 5 cartas do baralho
	public Carta[] get5cartas() {
		Carta[] jogo = new Carta[5];
		for (int i = 0; i < jogo.length; i++) {
			jogo[i] = baralho[posicao];
			posicao++;
		}
		// ordena da maior para menor
		Carta temp;
		for (int i = 0; i < jogo.length; i++) {
			for (int j = i + 1; j < jogo.length; j++) {
				if (jogo[i].valor < jogo[j].valor) {
					temp = jogo[i];
					jogo[i] = jogo[j];
					jogo[j] = temp;
				}
			}
		}
		return jogo;
	}

	

}
