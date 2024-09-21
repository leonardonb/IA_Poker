public class AgenteConstante implements Agente {

	public AgenteConstante() {
	}

	@Override
	public int getPrimeiraAposta(Carta[] jogo) {
		return 30;
	}

	@Override
	public boolean getSegundaAposta(int apostaMaisAlta) {
		return true;
	}

}
