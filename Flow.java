import java.util.Scanner;

public class Flow {
  private boolean opcaoErrada = false;
  private String mensagemErro = "";
  private Cache cache;

  public Flow() {
    cache = new Cache();
  }

  private void printMenu() {
    this.clearScreen();
    System.out.println("-- Menu");
    System.out.println("1) Ler endereço");
    System.out.println("2) Escrever endereço");
    System.out.println("3) Estatíticas");
    System.out.println("4) Sair");
    if (mensagemErro.length() > 0) {
      System.out.println("Antenção -> " + mensagemErro);
      mensagemErro = "";
    }
    System.out.print("Escolha uma opção: ");
  }

  private void clearScreen() {
    System.out.print("\033\143");
  }

  private Binario obtemBinario(String input) {
    Binario endereco = null;

    try {
      endereco = new Binario(input);
    } catch (Exception e) {
      mensagemErro = e.getMessage() + "!";
    }

    return endereco;
  }

  private void lerEndereco() {
    Scanner input = new Scanner(System.in);

    System.out.print("\nInforme o endereço a ser lido: ");
    String candidatoBinario = input.nextLine();
    Binario endereco = obtemBinario(candidatoBinario);

    if (endereco != null){
      RetornoAcesso retorno = cache.acessaEndereco(endereco);
      
      if(retorno.hit) 
        System.out.println("\nACERTO!\nDado já estava armazenado na cache");
      else
        System.out.println("\nFALHA!\nDado não estava armazenado na cache");

      System.out.println("O valor lido no endereço "  + endereco.endereco() + " é: " + retorno.valor);
      
      freeze();
    }
  }

  private void escreverEndereco() {
    Scanner input = new Scanner(System.in);
    int valor = 0;

    System.out.print("\nInforme o endereço a ser escrito: ");
    String candidatoBinario = input.nextLine();
    Binario endereco = obtemBinario(candidatoBinario);

    if (endereco != null) {
      System.out.print("\nInforme o valor a ser armazenado: ");
      valor = input.nextInt();
      
      if(valor > 255){
        System.out.println("\nO valor de escrita não pode ser maior que 255.");
        freeze();
        return;
      }

      cache.escreveEndereco(endereco, valor);
      System.out.println("\nO endereço "  + endereco.endereco() + " foi escrito com sucesso.");
      freeze();
    }
  }

  public void freeze(){
    System.out.println("Pressione enter para continuar");
    Scanner freeze = new Scanner(System.in);
    freeze.nextLine();
  }

  public void run() {
    Scanner input = new Scanner(System.in);
    int opcao = 0;

    do {
      printMenu();
      opcao = input.nextInt();

      switch (opcao) {
      case 1:
        lerEndereco();
        break;
      case 2:
        escreverEndereco();
        break;
      case 3:
        cache.estatistica();
        freeze();
        break;
      case 4:
        System.out.println("Meu trabalho por aqui foi feito");
        input.close();
        break;
      default:
        mensagemErro = "Opção inválida!";
      }
    } while (opcao != 4);
  }
}