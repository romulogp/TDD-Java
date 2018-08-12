package br.ce.wcaquino.daos;

import br.ce.wcaquino.entidades.Locacao;
import java.util.List;

/**
 *
 * @author Rômulo Göelzer Portolann
 */
public class LocacaoDAOFake implements LocacaoDAO {

  public void salvar(Locacao locacao) {
    // salvando..
  }

  public List<Locacao> obterLocacoesPendentes() {
    return null;
  }

}
