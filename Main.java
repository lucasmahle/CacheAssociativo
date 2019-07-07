import java.util.Scanner;

public class Main {

  public static void main(String args[]) {
    try {
      Scanner input = new Scanner(System.in);
      int opcao = 0;

      do {
        System.out.print("\033\143");
        System.out.println("-- Menu");
        System.out.println("1) Ler endereço");
        System.out.println("2) Escrever endereço");
        System.out.println("3) Estatíticas");
        System.out.println("4) Sair");
        System.out.print("Escolha uma opção: ");

        opcao = input.nextInt();

        switch (opcao) {
        case 1:
          break;
        case 2:
          break;
        case 3:
          break;
        case 4:
          System.out.println("Meu trabalho por aqui foi feito");
          input.close();
          break;
        default:
          System.out.println("Opção inválida!");
        }
      } while (opcao != 4);
    } catch (Exception e) {
    }
  }
}