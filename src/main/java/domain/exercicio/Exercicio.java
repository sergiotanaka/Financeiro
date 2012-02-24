package domain.exercicio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.conta.CentroCusto;
import domain.conta.Conta;
import domain.conta.ContaAnalitica;
import domain.conta.ContaSintetica;
import domain.conta.PlanoDeContas;

public class Exercicio implements Serializable {
	private static final long serialVersionUID = 1L;

	private final Periodo periodo = new Periodo();
	private PlanoDeContas plano;
	private final List<CentroCusto> centrosCusto = new ArrayList<CentroCusto>();
	private final List<Historico> historicos = new ArrayList<Historico>();

	private final Periodo filtro = new Periodo();
	private boolean filtroAtivado = false;

	public Exercicio() {
	}

	public Exercicio(Date dataInicio, Date dataFim, PlanoDeContas plano) {
		this();

		getPeriodo().setInicio(dataInicio);
		getPeriodo().setFim(dataFim);
		getFiltro().setInicio(dataInicio);
		getFiltro().setFim(dataFim);
		setPlano(plano);
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public PlanoDeContas getPlano() {
		return plano;
	}

	public void setPlano(PlanoDeContas plano) {
		this.plano = plano;
	}

	public List<CentroCusto> getCentrosCusto() {
		return centrosCusto;
	}

	public List<Historico> getHistoricos() {
		return historicos;
	}

	private List<Historico> getHistoricosSomLeitura() {
		if (isFiltroAtivado()) {
			List<Historico> filtrado = new ArrayList<Historico>();
			for (Historico historico : historicos) {
				if (getFiltro().estaDentro(historico.getDataHora())) {
					filtrado.add(historico);
				}
			}
			return filtrado;
		} else {
			return new ArrayList<Historico>(historicos);
		}
	}

	public double getSaldoConta(ContaAnalitica conta) {
		double saldo = 0.0;

//		for (Historico historico : getHistoricosSomLeitura()) {
//			if (historico.getContaCredito().equals(conta)) {
//				saldo += historico.getValor();
//			} else if (historico.getContaDebito().equals(conta)) {
//				saldo -= historico.getValor();
//			}
//		}

		return saldo;
	}

	public double getSaldoConta(ContaSintetica conta) {
		double saldo = 0.0;

//		for (Historico historico : getHistoricosSomLeitura()) {
//			if (conta.isPaiDe(historico.getContaCredito())) {
//				saldo += historico.getValor();
//			} else if (conta.isPaiDe(historico.getContaDebito())) {
//				saldo -= historico.getValor();
//			}
//		}

		return saldo;
	}

	public Periodo getFiltro() {
		return filtro;
	}

	public boolean isFiltroAtivado() {
		return filtroAtivado;
	}

	public void setFiltroAtivado(boolean filtroAtivado) {
		this.filtroAtivado = filtroAtivado;
	}

	public ArrayList<Historico> getHistoricoDa(ContaAnalitica conta,
			CentroCusto centro, boolean considerarFiltro) {
		ArrayList<Historico> historicoConta = new ArrayList<Historico>();

//		for (Historico historico : considerarFiltro ? getHistoricosSomLeitura()
//				: getHistoricos()) {
//			if (historico.getContaCredito().equals(conta)
//					|| historico.getContaDebito().equals(conta)) {
//				if (centro == null) {
//					historicoConta.add(historico);
//				} else {
//					if (historico.getCentroCusto() != null
//							&& historico.getCentroCusto().equals(centro)) {
//						historicoConta.add(historico);
//					}
//				}
//			}
//		}

		Collections.sort(historicoConta, new Comparator<Historico>() {

			@Override
			public int compare(Historico o1, Historico o2) {
				return o1.getDataHora().compareTo(o2.getDataHora());
			}

		});

		return historicoConta;
	}

	public void processarSaldos(CentroCusto centroCusto) {
		for (Conta conta : getPlano().getTodasContas()) {
			processarSaldo(conta, centroCusto);
		}
	}

	private void processarSaldo(Conta conta, CentroCusto centroCusto) {
		conta.getSaldo().zerar();
		for (Historico historico : getHistoricosSomLeitura()) {
			if (historico.isCreditoPara(conta, centroCusto)) {
				conta.getSaldo().addCredito(historico.getValor());
			}
			if (historico.isDebitoPara(conta, centroCusto)) {
				conta.getSaldo().addDebito(historico.getValor());
			}
		}
	}

	public Map<Historico, Double> processarSaldo(ContaAnalitica conta,
			ArrayList<Historico> historicos) {
		Map<Historico, Double> saldos = new HashMap<Historico, Double>();

		double saldo = 0;
		for (Historico historico : historicos) {
			saldo += historico.getValorEmRelacaoA(conta);
			saldos.put(historico, saldo);
		}

		return saldos;
	}

}
