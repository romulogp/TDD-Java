package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Usuario;

/**
 *
 * @author R�mulo G�elzer Portolann
 */
public interface EmailService {

  void notificarAtraso(Usuario usuario);

}
