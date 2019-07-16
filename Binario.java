public class Binario {
  private String numero = "";
  private int decimal = 0;
  private int tamanho = 0;

  public Binario(String input) throws Exception {
    tamanho = Config.bitsEndereco();
    constructorString(input);
  }

  public Binario(String input, int _tamanho) throws Exception {
    this.tamanho = _tamanho;
    constructorString(input);
  }

  public Binario(int input) {
    this.tamanho = Config.bitsEndereco();
    constructorInt(input);
  }

  public Binario(int input, int _tamanho) {
    this.tamanho = _tamanho;
    constructorInt(input);
  }

  private void constructorInt(int input) {
    this.numero = Integer.toString(input, 2);
    this.decimal = input;
  }

  private void constructorString(String input) throws Exception {
    this.validaEntrada(input);
    this.numero = input;
    this.decimal = Integer.parseInt(input, 2);
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

  public String endereco() {
    return numero.substring(0, 4) + "." + numero.substring(4, 5) + "." + numero.substring(5, 7);
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