package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

/**
 *
 * @author Rï¿½mulo Gï¿½elzer Portolann
 */
public class CalculadoraMockTest {

  @Mock
  private Calculadora calcMock;

  @Spy
  private Calculadora calcSpy;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

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

  @Test
  public void teste3() {
    Mockito.when(calcMock.somar(1, 2)).thenReturn(3);
    Mockito.when(calcMock.somar(1, 10)).thenCallRealMethod();
//    Mockito.when(calcSpy.somar(1, 2)).thenReturn(3);
    Mockito.doReturn(3).when(calcSpy).somar(1, 2);
    Mockito.doNothing().when(calcSpy).imprime();

    System.out.println("Mock: " + calcMock.somar(1, 3)); // Mock: quando não sabe o que fazer retorna o valor padrão.
    System.out.println("Spy: " + calcSpy.somar(1, 3));   // Spy:  quando não sabe o que fazer retorna a execução padrão.
    // Obs.: O Spy só atua em classes concretas. Portanto, não funcionará com interfaces.
    System.out.println("Mock chamando metodo. Retorno: " + calcSpy.somar(1, 10));

    System.out.println("Mock");
    calcMock.imprime(); // o comportamento padrão do MOCK é NÃO EXECUTAR
    System.out.println("Spy");
    calcSpy.imprime();  // o comportamento padrão do SPY é EXECUTAR
  }

}
