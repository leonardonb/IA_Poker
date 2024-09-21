import java.util.Random;

public class AgenteRandomico implements Agente {

	private Random rand;

	public AgenteRandomico() {
		rand = new Random();
	}

	@Override
	public int getPrimeiraAposta(Carta[] jogo) {
		return rand.nextInt(50);
	}

	@Override
	public boolean getSegundaAposta(int apostaMaisAlta) {
		return rand.nextBoolean();
	}

}
