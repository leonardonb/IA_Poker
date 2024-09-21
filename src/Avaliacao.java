import java.text.DecimalFormat;

public class Avaliacao {

	int pontos;
	String descricao;

	static final DecimalFormat df = new DecimalFormat("##,##,#0");

	public Avaliacao() {
	}

	@Override
	public String toString() {
		return "[" + df.format(this.pontos) + " pontos] = " + descricao;
	}

	/**
	 * Pontos = ParteA + ParteB + ParteC.
	 * 
	 * ParteA = 225 * mão (par = 1, dois pares = 2, trinca = 3, sequencia = 4, flush
	 * = 5, full house= 6, quadra = 7, straight flush = 8)
	 * 
	 * ParteB = 15 * + carta mais alta do tipo de jogo (par, dois pares, ...)
	 * 
	 * ParteC = carta mais alta das 5.
	 * 
	 * @param jogo
	 * @return
	 */
	public static Avaliacao avaliaJogo(Carta[] jogo) {
		int cartaMaisAlta = cartaMaisAlta(jogo);
		int respPontos = cartaMaisAlta; // ParteC + ParteD
		String respDescricao = "-";

		int pontosJogo = 10000;
		int pontosCartaJogo = 100;

		int naipe = mesmoNaipe(jogo);
		int sequencia = sequencia(jogo);
		int quadra = quadra(jogo);
		int full = fullHouse(jogo);
		int trinca = trinca(jogo);
		int doisPares = doisPares(jogo);
		int par = par(jogo);

		if ((naipe > 0) && (sequencia > 0)) {
			respPontos += pontosJogo * 8 + pontosCartaJogo * sequencia;
			respDescricao = "Seqüência de Naipe";
		} else if (quadra > 0) {
			respPontos += pontosJogo * 7 + pontosCartaJogo * quadra;
			respDescricao = "Quadra de " + quadra;
		} else if (full > 0) {
			respPontos += pontosJogo * 6 + pontosCartaJogo * full;
			respDescricao = "Dupla e Trinca com " + full;
		} else if (naipe > 0) {
			respPontos += pontosJogo * 5 + pontosCartaJogo * naipe;
			respDescricao = "Mesmo Naipe";
		} else if (sequencia > 0) {
			respPontos += pontosJogo * 4 + pontosCartaJogo * sequencia;
			respDescricao = "Seqüência";
		} else if (trinca > 0) {
			respPontos += pontosJogo * 3 + pontosCartaJogo * trinca;
			respDescricao = "Trinca de " + trinca;
		} else if (doisPares > 0) {
			respPontos += pontosJogo * 2 + pontosCartaJogo * doisPares;
			respDescricao = "Dois pares com " + doisPares;
		} else if (par > 0) {
			respPontos += pontosJogo * 1 + pontosCartaJogo * par;
			respDescricao = "Par de " + par;
		} else
			respDescricao = "nada";

		respDescricao += ", carta mais alta " + cartaMaisAlta;

		Avaliacao resp = new Avaliacao();
		resp.descricao = respDescricao;
		resp.pontos = respPontos;
		return resp;

	}

	/**
	 * Retorna o valor da maior carta, das 2, se for um par ou -1 caso contrário
	 * 
	 * @param jogo
	 * @return
	 */
	private static int par(Carta[] jogo) {
		int resp = -1;
		// copias os valores para v
		int[] v = new int[jogo.length];
		for (int i = 0; i < v.length; i++) {
			v[i] = jogo[i].valor;
		}
		// compara os pares
		if (v[0] == v[1]) {
			resp = v[0];
		} else if (v[1] == v[2]) {
			resp = v[1];
		} else if (v[2] == v[3]) {
			resp = v[2];
		} else if (v[3] == v[4]) {
			resp = v[4];
		}
		return resp;
	}

