package br.ce.wcaquino.matchers;

import br.ce.wcaquino.utils.DataUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 *
 * @author Rômulo Göelzer Portolann
 */
public class DiaSemanaMatcher extends TypeSafeMatcher<Date>{

    private final Integer diaSemana;
    
    public DiaSemanaMatcher(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }

    @Override
    protected boolean matchesSafely(Date data) {
        return DataUtils.verificarDiaSemana(data, diaSemana);
    }

    public void describeTo(Description description) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, diaSemana);
        String dataExtenso = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("pt", "BR"));
        description.appendText(dataExtenso);
    }

}
