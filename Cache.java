
import java.util.Random;

public class Cache {
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
    int max = 128;
    Random r = new Random();
    return r.nextInt((max - min) + 1) + min;
  }

  private void deslocaIndiceParaTopo(int indice, int indiceConjunto) {
    Conjunto conjuntoAlvo = conjunto[indiceConjunto];

    Linha linhaDeslocada = conjuntoAlvo.linha[indice];
    for (int i = 3; i >= 1; i--) {
      conjuntoAlvo.linha[i] = conjuntoAlvo.linha[i - 1];
    }

    conjuntoAlvo.linha[0] = linhaDeslocada;
  }

  private RetornoSubstituicao obterIndiceSubstituicao(int indiceConjunto) {
    Conjunto conjuntoAlvo = conjunto[indiceConjunto];
    RetornoSubstituicao retorno = new RetornoSubstituicao();

    retorno.index = conjuntoAlvo.indiceMaximo;
    if (conjuntoAlvo.espacoLivre < conjuntoAlvo.indiceMaximo) {
      retorno.index = conjuntoAlvo.espacoLivre;
      retorno.substituir = false;
      conjuntoAlvo.espacoLivre++;
    }

    return retorno;
  }

  public void escreveEndereco(Binario endereco, int valor) {
    int tag = endereco.getTag();
    int indiceConjunto = endereco.getConjunto();
    int indiceDeslocamento = endereco.getDeslocamento();

    RetornoSubstituicao substituicao = null;
    Conjunto conjuntoAlvo = conjunto[indiceConjunto];
    boolean dadoCacheado = false;

    // Altera MP
    bloco[tag].dado[indiceDeslocamento] = valor;

    // Altera Cache
    for (int i = 0; i < 4; i++) {
      Linha linhaTemp = conjuntoAlvo.linha[i];
      if (linhaTemp.tag == tag && linhaTemp.bitValid == 1) {
        dadoCacheado = true;
        linhaTemp.deslocamento[indiceDeslocamento] = valor;
        break;
      }
    }

    if (!dadoCacheado) {
      substituicao = obterIndiceSubstituicao(indiceConjunto);

      Linha linhaAlvo = conjuntoAlvo.linha[substituicao.index];

      // Carrega dado da MP
      for (int i = 0; i < 4; i++) {
        int data = bloco[tag].dado[i];
        linhaAlvo.deslocamento[i] = data;
      }

      linhaAlvo.tag = tag;
      linhaAlvo.bitValid = 1;
    }

    if (substituicao.substituir)
      deslocaIndiceParaTopo(substituicao.index, indiceConjunto);
  }

  public void acessaEndereco(Binario endereco) {
    int tag = endereco.getTag();
    int indiceConjunto = endereco.getConjunto();

    RetornoSubstituicao substituicao = new RetornoSubstituicao();
    Conjunto conjuntoAlvo = conjunto[indiceConjunto];
    boolean dadoCacheado = false;

    // Busca na linha do conjunto
    for (int i = 0; i < 4; i++) {
      Linha linhaTemp = conjuntoAlvo.linha[i];
      if (linhaTemp.tag == tag && linhaTemp.bitValid == 1) {
        hits++;
        dadoCacheado = true;
        substituicao.index = i;
        substituicao.substituir = conjuntoAlvo.espacoLivre >= conjuntoAlvo.indiceMaximo;
        break;
      }
    }

    if (!dadoCacheado) {
      misses++;

      substituicao = obterIndiceSubstituicao(indiceConjunto);
      Linha linhaAlvo = conjuntoAlvo.linha[substituicao.index];

      // Carrega dado da MP
      for (int i = 0; i < 4; i++) {
        int data = bloco[tag].dado[i];
        linhaAlvo.deslocamento[i] = data;
      }

      linhaAlvo.tag = tag;
      linhaAlvo.bitValid = 1;
    }

    if (substituicao.substituir)
      deslocaIndiceParaTopo(substituicao.index, indiceConjunto);
  }

  public void estatistica() {
    System.out.println("\nMemória Principal");
    System.out.println("| Bloco |  Célula 1 |  Célula 2 |  Célula 3 |  Célula 4 |");
    for (int indexBloco = 0; indexBloco < 32; indexBloco++) {
      System.out.print("|   " + String.format("%02d", indexBloco) + "  ");

      for (int indexCelula = 0; indexCelula < 4; indexCelula++)
        System.out.print("|    " + String.format("%03d", bloco[indexBloco].dado[indexCelula]) + "    ");

      System.out.println("|");
    }

    for (int indexConjunto = 0; indexConjunto < 2; indexConjunto++) {
      System.out.println("\nConjunto: " + indexConjunto);
      System.out.println("| V |  Tag |Dest|  D1(00) |  D2(01) |  D3(10) |  D4(11) |");

      Conjunto conjuntoTemp = conjunto[indexConjunto];

      for (int indexLinha = 0; indexLinha < 4; indexLinha++) {
        Linha linhaTemp = conjuntoTemp.linha[indexLinha];
        Binario tagBin = new Binario(linhaTemp.tag);
        System.out.print("| " + linhaTemp.bitValid + " | " + tagBin.toBin(4) + " | " + indexConjunto + "  | ");

        for (int indexDeslocamento = 0; indexDeslocamento < 4; indexDeslocamento++) {
          Binario deslocamentoBin = new Binario(linhaTemp.deslocamento[indexDeslocamento]);
          System.out.print(deslocamentoBin.toBin());
          System.out.print(" | ");
        }
        System.out.println("");
      }
      System.out.println("");
    }

    System.out.println("Hits: " + hits);
    System.out.println("Misses: " + misses);
  }
}