	/**
	 * Retorna o valor da maior carta, das 4, se forem dois pares ou -1 caso
	 * contrário
	 * 
	 * @param jogo
	 * @return
	 */
	private static int doisPares(Carta[] jogo) {
		int resp = -1;
		// copias os valores para v
		int[] v = new int[jogo.length];
		for (int i = 0; i < v.length; i++) {
			v[i] = jogo[i].valor;
		}
		// compara os pares
		if ((v[0] == v[1]) & (v[2] == v[3])) {
			resp = Math.max(v[0], v[3]);
		} else if ((v[0] == v[1]) & (v[3] == v[4])) {
			resp = Math.max(v[0], v[3]);
		} else if ((v[1] == v[2]) & (v[3] == v[4])) {
			resp = Math.max(v[1], v[3]);
		}
		return resp;
	}

	/**
	 * Retorna o valor da maior carta, das 3, se for uma trinca ou -1 caso contrário
	 * 
	 * @param jogo
	 * @return
	 */
	private static int trinca(Carta[] jogo) {
		int resp = -1;
		boolean[] trinca = new boolean[3];
		for (int i = 0; i < trinca.length; i++) {
			trinca[i] = (jogo[i].valor == jogo[i + 1].valor) & (jogo[i].valor == jogo[i + 2].valor);
			if (trinca[i]) {
				resp = jogo[i].valor;
				i = trinca.length;// break
			}
		}
		return resp;
	}

	/**
	 * Retorna o valor da maior carta, das 5, se for um fullHouse ou -1 caso
	 * contrário
	 * 
	 * @param jogo
	 * @return
	 */
	private static int fullHouse(Carta[] jogo) {
		boolean a = jogo[0].valor == jogo[1].valor;
		boolean b = jogo[3].valor == jogo[4].valor;
		boolean c = jogo[2].valor == jogo[1].valor;
		boolean d = jogo[2].valor == jogo[3].valor;
		boolean e = c | d;
		boolean f = a & b & e;
		int resp = -1;
		if (f)
			resp = cartaMaisAlta(jogo);
		return resp;

	}

	/**
	 * Retorna o valor da maior carta se for uma quadra ou -1 caso contrário
	 * 
	 * @param jogo
	 * @return
	 */
	private static int quadra(Carta[] jogo) {
		boolean a = (jogo[0].valor == jogo[1].valor) & (jogo[0].valor == jogo[2].valor)
				& (jogo[0].valor == jogo[3].valor);
		boolean b = (jogo[4].valor == jogo[1].valor) & (jogo[4].valor == jogo[2].valor)
				& (jogo[4].valor == jogo[3].valor);
		boolean igual = a | b;
		int resp = -1;
		if (igual)
			resp = jogo[2].valor;
		return resp;
	}

	/**
	 * Retorna o valor da carta mais alta do array
	 * 
	 * @param jogo
	 * @return
	 */
	private static int cartaMaisAlta(Carta[] jogo) {
		int r = -1;
		for (int i = 0; i < jogo.length; i++) {
			r = Math.max(r, jogo[i].valor);
		}
		return r;
	}

	/**
	 * Retorna o valor da maior carta se for uma sequencia ou -1 caso contrário
	 * 
	 * @param jogo
	 * @return
	 */
	private static int sequencia(Carta[] jogo) {
		boolean ordenado = true;
		for (int i = 1; i < jogo.length; i++) {
			ordenado = (ordenado & (jogo[i - 1].valor - 1 == jogo[i].valor));
		}
		int resp = -1;
		if (ordenado)
			resp = cartaMaisAlta(jogo);
		return resp;
	}

	/**
	 * Retorna o valor da maior carta se todas forem do mesmo naipe ou -1 caso
	 * contrário
	 * 
	 * @param jogo
	 * @return
	 */
	private static int mesmoNaipe(Carta[] jogo) {
		boolean mesmo = true;
		for (int i = 1; i < jogo.length; i++) {
			mesmo = (mesmo & jogo[i].naipe == jogo[0].naipe);
		}
		int resp = -1;
		if (mesmo)
			resp = cartaMaisAlta(jogo);
		return resp;
	}

