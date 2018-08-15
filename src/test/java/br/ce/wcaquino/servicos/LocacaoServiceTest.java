package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builder.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builder.LocacaoBuilder.umaLocacao;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import static br.ce.wcaquino.matchers.Matchers.caiNumaSegunda;
import static br.ce.wcaquino.matchers.Matchers.ehHoje;
import static br.ce.wcaquino.matchers.Matchers.ehHojeComDiferencaDeDias;
import br.ce.wcaquino.utils.DataUtils;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)       // Dizer ao jUnit que a execução será gerenciado pelo PowerMock
@PrepareForTest({LocacaoService.class, DataUtils.class}) // Classe para execução
public class LocacaoServiceTest {

  private static int testCount = 0;

  @InjectMocks
  private LocacaoService service;

  @Mock
  private SPCService spc;
  @Mock
  private LocacaoDAO dao;
  @Mock
  private EmailService email;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
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
    //cenario
    Usuario usuario = umUsuario().agora();
    Filme filme = umFilme().comValor(5.0).agora();

    PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(28, 4, 2017));

    //acao
    Locacao locacao = service.alugarFilme(usuario, Arrays.asList(filme));

    //verificacao
    error.checkThat(locacao.getValor(), is(equalTo(5.0)));
//    error.checkThat(locacao.getDataLocacao(), ehHoje());
//    error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDeDias(1));
    error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)), is(true));
    error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)), is(true));
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
  public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException, Exception {
    // cenario
    Usuario usuario = umUsuario().agora();
    List<Filme> filme = Arrays.asList(umFilme().agora());

    PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(29, 4, 2017));

    // acao
    Locacao locacao = service.alugarFilme(usuario, filme);

    // verificacao
    assertThat(locacao.getDataRetorno(), caiNumaSegunda());
    PowerMockito.verifyNew(Date.class).withNoArguments();
  }

  @Test
  public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
    // cenario
    Usuario usuario = umUsuario().agora();
    List<Filme> filmes = Arrays.asList(umFilme().agora());

    when(spc.possuiNegaticacao(Mockito.any(Usuario.class))).thenReturn(true); // se qualquer usuario for checado

    // acao
    try {
      service.alugarFilme(usuario, filmes);
      // verificacao
      Assert.fail();
    } catch (LocadoraException ex) {
      Assert.assertThat(ex.getMessage(), is("Usuï¿½rio Negativado"));
    }

    verify(spc).possuiNegaticacao(usuario);
  }

  @Test
  public void deveEnviarEmailParaLocacoesAtrasadas() {
    // cenario
    Usuario usuario = umUsuario().agora();
    Usuario usuario2 = umUsuario().comNome("Usuario em dia").agora();
    Usuario usuario3 = umUsuario().comNome("Outro atrasado").agora();

    List<Locacao> locacoes = Arrays.asList(
        umaLocacao().comUsuario(usuario).atrasada().agora(),
        umaLocacao().comUsuario(usuario2).agora(),
        umaLocacao().comUsuario(usuario3).atrasada().agora(),
        umaLocacao().comUsuario(usuario3).atrasada().agora());

    when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

    // acao
    service.notificarAtrasos();

    // verificacao multipla
    verify(email, times(3)).notificarAtraso(Mockito.any(Usuario.class)); // deve notificar qualquer usuario pelo menos 3 vezes
    verify(email).notificarAtraso(usuario);                              // deve notificar o usuario
    verify(email, Mockito.atLeastOnce()).notificarAtraso(usuario3);      // deve notificar pelo menos uma vez o usuario3
    verify(email, never()).notificarAtraso(usuario2);                    // NAO deve notificar o usuario2
    verifyNoMoreInteractions(email);                                     // nao deve haver mais envio de emails

//    // verificacao simples
//    verify(email).notificarAtraso(usuario); // deve receber email
//    verify(email).notificarAtraso(usuario3);// deve receber email
//    verify(email, never()).notificarAtraso(usuario2); // Nï¿½O deve receber email
//    verifyNoMoreInteractions(email);
  }

  @Test
  public void deveTratarErroNoSPC() throws Exception {
    // cenario
    Usuario usuario = umUsuario().agora();
    List<Filme> filmes = Arrays.asList(umFilme().agora());

    when(spc.possuiNegaticacao(usuario)).thenThrow(new Exception("Falha abominante"));

    // verificacao
    exception.expect(LocadoraException.class);
    exception.expectMessage("Problemas com SPC, tente novamente");

    // acao
    service.alugarFilme(usuario, filmes);
  }

  @Test
  public void deveProrrogarUmaLocacao() {
    // cenario
    Locacao locacao = umaLocacao().agora();

    // acao
    service.prorrogarLocacao(locacao, 3);

    // verificacao
    ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
    Mockito.verify(dao).salvar(argCapt.capture());
    Locacao locacaoRetornada = argCapt.getValue();

    error.checkThat(locacaoRetornada.getValor(), is(12.0));
    error.checkThat(locacaoRetornada.getDataLocacao(), ehHoje());
    error.checkThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDeDias(3));
  }

}
