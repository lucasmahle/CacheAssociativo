public class Conjunto {
  public Linha[] linha = new Linha[4];
  public boolean espacoLivre = true;
  // public int espacoLivre = 0;
  // public int indiceMaximo = 3; // 4 - 1

  public Conjunto() {
    for (int i = 0; i < 4; i++) {
      linha[i] = new Linha();
    }
  }
}