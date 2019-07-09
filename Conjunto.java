public class Conjunto {
  public Linha[] linha = new Linha[4];

  public Conjunto() {
    for (int i = 0; i < 4; i++) {
      linha[i] = new Linha();
    }
  }
}