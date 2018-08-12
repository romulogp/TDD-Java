package br.ce.wcaquino.daos;

import br.ce.wcaquino.entidades.Locacao;

/**
 *
 * @author Rômulo Göelzer Portolann
 */
public class LocacaoDAOFake implements LocacaoDAO {

  public Locacao salvar(Locacao locacao) {
    return locacao;
  }

}
