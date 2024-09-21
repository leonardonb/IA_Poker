public class AgenteProbabilistico implements Agente {
    long tempoEspera;

    public AgenteProbabilistico() {
      //  this.tempoEspera = tempoDeEspera;
    }

    // Método para avaliar a força da mão (exemplo simplificado)
    private double avaliarForcaDaMao(Carta[] jogo) {
        // Aqui você faria a lógica para avaliar a mão de poker
        // Por exemplo: um par, dois pares, trinca, straight, flush, etc.
        // O retorno seria um valor entre 0.0 (mão muito fraca) e 1.0 (mão muito forte)

        // Exemplo simplificado: se houver um par, a força é 0.3, se houver uma trinca, a força é 0.6, etc.
        if (temPar(jogo)) return 0.3;
        if (temTrinca(jogo)) return 0.6;
        if (temStraight(jogo)) return 0.8;
        if (temFlush(jogo)) return 0.9;

        return 0.1; // mão muito fraca
    }

    @Override
    public int getPrimeiraAposta(Carta[] jogo) {
        try {
            Thread.sleep(this.tempoEspera);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Avaliar a força da mão
        double forca = avaliarForcaDaMao(jogo);

        // Basear a aposta na força da mão
        int aposta = (int) (forca * 100); // Exemplo: quanto maior a força, maior a aposta
        return Math.max(aposta, 10); // Aposta mínima de 10
    }

    @Override
    public boolean getSegundaAposta(int apostaMaisAlta) {
        try {
            Thread.sleep(this.tempoEspera);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Lógica de decisão para continuar apostando ou não
        // Exemplo: se a aposta mais alta for muito alta e a força da mão for baixa, desista
        return apostaMaisAlta < 50; // Exemplo simplificado
    }

    // Exemplos de métodos para avaliar mãos (implementação seria mais detalhada)
    private boolean temPar(Carta[] jogo) {
        // Lógica para determinar se há um par
        return false; // Exemplo placeholder
    }

    private boolean temTrinca(Carta[] jogo) {
        // Lógica para determinar se há uma trinca
        return false; // Exemplo placeholder
    }

    private boolean temStraight(Carta[] jogo) {
        // Lógica para determinar se há um straight
        return false; // Exemplo placeholder
    }

    private boolean temFlush(Carta[] jogo) {
        // Lógica para determinar se há um flush
        return false; // Exemplo placeholder
    }
}
