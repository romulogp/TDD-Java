package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
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
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
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
    public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
        // cenario
        Usuario usuario = new Usuario("Usuario1");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme1", 2, 4.0),
                new Filme("Filme2", 2, 4.0),
                new Filme("Filme3", 2, 4.0));

        // acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        // verificacao
        // 4 + 4 + (3 * .75)
        assertThat(locacao.getValor(), is(11.0));
    }

    @Test
    public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
        // cenario 
        Usuario usuario = new Usuario("Usuario1");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme1", 2, 4.0),
                new Filme("Filme2", 2, 4.0),
                new Filme("Filme3", 2, 4.0),
                new Filme("Filme4", 2, 4.0));

        // acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        // verificacao
        assertThat(locacao.getValor(), is(13.0));
    }

    @Test
    public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
        // cenario
        Usuario usuario = new Usuario("Usuario1");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme1", 2, 4.0),
                new Filme("Filme2", 2, 4.0),
                new Filme("Filme3", 2, 4.0),
                new Filme("Filme4", 2, 4.0),
                new Filme("Filme5", 2, 4.0));

        // acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        // verificacao
        assertThat(locacao.getValor(), is(14.0));
    }

    @Test
    public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
        // cenario
        Usuario usuario = new Usuario("Usuario1");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme1", 2, 4.0),
                new Filme("Filme2", 2, 4.0),
                new Filme("Filme3", 2, 4.0),
                new Filme("Filme4", 2, 4.0),
                new Filme("Filme5", 2, 4.0),
                new Filme("Filme6", 2, 4.0));

        // acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        // verificacao
        assertThat(locacao.getValor(), is(14.0));
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
        
        // Realiza o teste dinamicamente.
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
        
        // cenario
        Usuario usuario = new Usuario("Usuario1");
        List<Filme> filme = Arrays.asList(new Filme("Filme1", 2, 4.0));

        // acao
        Locacao locacao = service.alugarFilme(usuario, filme);

        // verificacao
        boolean ehSegunda = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
        Assert.assertTrue(ehSegunda);
    }

}
