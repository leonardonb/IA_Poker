import java.util.Arrays;

public class Poker {

	static final int saldoInicial = 1000;
	static final long duracaoDoJogoEmSegundos = 800;
	static final long miliSegundosDeIntervalo = 1000;
	static final int apostaMinima = 10;
	static final boolean verbose = true;

	public static void main(String[] args) throws InterruptedException {

		Controlador[] jogadores = new Controlador[7];
		jogadores[0] = new Controlador(new AgenteProbabilistico(), saldoInicial, "Probabilistico 1");
		jogadores[1] = new Controlador(new AgenteConstante(), saldoInicial, "Const 2");
		jogadores[2] = new Controlador(new AgenteConstanteLento(miliSegundosDeIntervalo * 1000), saldoInicial,
				"Lento 3");
		jogadores[3] = new Controlador(new AgenteRandomico(), saldoInicial, "Rando 4");
		jogadores[4] = new Controlador(new AgenteConstante(), saldoInicial, "Const 5");
		jogadores[5] = new Controlador(new AgenteConstanteLento(miliSegundosDeIntervalo * 2000), saldoInicial,
				"Lento 6");
		jogadores[6] = new Controlador(new AgenteRandomico(), saldoInicial, "Rando 7");

		// Starta os agentes
		for (int i = 0; i < jogadores.length; i++) {
			jogadores[i].start();
		}

		long inicio = System.currentTimeMillis();
		while ((System.currentTimeMillis() - inicio) < duracaoDoJogoEmSegundos * 1000) {

			System.out.println("Tempo de jogo: " + (System.currentTimeMillis() - inicio) / 1000 + " segundos");

			if (verbose)
				System.out.println("\n PRIMEIRA APOSTA");
			// distribuia as cartas
			Baralho baralho = new Baralho();
			Carta[][] cartas = new Carta[jogadores.length][];
			int[] pontosJogos = new int[jogadores.length];
			for (int i = 0; i < cartas.length; i++) {
				cartas[i] = baralho.get5cartas();
				jogadores[i].setCartas(Carta.copia(cartas[i]));
				pontosJogos[i] = Avaliacao.avaliaJogo(cartas[i]).pontos;
				if (jogadores[i].saldo == 0)
					pontosJogos[i] = -1;
				else
					synchronized (jogadores[i]) {
						jogadores[i].notify(); // Avisa que houve um agendamento.
					}

			}
			// aguarda os agentes
			Thread.sleep(miliSegundosDeIntervalo);

			// apostas da partida
			int[] apostas = new int[jogadores.length];
			Arrays.fill(apostas, 0);
			int apostaMaisAlta = 0;
			for (int i = 0; i < jogadores.length; i++) {
				// pega a aposta, deve ser no m�nino a aposta m�nima e no m�ximo o saldo
				apostas[i] = Math.min(Math.max(apostaMinima, jogadores[i].aposta), jogadores[i].saldo);
				// atualiza o saldo
				jogadores[i].saldo -= apostas[i];

				if (verbose)
					System.out.println(Carta.printCartas(cartas[i]) + "\t" + jogadores[i].getName() + " - APOSTA "
							+ apostas[i] + "\t" + (Avaliacao.avaliaJogo(cartas[i])));
				// atualiza a aposta mais alta
				apostaMaisAlta = Math.max(apostaMaisAlta, apostas[i]);
			}

			// pergunta quem vai pagar para ver
			for (int i = 0; i < jogadores.length; i++) {
				jogadores[i].setPagaPraVer(apostaMaisAlta);
				synchronized (jogadores[i]) {
					jogadores[i].notify(); // Avisa que houve um agendamento.
				}
			}

			if (verbose)
				System.out.println("\n SEGUNDA APOSTA");
			// aguarda os agentes
			Thread.sleep(miliSegundosDeIntervalo);
			// avalia os jogos
			int pontosMaisAltos = -1;
			for (int i = 0; i < jogadores.length; i++) {
				// s� avalia quem paga para ver
				if (jogadores[i].pagaPraVer || (apostas[i] == apostaMaisAlta)) {
					int p = pontosJogos[i];
					if (p > pontosMaisAltos)
						pontosMaisAltos = p;

					// ajusta as apostas
					int apostaComplementar = apostaMaisAlta - apostas[i]; // calcula complemento
					apostaComplementar = Math.min(jogadores[i].saldo, apostaComplementar); // limita a aposta ao saldo
					apostas[i] += apostaComplementar; // atualiza o pote
					// substrai do saldo
					jogadores[i].saldo -= apostaComplementar;
					if (verbose)
						System.out.println(jogadores[i].getName() + " - APOSTA " + apostas[i]);
				} else {
					// tira os pontos do jogador que desistiu
					pontosJogos[i] = -1;
				}
			}
			// calcula a quantidade de cape�es que v�o dividir o pr�mio
			int divisor = 0;
			for (int i = 0; i < pontosJogos.length; i++) {
				if (pontosJogos[i] == pontosMaisAltos)
					divisor++;
			}
			// calcula o total do pote (soma das apostas)
			int pote = 0;
			for (int i = 0; i < apostas.length; i++) {
				pote += apostas[i];
			}
			// paga aos campe�es
			int premio = pote / divisor;
			for (int i = 0; i < apostas.length; i++) {
				if (pontosJogos[i] == pontosMaisAltos)
					jogadores[i].saldo += premio;
			}

			// mostra saldo
			System.out.println("\n SALDO");
			for (int i = 0; i < jogadores.length; i++) {
				System.out.println(jogadores[i].getName() + " R$ " + jogadores[i].saldo);
			}

			System.out.println("\n");
		}

	}

}
