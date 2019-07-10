public class DumpCache {
  private Bloco[] bloco = null;
  private Conjunto[] conjunto = null;

  protected void dumpCache(Bloco[] _bloco, Conjunto[] _conjunto, int hitsLeitura, int missesLeitura, int hitsEscrita, int missesEscrita) {
    this.bloco = _bloco;
    this.conjunto = _conjunto;

    this.printMemoriaPrincipal();

    this.printDataCache();

    this.totalizadores("Leitura", hitsLeitura, missesLeitura);
    this.totalizadores("Escrita", hitsEscrita, missesEscrita);
  }

  private String ft(int valor, int quantidade) {
    return String.format("%0" + quantidade + "d", valor);
  }

  private void totalizadores(String titulo, int hits, int misses) {
    int total = hits + misses;
    double hitsPerc = hits == 0 ? 0 : (hits * 100) / total;
    double missesPerc = misses == 0 ? 0 : (misses * 100) / total;

    System.out.println("\nTotalizador " + titulo);
    System.out.println("Hits:   " + hits + " -> " + String.format("%.2f", hitsPerc) + "%");
    System.out.println("Misses: " + misses + " -> " + String.format("%.2f", missesPerc) + "%");
    System.out.println("Total:  " + total);
  }

  private void printDataCache(){
    for (int indexConjunto = 0; indexConjunto < 2; indexConjunto++) {
      System.out.println("\nConjunto: " + indexConjunto);
      System.out.println("| LRU | V |  Tag | Dest |  D0(00)  |  D1(01)  |  D2(10)  |  D3(11)  |");

      Conjunto conjuntoTemp = conjunto[indexConjunto];

      for (int indexLinha = 0; indexLinha < 4; indexLinha++) {
        Linha linhaTemp = conjuntoTemp.linha[indexLinha];
        Binario tagBin = new Binario(linhaTemp.tag);
        Binario lruBin = new Binario(linhaTemp.LRU);
        System.out.print("|  " + (linhaTemp.bitValid == 1 ? lruBin.toBin(2) : "00") + " | " + linhaTemp.bitValid + " | "
            + tagBin.toBin(4) + " |   " + indexConjunto + "  | ");

        for (int indexDeslocamento = 0; indexDeslocamento < 4; indexDeslocamento++) {
          Binario deslocamentoBin = new Binario(linhaTemp.deslocamento[indexDeslocamento], 8);
          System.out.print(deslocamentoBin.toBin());
          System.out.print(" | ");
        }
        System.out.println("");
      }
      System.out.println("");
    }
  }

  private void printMemoriaPrincipal() {
    System.out.println("\nMemória Principal");

    System.out.println("| Bloco |  Célula 0 |  Célula 1 |  Célula 2 |  Célula 3 |");
    for (int indexBloco = 0; indexBloco < 32; indexBloco++) {
      System.out.print("|   " + ft(indexBloco, 2) + "  ");

      for (int indexCelula = 0; indexCelula < 4; indexCelula++)
        System.out.print("|    " + ft(bloco[indexBloco].dado[indexCelula], 3) + "    ");

      System.out.println("|");
    }
  }
}