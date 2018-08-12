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
    
    if (spcService.possuiNegaticacao(usuario)) {
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

  public void setLocacaoDAO(LocacaoDAO locacaoDAO) {
    this.locacaoDAO = locacaoDAO;
  }
  
  public void setSPCService(SPCService spcService) {
    this.spcService = spcService;
  }

}
