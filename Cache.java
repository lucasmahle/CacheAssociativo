
import java.util.Random;

public class Cache extends DumpCache {
  private Conjunto[] conjunto = new Conjunto[2];
  private Bloco[] bloco = new Bloco[32];

  private int hitsLeitura = 0;
  private int missesLeitura = 0;
  private int hitsEscrita = 0;
  private int missesEscrita = 0;

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
    int max = 255;
    Random r = new Random();
    return r.nextInt((max - min) + 1) + min;
  }

  private void deslocamentoInternoTopo(ControleLRU substituicao, int indiceConjunto) {
    Conjunto conjuntoAlvo = conjunto[indiceConjunto];

    int lruLinhaDeslocada = conjuntoAlvo.linha[substituicao.indexTopo].LRU;

    for (int i = 0; i < 4; i++) {
      Linha linhaTemp = conjuntoAlvo.linha[i];

      if (substituicao.indexTopo == i)
        conjuntoAlvo.linha[i].LRU = 0;
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

      if (conjuntoAlvo.linha[i].LRU == 3)
        indiceLRUSubstituicao = i;
    }

    return indiceLRUSubstituicao;
  }

  public RetornoAcesso escreveEndereco(Binario endereco, int valor) {
    int tag = endereco.getTag();
    int indiceConjunto = endereco.getConjunto();
    int indiceDeslocamento = endereco.getDeslocamento();

    RetornoAcesso retorno = new RetornoAcesso();
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
        this.hitsEscrita++;
        retorno.hit = true;
        substituicao.indexTopo = i;
        linhaTemp.deslocamento[indiceDeslocamento] = valor;
        break;
      }
    }

    if (!dadoCacheado) {
      this.missesEscrita++;
      int indiceSubstituicao = indiceSubstituicao(indiceConjunto);
      Linha linhaAlvo = conjuntoAlvo.linha[indiceSubstituicao];
      
      this.carregaDadosDaMP(indiceConjunto, indiceSubstituicao, tag);

      retorno.hit = false;
      substituicao.indexTopo = indiceSubstituicao;
      linhaAlvo.tag = tag;
      linhaAlvo.bitValid = 1;
    }

    deslocamentoInternoTopo(substituicao, indiceConjunto);

    return retorno;
  }

  public RetornoAcesso acessaEndereco(Binario endereco) {
    int tag = endereco.getTag();
    int indiceConjunto = endereco.getConjunto();
    int indiceDeslocamento = endereco.getDeslocamento();

    RetornoAcesso retorno = new RetornoAcesso();
    ControleLRU substituicao = new ControleLRU();
    Conjunto conjuntoAlvo = conjunto[indiceConjunto];
    boolean dadoCacheado = false;

    // Busca na linha do conjunto
    for (int i = 0; i < 4; i++) {
      Linha linhaTemp = conjuntoAlvo.linha[i];
      if (linhaTemp.tag == tag && linhaTemp.bitValid == 1) {
        this.hitsLeitura++;
        dadoCacheado = true;
        retorno.valor = linhaTemp.deslocamento[indiceDeslocamento];
        retorno.hit = true;
        substituicao.indexTopo = i;
        break;
      }
    }

    if (!dadoCacheado) {
      this.missesLeitura++;

      int indiceSubstituicao = indiceSubstituicao(indiceConjunto);
      Linha linhaAlvo = conjuntoAlvo.linha[indiceSubstituicao];

      this.carregaDadosDaMP(indiceConjunto, indiceSubstituicao, tag);

      retorno.valor = linhaAlvo.deslocamento[indiceDeslocamento];
      retorno.hit = false;
      substituicao.indexTopo = indiceSubstituicao;
      linhaAlvo.tag = tag;
      linhaAlvo.bitValid = 1;
    }

    deslocamentoInternoTopo(substituicao, indiceConjunto);

    return retorno;
  }

  private void carregaDadosDaMP(int indiceConjunto, int indiceQuadro, int tag){
    Conjunto conjuntoAlvo = conjunto[indiceConjunto];
    Linha linhaAlvo = conjuntoAlvo.linha[indiceQuadro];
    String tagStr = Integer.toString(tag, 2);

    // Adiciona o bit do conjunto
    tagStr += indiceConjunto;

    // Converte para valor de acesso
    int blocoMP = Integer.parseInt(tagStr, 2);

    for (int i = 0; i < 4; i++) {
      int data = bloco[blocoMP].dado[i];
      linhaAlvo.deslocamento[i] = data;
    }
  }

  public void estatistica() {
    this.dumpCache(bloco, conjunto, hitsLeitura, missesLeitura, hitsEscrita, missesEscrita);
  }
}