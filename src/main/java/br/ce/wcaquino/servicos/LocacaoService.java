package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import static br.ce.wcaquino.utils.DataUtils.adicionarDias;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LocacaoService {

  private LocacaoDAO locacaoDAO;
  private SPCService spcService;
  private EmailService emailService;

  public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {
    if (usuario == null) {
      throw new LocadoraException("Usuario vazio");
    }

    if (filmes == null || filmes.isEmpty()) {
      throw new LocadoraException("Filme vazio");
    }

    for (Filme filme : filmes) {
      if (filme.getEstoque() == 0) {
        throw new FilmeSemEstoqueException();
      }
    }

    boolean negativado;
    try {
      negativado = spcService.possuiNegaticacao(usuario);
    } catch (Exception ex) {
      throw new LocadoraException("Problemas com SPC, tente novamente");
    }

    if (negativado) {
      throw new LocadoraException("Usu�rio Negativado");
    }

    Locacao locacao = new Locacao();
    locacao.setFilmes(filmes);
    locacao.setUsuario(usuario);
    locacao.setDataLocacao(new Date());
    Double valorTotal = 0.0;
    for (int i = 0; i < filmes.size(); i++) {
      Filme filme = filmes.get(i);
      double valorFilme = filme.getPrecoLocacao();
      switch (i) {
        case 2:
          valorFilme = valorFilme * .75;
          break;
        case 3:
          valorFilme = valorFilme * .50;
          break;
        case 4:
          valorFilme = valorFilme * .25;
          break;
        case 5:
          valorFilme = 0;
        default:
          break;
      }
      valorTotal += valorFilme;
    }
    locacao.setValor(valorTotal);

    //Entrega no dia seguinte
    Date dataEntrega = adicionarDias(new Date(), 1);
    if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
      dataEntrega = adicionarDias(dataEntrega, 1);
    }
    locacao.setDataRetorno(dataEntrega);

    //Salvando a locacao...	
    locacaoDAO.salvar(locacao);

    return locacao;
  }

  public void notificarAtrasos() {
    List<Locacao> locacoes = locacaoDAO.obterLocacoesPendentes();
    for (Locacao locacao : locacoes) {
      if (locacao.getDataRetorno().before(new Date())) {
        emailService.notificarAtraso(locacao.getUsuario());
      }
    }
  }

}
