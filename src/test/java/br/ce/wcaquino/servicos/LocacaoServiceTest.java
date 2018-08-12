package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builder.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;
import br.ce.wcaquino.daos.LocacaoDAO;
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
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

public class LocacaoServiceTest {

  private static int testCount = 0;
  private LocacaoService service;

  private SPCService spc;
  private LocacaoDAO dao;

  @Before
  public void setup() {
    service = new LocacaoService();
    dao = Mockito.mock(LocacaoDAO.class);
    service.setLocacaoDAO(dao);
    spc = Mockito.mock(SPCService.class);
    service.setSPCService(spc);
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
    Usuario usuario = umUsuario().agora();
    Filme filme = umFilme().comValor(5.0).agora();

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
    Usuario usuario = umUsuario().agora();
    Filme filme = umFilme().agora();
    Filme filme2 = umFilme().semEstoque().agora();

    //acao
    service.alugarFilme(usuario, Arrays.asList(filme, filme2));
  }

  @Test
  public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
    //cenario
    Filme filme = umFilme().agora();

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
    Usuario usuario = umUsuario().agora();

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
    Usuario usuario = umUsuario().agora();
    List<Filme> filme = Arrays.asList(umFilme().agora());

    // acao
    Locacao locacao = service.alugarFilme(usuario, filme);

    // verificacao
    assertThat(locacao.getDataRetorno(), caiEm(Calendar.MONDAY));
    assertThat(locacao.getDataRetorno(), caiNumaSegunda());
  }

  @Test
  public void naoDeveAlugarFilmeParaNegativadoSPC() throws FilmeSemEstoqueException, LocadoraException {
    // cenario
    Usuario usuario = umUsuario().agora();
    List<Filme> filmes = Arrays.asList(umFilme().agora());

    when(spc.possuiNegaticacao(usuario)).thenReturn(true);

    exception.expect(LocadoraException.class);
    exception.expectMessage("Usuário Negativado");

    // acao
    service.alugarFilme(usuario, filmes);
  }

}
