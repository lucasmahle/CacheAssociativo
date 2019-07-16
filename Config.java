import java.lang.Math; 

public class Config {
  public static final int CELULAS_MP = 128;
  public static final int TAMANHO_BLOCO = 4;
  public static final int NUMERO_LINHAS = 8;
  public static final int BITS_CELULA = 8;
  public static final int LINHAS_POR_CONJUNTO = 2;

  public static final int valorMaximo(){
    return Math.pow(2, BITS_CELULA) - 1;
  }

  public static final int bitsEndereco(){
    String binValue = Integer.toString(CELULAS_MP - 1, 2);
    return binValue.length();
  }
}