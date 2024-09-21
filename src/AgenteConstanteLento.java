public class AgenteConstanteLento implements Agente {

	long tempoEspera;

	public AgenteConstanteLento(long tempoDeEspera) {
		this.tempoEspera = tempoDeEspera;
	}

	@Override
	public int getPrimeiraAposta(Carta[] jogo) {
		try {
			Thread.sleep(tempoEspera);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 30;
	}

	@Override
	public boolean getSegundaAposta(int apostaMaisAlta) {
		try {
			Thread.sleep(tempoEspera);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

}
