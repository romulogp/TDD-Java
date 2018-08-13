package br.ce.wcaquino.builder;

import static br.ce.wcaquino.builder.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author Rômulo Göelzer Portolann
 */
public class LocacaoBuilder {

  private Locacao locacao;

  private LocacaoBuilder() {
  }

  public static LocacaoBuilder umaLocacao() {
    LocacaoBuilder builder = new LocacaoBuilder();
    builder.locacao = new Locacao();
    builder.locacao.setUsuario(umUsuario().agora());
    builder.locacao.setDataLocacao(new Date());
    builder.locacao.setDataRetorno(obterDataComDiferencaDias(1));
    builder.locacao.setFilmes(Arrays.asList(umFilme().agora()));
    builder.locacao.setValor(4.0);
    return builder;
  }

  public LocacaoBuilder comUsuario(Usuario usuario) {
    locacao.setUsuario(usuario);
    return this;
  }

  public LocacaoBuilder comDataRetorno(Date dataRetorno) {
    locacao.setDataRetorno(dataRetorno);
    return this;
  }

  public LocacaoBuilder atrasada() {
    locacao.setDataLocacao(obterDataComDiferencaDias(-4));
    locacao.setDataRetorno(obterDataComDiferencaDias(-2));
    return this;
  }

  public Locacao agora() {
    return locacao;
  }

}
