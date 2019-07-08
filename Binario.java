public class Binario {
  private String numero = "";
  private int decimal = 0;
  private int tamanho = 7;

  public Binario(String input) throws Exception {
    this.validaEntrada(input);
    this.numero = input;
    this.decimal = Integer.parseInt(input, 2);
  }

  public Binario(int input) {
    this.numero = Integer.toString(input, 2);
    this.decimal = input;
  }

  public int getTag() {
    return this.getRange(0, 4);
  }

  public int getConjunto() {
    return this.getRange(4, 5);
  }

  public int getDeslocamento() {
    return this.getRange(5, 7);
  }

  public String toDec() {
    return Integer.toString(this.decimal);
  }

  public String toBin() {
    return this.toBin(tamanho);
  }

  public String toBin(int tamanhoBin) {
    String bin = Integer.toString(this.decimal, 2);
    int paddingZero = tamanhoBin - bin.length();
    for (int i = 0; i < paddingZero; i++) {
      bin = "0" + bin;
    }

    return bin;
  }

  public String toHex() {
    return Integer.toString(this.decimal, 16);
  }

  private void validaEntrada(String input) throws Exception {
    if (input.length() == 0)
      throw new Exception("Nenhum valor informado");

    if (input.length() != tamanho)
      throw new Exception("O valor informador deve ser composto por " + tamanho + " bits");
  }

  private int getRange(int inicio, int fim) {
    String pedaco = this.numero.substring(inicio, fim);
    return Integer.parseInt(pedaco, 2);
  }
}