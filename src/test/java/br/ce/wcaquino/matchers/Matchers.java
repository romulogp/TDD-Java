package br.ce.wcaquino.matchers;

import java.util.Calendar;

/**
 *
 * @author R�mulo G�elzer Portolann
 */
public class Matchers {

  public static DiaSemanaMatcher caiEm(Integer diaSemana) {
    return new DiaSemanaMatcher(diaSemana);
  }

  public static DiaSemanaMatcher caiNumaSegunda() {
    return caiEm(Calendar.MONDAY);
  }

  public static DateMatcher ehHoje() {
    return ehHojeComDiferencaDeDias(0);
  }

  public static DateMatcher ehHojeComDiferencaDeDias(Integer dias) {
    Calendar c = Calendar.getInstance();
    c.add(Calendar.DAY_OF_MONTH, dias);
    return new DateMatcher(c.getTime());
  }

}
