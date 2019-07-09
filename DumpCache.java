public class DumpCache {
  private Bloco[] bloco = null;
  private Conjunto[] conjunto = null;
  private int hits = 0;
  private int misses = 0;
  private int acessos = 0;

  protected void dumpCache(Bloco[] _bloco, Conjunto[] _conjunto, int _hits, int _misses, int _acessos) {
    this.bloco = _bloco;
    this.conjunto = _conjunto;
    this.hits = _hits;
    this.misses = _misses;
    this.acessos = _acessos;

    this.printMemoriaPrincipal();

    this.printDataCache();

    System.out.println("Hits: " + hits);
    System.out.println("Misses: " + misses);
  }

  private String ft(int valor, int quantidade) {
    return String.format("%0" + quantidade + "d", valor);
  }

  private void printDataCache(){
    for (int indexConjunto = 0; indexConjunto < 2; indexConjunto++) {
      System.out.println("\nConjunto: " + indexConjunto);
      System.out.println("| LRU | V |  Tag | Dest |  D1(00) |  D2(01) |  D3(10) |  D4(11) |");

      Conjunto conjuntoTemp = conjunto[indexConjunto];

      for (int indexLinha = 0; indexLinha < 4; indexLinha++) {
        Linha linhaTemp = conjuntoTemp.linha[indexLinha];
        Binario tagBin = new Binario(linhaTemp.tag);
        System.out.print("|  " + (linhaTemp.bitValid == 1 ? linhaTemp.LRU : 0) + "  | " + linhaTemp.bitValid + " | "
            + tagBin.toBin(4) + " |   " + indexConjunto + "  | ");

        for (int indexDeslocamento = 0; indexDeslocamento < 4; indexDeslocamento++) {
          Binario deslocamentoBin = new Binario(linhaTemp.deslocamento[indexDeslocamento]);
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

    System.out.println("| Bloco |  Célula 1 |  Célula 2 |  Célula 3 |  Célula 4 |");
    for (int indexBloco = 0; indexBloco < 32; indexBloco++) {
      System.out.print("|   " + ft(indexBloco, 2) + "  ");

      for (int indexCelula = 0; indexCelula < 4; indexCelula++)
        System.out.print("|    " + ft(bloco[indexBloco].dado[indexCelula], 3) + "    ");

      System.out.println("|");
    }
  }
}