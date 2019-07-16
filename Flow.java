import java.util.Scanner;

public class Flow {
  /**
   * Flow controla o fluxo de opções selecionadas. Não é aqui que fica a regra de
   * negócio para operações na cache, apenas menu e retorno de mensagem ao usuário
   */

  /**
   * mensagemErro controla visualização para o usuário. Já que toda renderização
   * do menu é limpo a tela, essa variável controla alguma mensagem de erro
   */
  private String mensagemErro = "";

  /**
   * Armazena a instância da Cache
   */
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

    // Exibe o erro e depois limpa a variável que contrala a mensagem
    if (mensagemErro.length() > 0) {
      System.out.println("Atenção -> " + mensagemErro);
      mensagemErro = "";
    }

    System.out.print("Escolha uma opção: ");
  }

  /**
   * Limpa a tela
   */
  private void clearScreen() {
    System.out.print("\033\143");
  }

  /**
   * Retorna uma instância da classe que controla endereços. Caso aconteça algum
   * erro, a mensagem é armazenada na variável que controla os retoros aos
   * usuários e retorna null
   */
  private Binario obtemBinario(String input) {
    Binario endereco = null;

    try {
      endereco = new Binario(input);
    } catch (Exception e) {
      mensagemErro = e.getMessage() + "!";
    }

    return endereco;
  }

  /**
   * Interrompe o input do terminal afim de congelar a informação na tela. Isso
   * foi necessário pois a tela é limpa a cada renderização do menu
   */
  public void freeze() {
    System.out.println("Pressione enter para continuar");
    Scanner freeze = new Scanner(System.in);
    freeze.nextLine();
  }

  /**
   * Método para ler o endereço informado pelo usuário e efetua a leitura na
   * memória cache. Renderiza as mensagens conforme retorno da Cache
   */
  private void lerEndereco() {
    Scanner input = new Scanner(System.in);

    System.out.print("\nInforme o endereço a ser lido: ");
    String candidatoBinario = input.nextLine();
    Binario endereco = obtemBinario(candidatoBinario);

    if (endereco != null) {
      RetornoAcesso retorno = cache.acessaEndereco(endereco);

      if (retorno.hit)
        System.out.println("\nACERTO!\nDado já estava armazenado na cache");
      else
        System.out.println("\nFALHA!\nDado não estava armazenado na cache");

      System.out.println("O valor lido no endereço " + endereco.endereco() + " é: " + retorno.valor);

      freeze();
    }
  }

  /**
   * Método para escrever na memória cache em um endereço informado pelo usuário.
   */
  private void escreverEndereco() {
    Scanner input = new Scanner(System.in);
    int valor = 0;

    System.out.print("\nInforme o endereço a ser escrito: ");
    String candidatoBinario = input.nextLine();
    Binario endereco = obtemBinario(candidatoBinario);

    if (endereco != null) {
      System.out.print("\nInforme o valor a ser armazenado: ");
      valor = input.nextInt();

      if (valor > Config.valorMaximo()) {
        System.out.println("\nO valor de escrita não pode ser maior que " + Config.valorMaximo() + ".");
        freeze();
        return;
      }

      RetornoAcesso retorno = cache.escreveEndereco(endereco, valor);

      if (retorno.hit)
        System.out.println("\nACERTO!\nDado escrito já estava armazenado na cache");
      else
        System.out.println("\nFALHA!\nDado escrito não estava armazenado na cache");

      System.out.println("\nO endereço " + endereco.endereco() + " foi escrito com sucesso.");
      freeze();
    }
  }

  /**
   * Método principal que controla a opção selecionada pelo usuário
   */
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