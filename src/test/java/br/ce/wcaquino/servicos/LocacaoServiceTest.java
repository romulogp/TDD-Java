package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import static br.ce.wcaquino.matchers.Matchers.caiEm;
import static br.ce.wcaquino.matchers.Matchers.caiNumaSegunda;
import static br.ce.wcaquino.matchers.Matchers.ehHoje;
import static br.ce.wcaquino.matchers.Matchers.ehHojeComDiferencaDeDias;
import br.ce.wcaquino.utils.DataUtils;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

public class LocacaoServiceTest {

    private static int testCount = 0;
    private LocacaoService service;

    @Before
    public void setup() {
        service = new LocacaoService();
    }

    @Before
    public void increment() {
        ++testCount;
    }

    @AfterClass
    public static void printCounter() {
        System.out.println(testCount);
    }

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void deveAlugarFilme() throws Exception {

        // Realiza o teste dinamicamente.
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 1, 5.0);

        //acao
        Locacao locacao = service.alugarFilme(usuario, Arrays.asList(filme));

        //verificacao
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(locacao.getDataLocacao(), ehHoje());
        error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDeDias(1));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 2", 1, 4.0);
        Filme filme2 = new Filme("Filme 2", 0, 3.0);

        //acao
        service.alugarFilme(usuario, Arrays.asList(filme, filme2));
    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
        //cenario
        Filme filme = new Filme("Filme 2", 1, 4.0);

        //acao
        try {
            service.alugarFilme(null, Arrays.asList(filme));
            Assert.fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario vazio"));
        }
    }

    @Test
    public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //acao
        service.alugarFilme(usuario, null);
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {

        // Realiza o teste dinamicamente, ou seja, se o teste deve ou não ser ignorado
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        // cenario
        Usuario usuario = new Usuario("Usuario1");
        List<Filme> filme = Arrays.asList(new Filme("Filme1", 2, 4.0));

        // acao
        Locacao locacao = service.alugarFilme(usuario, filme);

        // verificacao
        assertThat(locacao.getDataRetorno(), caiEm(Calendar.MONDAY));
        assertThat(locacao.getDataRetorno(), caiNumaSegunda());
    }

}
