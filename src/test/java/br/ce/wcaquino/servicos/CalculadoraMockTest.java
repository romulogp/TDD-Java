package br.ce.wcaquino.servicos;

import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author Rômulo Göelzer Portolann
 */
public class CalculadoraMockTest {

  @Test
  public void teste() {
    Calculadora calc = Mockito.mock(Calculadora.class);
    Mockito.when(calc.somar(1, 2)).thenReturn(5);
    Mockito.when(calc.somar(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);

    System.out.println(calc.somar(1, 2));
  }

}
