import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AgenteILT extends Avaliacao implements Agente {

    private Carta[] jogo;

    public AgenteILT() {
    }

    private int avaliarForcaDaMao(Carta[] jogo) {
        this.jogo = jogo;

        if (temRoyalFlush(jogo)) return 800;
        if (temStraightFlush(jogo)) return 700;
        if (temQuadra(jogo)) return 500;
        if (temFullHouse(jogo)) return 400;
        if (temFlush(jogo)) return 300;
        if (temSequencia(jogo)) return 200;
        if (temTrinca(jogo)) return 170;
        if (temDoisPares(jogo)) return 120;
        int temPar = temPar(jogo);
        if (temPar > 0) return temPar;
        return analiseCartaAlta(jogo);
    }

    @Override
    public int getPrimeiraAposta(Carta[] jogo) {
        int aposta = avaliarForcaDaMao(jogo);
        return Math.max(aposta, 10);
    }

    @Override
    public boolean getSegundaAposta(int apostaMaisAlta) {
        int minhaAposta = avaliarForcaDaMao(this.jogo);
        if (minhaAposta == 10) return false;
        return minhaAposta*2 > apostaMaisAlta;
    }

    // TODAS CARTAS > 9, SEQUENCIA E MESMO NAIPE
    private boolean temRoyalFlush(Carta[] jogo) {
//        testar Royal Flush
//        jogo = new Carta[] {
//                new Carta(10, 'O'),
//                new Carta(12, 'O'),
//                new Carta(13, 'O'),
//                new Carta(11, 'O'),
//                new Carta(14, 'O')
//        };
//        boolean flushDescricao = true;

        Avaliacao avaliacao = Avaliacao.avaliaJogo(jogo);
        boolean flushDescricao = avaliacao.descricao.contains("Seqüência de Naipe");
        if (flushDescricao && temSequenciaEspecifica(jogo, 10, 14)){
            System.out.println("-------------------------- ROYAL FLUSH --------------------------");
        }
        return flushDescricao;
    }

    // 5 CARTAS DO MESMO NAIPE E SEQUENCIA
    private boolean temStraightFlush(Carta[] jogo) {
        Avaliacao avaliacao = Avaliacao.avaliaJogo(jogo);
        return avaliacao.descricao.contains("Seqüência de Naipe");
    }

    // 4 CARTAS DO MESMO VALOR
    private boolean temQuadra(Carta[] jogo) {
        Avaliacao avaliacao = Avaliacao.avaliaJogo(jogo);
        return avaliacao.descricao.contains("Quadra de");
    }


    // TRINCA E UM PAR
    private boolean temFullHouse(Carta[] jogo) {
        Avaliacao avaliacao = Avaliacao.avaliaJogo(jogo);
        return avaliacao.descricao.contains("Dupla e Trinca com");
    }

    // 5 CARTAS DO MESMO NAIPE
    private boolean temFlush(Carta[] jogo) {
        Avaliacao avaliacao = Avaliacao.avaliaJogo(jogo);
        return avaliacao.descricao.contains("Mesmo Naipe");
    }


    // 5 CARTAS EM SEQUENCIA
    private boolean temSequencia(Carta[] jogo) {
        Avaliacao avaliacao = Avaliacao.avaliaJogo(jogo);
        return avaliacao.descricao.contains("Seqüência");
    }

    // 3 CARTAS DO MESMO VALOR
    private boolean temTrinca(Carta[] jogo) {
        Avaliacao avaliacao = Avaliacao.avaliaJogo(jogo);
        return avaliacao.descricao.contains("Trinca de ");
    }

    // 2 PARES SEPARADOS
    private boolean temDoisPares(Carta[] jogo) {
        Avaliacao avaliacao = Avaliacao.avaliaJogo(jogo);
        return avaliacao.descricao.contains("Dois pares com");
    }

    // DUAS CARTAS DO MESMO VALOR
    private int temPar(Carta[] jogo) {
        Avaliacao avaliacao = Avaliacao.avaliaJogo(jogo);
        if (avaliacao.descricao.contains("Par de")){
            Pattern pattern = Pattern.compile("Par de (\\d+)");
            Matcher matcher = pattern.matcher(avaliacao.descricao);
            if (matcher.find()) {
                int par = Integer.parseInt(matcher.group(1));
                switch (par) {
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        return 15;
                    case 6:
                        return 16;
                    case 7:
                        return 17;
                    case 8:
                        return 18;
                    case 9:
                        return 20;
                    case 10:
                        return 22;
                    case 11:
                        return 25;
                    case 12:
                        return 30;
                    case 13:
                        return 35;
                    case 14:
                        return 40;
                }
            }
        }
        return -1;
    }

    private int analiseCartaAlta(Carta[] jogo) {
        Avaliacao avaliacao = Avaliacao.avaliaJogo(jogo);
        if (avaliacao.descricao.contains("nada")){
            switch (avaliacao.pontos) {
                case 11:
                    return 11;
                case 12:
                    return 12;
                case 13:
                    return 13;
                case 14:
                    return 14;
            }
        }
        return 10;
    }

    private boolean temSequenciaEspecifica(Carta[] jogo, int inicio, int fim) {
        boolean temSequencia = false;
        for (int i = inicio; i < fim; i++) {
            temSequencia = false;
            for (Carta carta: jogo) {
                if (carta.valor == i){
                    temSequencia = true;
                    break;
                }
            }
            if (!temSequencia) return false;
        }
        return temSequencia;
    }

}
