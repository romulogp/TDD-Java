package br.ce.wcaquino.daos;

import br.ce.wcaquino.entidades.Locacao;

/**
 *
 * @author R�mulo G�elzer Portolann
 */
public class LocacaoDAOFake implements LocacaoDAO {

  public Locacao salvar(Locacao locacao) {
    return locacao;
  }

}
