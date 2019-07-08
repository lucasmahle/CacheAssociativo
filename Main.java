public class Main {

  public static void main(String args[]) {
    try {
      Flow flow = new Flow();

      flow.run();
    } catch (Exception e) {
      System.out.println("\n\nAlguma coisa aconteceu de errado. Programa encerrado");
      e.printStackTrace();
    }
  }
}