	public static void main(String[] args) {
		Carta[] jogo = new Carta[] { new Carta(14, 'O'), new Carta(13, 'O'), new Carta(12, 'O'), new Carta(11, 'O'),
				new Carta(10, 'O') };
		System.out.println(Carta.printCartas(jogo) + "\t" + Avaliacao.avaliaJogo(jogo));

		jogo = new Carta[] { new Carta(9, 'O'), new Carta(8, 'O'), new Carta(7, 'O'), new Carta(6, 'O'),
				new Carta(5, 'O') };
		System.out.println(Carta.printCartas(jogo) + "\t" + Avaliacao.avaliaJogo(jogo));

		jogo = new Carta[] { new Carta(14, 'C'), new Carta(13, 'O'), new Carta(12, 'O'), new Carta(11, 'O'),
				new Carta(10, 'O') };
		System.out.println(Carta.printCartas(jogo) + "\t" + Avaliacao.avaliaJogo(jogo));

		jogo = new Carta[] { new Carta(9, 'C'), new Carta(8, 'O'), new Carta(7, 'O'), new Carta(6, 'O'),
				new Carta(5, 'O') };
		System.out.println(Carta.printCartas(jogo) + "\t" + Avaliacao.avaliaJogo(jogo));

		jogo = new Carta[] { new Carta(7, 'O'), new Carta(13, 'O'), new Carta(12, 'O'), new Carta(11, 'O'),
				new Carta(10, 'O') };
		System.out.println(Carta.printCartas(jogo) + "\t" + Avaliacao.avaliaJogo(jogo));

		// quadra 1
		jogo = new Carta[] { new Carta(7, 'C'), new Carta(7, 'O'), new Carta(7, 'O'), new Carta(7, 'O'),
				new Carta(10, 'O') };
		System.out.println(Carta.printCartas(jogo) + "\t" + Avaliacao.avaliaJogo(jogo));

		// quadra 2
		jogo = new Carta[] { new Carta(8, 'C'), new Carta(7, 'O'), new Carta(7, 'O'), new Carta(7, 'O'),
				new Carta(7, 'O') };
		System.out.println(Carta.printCartas(jogo) + "\t" + Avaliacao.avaliaJogo(jogo));

		jogo = new Carta[] { new Carta(7, 'C'), new Carta(7, 'O'), new Carta(7, 'O'), new Carta(11, 'O'),
				new Carta(10, 'O') };
		System.out.println(Carta.printCartas(jogo) + "\t" + Avaliacao.avaliaJogo(jogo));

		jogo = new Carta[] { new Carta(7, 'C'), new Carta(7, 'O'), new Carta(7, 'O'), new Carta(10, 'O'),
				new Carta(10, 'O') };
		System.out.println(Carta.printCartas(jogo) + "\t" + Avaliacao.avaliaJogo(jogo));

		jogo = new Carta[] { new Carta(7, 'C'), new Carta(7, 'O'), new Carta(8, 'O'), new Carta(8, 'O'),
				new Carta(10, 'O') };
		System.out.println(Carta.printCartas(jogo) + "\t" + Avaliacao.avaliaJogo(jogo));

		jogo = new Carta[] { new Carta(7, 'C'), new Carta(7, 'O'), new Carta(12, 'O'), new Carta(11, 'O'),
				new Carta(10, 'O') };
		System.out.println(Carta.printCartas(jogo) + "\t" + Avaliacao.avaliaJogo(jogo));

		jogo = new Carta[] { new Carta(7, 'C'), new Carta(13, 'O'), new Carta(12, 'O'), new Carta(11, 'O'),
				new Carta(10, 'O') };
		System.out.println(Carta.printCartas(jogo) + "\t" + Avaliacao.avaliaJogo(jogo));
	}

}
