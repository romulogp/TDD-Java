package br.ce.wcaquino.matchers;

import br.ce.wcaquino.utils.DataUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 *
 * @author Rômulo Göelzer Portolann
 */
public class DataDiferencaDiasMatcher extends TypeSafeMatcher<Date> {

  private Integer qtdDias;

  DataDiferencaDiasMatcher(Integer qtdDias) {
    this.qtdDias = qtdDias;
  }

  public void describeTo(Description desc) {
    Date dataEsperada = DataUtils.obterDataComDiferencaDias(qtdDias);
    DateFormat format = new SimpleDateFormat("dd/MM/YYYY");
    desc.appendText(format.format(dataEsperada));
  }

  @Override
  protected boolean matchesSafely(Date t) {
    return DataUtils.isMesmaData(t, DataUtils.obterDataComDiferencaDias(qtdDias));
  }

}
