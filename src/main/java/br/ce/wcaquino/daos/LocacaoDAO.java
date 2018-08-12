package br.ce.wcaquino.daos;

import br.ce.wcaquino.entidades.Locacao;
import java.util.List;

/**
 *
 * @author Rômulo Göelzer Portolann
 */
public interface LocacaoDAO {

  void salvar(Locacao locacao);

  List<Locacao> obterLocacoesPendentes();

}
