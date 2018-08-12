package br.ce.wcaquino.builder;

import br.ce.wcaquino.entidades.Usuario;

/**
 *
 * @author R�mulo G�elzer Portolann
 */
public class UsuarioBuilder {

    private Usuario usuario;

    private UsuarioBuilder() {
    }

    public static UsuarioBuilder umUsuario() {
        UsuarioBuilder builder = new UsuarioBuilder();
        builder.usuario = new Usuario();
        builder.usuario.setNome("Usuario 1");
        return builder;
    }

    public Usuario agora() {
        return usuario;
    }

}
