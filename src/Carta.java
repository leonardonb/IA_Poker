import java.util.Random;

public class Carta {

	int valor;
	char naipe;

	public static final char[] naipes = new char[] { 'O', 'E', 'P', 'C' };
	public static final int[] valores = new int[] { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };

	public Carta(int valor, char naipe) {
		super();
		this.valor = valor;
		this.naipe = naipe;
	}

	/**
	 * Retorna um baralho embaralhado
	 * 
	 * @return
	 */
	public static Carta[] getBaralho() {
		Carta[] baralho = new Carta[52];

		// construindo o baralho
		int w = 0;
		for (int i = 0; i < naipes.length; i++) {
			for (int j = 0; j < valores.length; j++) {
				baralho[w] = new Carta(valores[j], naipes[i]);
				w++;
			}
		}

		// embaralhando
		Random r = new Random();
		Carta temp;
		for (int i = 0; i < baralho.length; i++) {
			temp = baralho[i];
			w = r.nextInt(baralho.length);
			baralho[i] = baralho[w];
			baralho[w] = temp;
		}

		return baralho;

	}

	// faz uma cópia do jogo para encaminhar para o agente
	public static Carta[] copia(Carta[] jogo) {
		Carta[] resp = new Carta[jogo.length];
		for (int i = 0; i < resp.length; i++) {
			resp[i] = new Carta(jogo[i].valor, jogo[i].naipe);
		}
		return resp;
	}

	public static String printCartas(Carta[] jogo) {
		String a = "";
		for (int i = 0; i < jogo.length; i++) {
			a += jogo[i].valor + "." + jogo[i].naipe + ", ";
		}
		return a;
	}

}
