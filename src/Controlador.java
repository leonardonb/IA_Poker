
public class Controlador extends Thread {

	// variáveis de comunicação
	boolean primeiraAposta;
	boolean segundaAposta;
	int apostaMaisAlta;
	Carta[] jogo;
	int pontos;
	int aposta;
	boolean pagaPraVer;

	// variávies do controle
	Agente agente;
	int saldo;

	public Controlador(Agente agente, int saldo, String nome) {
		super();
		this.agente = agente;
		this.saldo = saldo;
		this.setName(nome);
	}

	public synchronized void setCartas(Carta[] cartas) {
		jogo = cartas;
		aposta = 0;
		primeiraAposta = true;
	}

	public synchronized void setPagaPraVer(int apostaMaisAlta) {
		pagaPraVer = false;
		this.apostaMaisAlta = apostaMaisAlta;
		segundaAposta = true;
	}

	@Override
	public void run() {
		while (true) {
			try {
				synchronized (this) {
					this.wait(); // Aguarda até que algum agendamento ocorra.
				}
			} catch (InterruptedException e) {
//				e.printStackTrace();
				System.out.println(this.getName() + ": Erro no Controlador");
			}

			if (primeiraAposta) {
				aposta = agente.getPrimeiraAposta(jogo);
				primeiraAposta = false;
			}
			if (segundaAposta) {
				pagaPraVer = agente.getSegundaAposta(apostaMaisAlta);
				segundaAposta = false;
			}

		}
	}

}
