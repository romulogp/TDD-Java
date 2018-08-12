package br.ce.wcaquino.builder;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import java.util.ArrayList;

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
    builder.locacao.setUsuario(null);
    builder.locacao.setDataLocacao(null);
    builder.locacao.setDataRetorno(null);
    builder.locacao.setFilmes(new ArrayList<Filme>());
    builder.locacao.setValor(0.0);
    return builder;
  }

  public Locacao agora() {
    return locacao;
  }

}
