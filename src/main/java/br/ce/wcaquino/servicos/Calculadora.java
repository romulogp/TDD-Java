package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

/**
 *
 * @author Rômulo Göelzer Portolann
 */
public class Calculadora {

  int somar(int a, int b) {
    System.out.println("Somando a + b");
    return a + b;
  }

  int subtrair(int a, int b) {
    return a - b;
  }

  double dividir(int a, int b) throws NaoPodeDividirPorZeroException {
    if (b == 0) {
      throw new NaoPodeDividirPorZeroException();
    }
    return a / b;
  }
  
  public void imprime() {
    System.out.println("Passei aqui");
  }

}
