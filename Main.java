public class Main {

  /**
   * Esse arquivo tem unicamente a função de iniciar o programa pelo método main.
   * Toda lógica do fluxo fica no arquivo Flow.java
   */
  public static void main(String args[]) {
    try {
      // Instancia o controle de fluxo
      Flow flow = new Flow();

      // Inicia o programa
      flow.run();
    } catch (Exception e) {
      System.out.println("\n\nAlguma coisa aconteceu de errado. Programa encerrado.");
      e.printStackTrace();
    }
  }
}