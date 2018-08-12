package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Usuario;

/**
 *
 * @author Rômulo Göelzer Portolann
 */
public interface EmailService {

  void notificarAtraso(Usuario usuario);

}
