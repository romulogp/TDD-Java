package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Rômulo Göelzer Portolann
 */
public class CalculadoraTest {

  private Calculadora calc;

  @Before
  public void setup() {
    calc = new Calculadora();
  }

  @Test
  public void deveSomarDoisValores() {
    // cenario
    int a = 5;
    int b = 10;

    // acao
    int result = calc.somar(a, b);

    // verificacao
    Assert.assertEquals(15, result);
  }

  @Test
  public void deveSubtrairDoisValores() {
    // cenario
    int a = 20;
    int b = 1;

    // acao
    int result = calc.subtrair(a, b);

    // verificacao
    Assert.assertEquals(19, result);
  }

  @Test
  public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
    // cenario
    int a = 10;
    int b = 2;

    // acao
    double result = calc.dividir(a, b);

    // verificacao
    Assert.assertEquals(5.0, result);
  }

  @Test(expected = NaoPodeDividirPorZeroException.class)
  public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
    // acao
    int a = 10;
    int b = 0;

    // acao
    calc.dividir(a, b);
  }

}
