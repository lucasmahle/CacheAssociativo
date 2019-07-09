
import java.util.Random;

public class Cache extends DumpCache {
  private Conjunto[] conjunto = new Conjunto[2];
  private Bloco[] bloco = new Bloco[32];

  private int hits = 0;
  private int misses = 0;

  public Cache() {
    for (int i = 0; i < 2; i++) {
      conjunto[i] = new Conjunto();
    }
    for (int i = 0; i < 32; i++) {
      bloco[i] = new Bloco();

      for (int j = 0; j < 4; j++) {
        bloco[i].dado[j] = randomValue();
      }
    }
  }

  private int randomValue() {
    int min = 0;
    int max = 127;
    Random r = new Random();
    return r.nextInt((max - min) + 1) + min;
  }

  private void deslocamentoInternoTopo(ControleLRU substituicao, int indiceConjunto) {
    Conjunto conjuntoAlvo = conjunto[indiceConjunto];

    int lruLinhaDeslocada = conjuntoAlvo.linha[substituicao.indexTopo].LRU;

    for (int i = 0; i < 4; i++) {
      Linha linhaTemp = conjuntoAlvo.linha[i];

      if (substituicao.indexTopo == i)
        conjuntoAlvo.linha[i].LRU = 1;
      else if (linhaTemp.bitValid == 1 && (linhaTemp.LRU <= lruLinhaDeslocada || lruLinhaDeslocada == 0))
        conjuntoAlvo.linha[i].LRU++;
    }
  }

  private int indiceSubstituicao(int indiceConjunto) {
    Conjunto conjuntoAlvo = conjunto[indiceConjunto];
    int indiceLRUSubstituicao = 0;

    for (int i = 0; i < 4; i++) {
      if (conjuntoAlvo.linha[i].bitValid == 0) {
        return i;
      }

      if (conjuntoAlvo.linha[i].LRU == 4)
        indiceLRUSubstituicao = i;
    }

    return indiceLRUSubstituicao;
  }

  // private ControleLRU APAGARobterIndiceSubstituicao(int indiceConjunto) {
  // Conjunto conjuntoAlvo = conjunto[indiceConjunto];
  // ControleLRU retorno = new ControleLRU();

  // // O próximo índice a ser escrito
  // // Sempre é o último
  // retorno.indexTopo = conjuntoAlvo.indiceMaximo;

  // // Exceto quando há espaços livres
  // if (conjuntoAlvo.espacoLivre <= conjuntoAlvo.indiceMaximo) {
  // retorno.indexTopo = conjuntoAlvo.espacoLivre;
  // retorno.substituir = false;
  // conjuntoAlvo.espacoLivre++;
  // }

  // return retorno;
  // }

  public void escreveEndereco(Binario endereco, int valor) {
    int tag = endereco.getTag();
    int indiceConjunto = endereco.getConjunto();
    int indiceDeslocamento = endereco.getDeslocamento();

    ControleLRU substituicao = new ControleLRU();
    Conjunto conjuntoAlvo = conjunto[indiceConjunto];
    boolean dadoCacheado = false;

    // Altera MP
    bloco[tag].dado[indiceDeslocamento] = valor;

    // Altera Cache
    for (int i = 0; i < 4; i++) {
      Linha linhaTemp = conjuntoAlvo.linha[i];
      if (linhaTemp.tag == tag && linhaTemp.bitValid == 1) {
        dadoCacheado = true;
        substituicao.indexTopo = i;
        linhaTemp.deslocamento[indiceDeslocamento] = valor;
        break;
      }
    }

    if (!dadoCacheado) {
      int indiceSubstituicao = indiceSubstituicao(indiceConjunto);
      Linha linhaAlvo = conjuntoAlvo.linha[indiceSubstituicao];

      // Carrega dado da MP
      for (int i = 0; i < 4; i++) {
        int data = bloco[tag].dado[i];
        linhaAlvo.deslocamento[i] = data;
      }

      substituicao.indexTopo = indiceSubstituicao;
      linhaAlvo.tag = tag;
      linhaAlvo.bitValid = 1;
    }

    deslocamentoInternoTopo(substituicao, indiceConjunto);
  }

  public int acessaEndereco(Binario endereco) {
    int tag = endereco.getTag();
    int indiceConjunto = endereco.getConjunto();
    int indiceDeslocamento = endereco.getDeslocamento();
    int valorLido = 0;

    ControleLRU substituicao = new ControleLRU();
    Conjunto conjuntoAlvo = conjunto[indiceConjunto];
    boolean dadoCacheado = false;

    // Busca na linha do conjunto
    for (int i = 0; i < 4; i++) {
      Linha linhaTemp = conjuntoAlvo.linha[i];
      if (linhaTemp.tag == tag && linhaTemp.bitValid == 1) {
        this.hits++;
        dadoCacheado = true;
        valorLido = linhaTemp.deslocamento[indiceDeslocamento];
        substituicao.indexTopo = i;
        break;
      }
    }

    if (!dadoCacheado) {
      this.misses++;

      int indiceSubstituicao = indiceSubstituicao(indiceConjunto);
      Linha linhaAlvo = conjuntoAlvo.linha[indiceSubstituicao];

      // Carrega dado da MP
      for (int i = 0; i < 4; i++) {
        int data = bloco[tag].dado[i];
        linhaAlvo.deslocamento[i] = data;
      }

      valorLido = linhaAlvo.deslocamento[indiceDeslocamento];
      substituicao.indexTopo = indiceSubstituicao;
      linhaAlvo.tag = tag;
      linhaAlvo.bitValid = 1;
    }

    deslocamentoInternoTopo(substituicao, indiceConjunto);

    return valorLido;
  }

  public void estatistica() {
    this.dumpCache(bloco, conjunto, hits, misses, 0);
  }
}