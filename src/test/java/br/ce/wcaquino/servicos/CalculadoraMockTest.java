package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 *
 * @author R�mulo G�elzer Portolann
 */
public class CalculadoraMockTest {

  @Test
  public void teste() {
    Calculadora calc = Mockito.mock(Calculadora.class);
    Mockito.when(calc.somar(1, 2)).thenReturn(5);
    Mockito.when(calc.somar(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);

    assertEquals(5, calc.somar(1, 1000000));
  }

  @Test
  public void teste2() {
    Calculadora calc = Mockito.mock(Calculadora.class);

    ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
    Mockito.when(calc.somar(argCapt.capture(), argCapt.capture())).thenReturn(5);

    assertEquals(5, calc.somar(1, 10000));
    System.out.println(argCapt.getAllValues());
  }

}
