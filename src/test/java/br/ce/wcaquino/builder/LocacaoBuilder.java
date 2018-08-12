package br.ce.wcaquino.builder;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import java.util.ArrayList;
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
    builder.locacao.setUsuario(null);
    builder.locacao.setDataLocacao(null);
    builder.locacao.setDataRetorno(null);
    builder.locacao.setFilmes(new ArrayList<Filme>());
    builder.locacao.setValor(0.0);
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

  public Locacao agora() {
    return locacao;
  }

}
