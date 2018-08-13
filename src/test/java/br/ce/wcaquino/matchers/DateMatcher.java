package br.ce.wcaquino.matchers;

import br.ce.wcaquino.utils.DataUtils;
import java.util.Date;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 *
 * @author R�mulo G�elzer Portolann
 */
public class DateMatcher extends TypeSafeMatcher<Date>{

    private final Date data1;
    
    public DateMatcher(Date data1) {
        this.data1 = data1;
    }

    @Override
    protected boolean matchesSafely(Date data2) {
        return DataUtils.isMesmaData(data1, data2);
    }

    public void describeTo(Description description) {
        description.appendText(this.data1.toString());
    }

}
