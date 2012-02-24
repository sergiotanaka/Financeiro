package domain.conta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import domain.conta.primnivel.Ativo;
import domain.conta.primnivel.Capital;
import domain.conta.primnivel.Despesas;
import domain.conta.primnivel.Passivo;
import domain.conta.primnivel.Receitas;

public class PlanoDeContas implements Serializable {
	private static final long serialVersionUID = 1L;

	private final Ativo ativo = new Ativo();
	private final Passivo passivo = new Passivo();
	private final Capital capital = new Capital();
	private final Despesas custosDespesas = new Despesas();
	private final Receitas receitas = new Receitas();

	public PlanoDeContas() {
	}

	public Ativo getAtivo() {
		return ativo;
	}

	public Passivo getPassivo() {
		return passivo;
	}

	public Capital getCapital() {
		return capital;
	}

	public Despesas getCustosDespesas() {
		return custosDespesas;
	}

	public Receitas getReceitas() {
		return receitas;
	}

	public static PlanoDeContas buildPlano() {
		PlanoDeContas plano = new PlanoDeContas();

		ContaSintetica ativoCirculante = new ContaSintetica("Ativo Circulante");
		ContaSintetica naoCirculante = new ContaSintetica("Não Circulante");
		ContaSintetica bancos = new ContaSintetica("Bancos");
		ContaSintetica investimentos = new ContaSintetica("Investimentos");
		ContaSintetica imobilizado = new ContaSintetica("Imobilizado");

		ativoCirculante.addSubConta(new ContaAnalitica("Caixa"));
		ativoCirculante.addSubConta(bancos);

		bancos.addSubConta(new ContaAnalitica("Santander C/C"));
		bancos.addSubConta(new ContaAnalitica("Santander Poup"));
		bancos.addSubConta(new ContaAnalitica("B. Brasil"));
		bancos.addSubConta(new ContaAnalitica("Itaú"));

		naoCirculante.addSubConta(investimentos);
		naoCirculante.addSubConta(imobilizado);

		investimentos.addSubConta(new ContaAnalitica("CDB Santander"));
		imobilizado.addSubConta(new ContaAnalitica("Moto Fazer"));

		plano.getAtivo().addSubConta(ativoCirculante);
		plano.getAtivo().addSubConta(naoCirculante);

		ContaSintetica despesas = new ContaSintetica("Despesas Gerais");
		despesas.addSubConta(new ContaAnalitica("Aluguel"));
		despesas.addSubConta(new ContaAnalitica("Alimentação"));
		despesas.addSubConta(new ContaAnalitica("Transporte"));
		despesas.addSubConta(new ContaAnalitica("Vestuário"));
		despesas.addSubConta(new ContaAnalitica("Diversão"));
		plano.getCustosDespesas().addSubConta(despesas);

		plano.getReceitas().addSubConta(new ContaAnalitica("Salário"));
		plano.getReceitas().addSubConta(new ContaAnalitica("Outras receitas"));

		return plano;
	}

	public List<ContaAnalitica> findContaAnaliticaPorNome(String nome) {
		List<ContaAnalitica> contas = new ArrayList<ContaAnalitica>();

		for (ContaAnalitica conta : getContasAnaliticas()) {
			if (conta.getNome().toUpperCase().contains(nome.toUpperCase())) {
				contas.add(conta);
			}
		}

		return contas;
	}

	public List<ContaAnalitica> getContasAnaliticas() {
		List<ContaAnalitica> contas = new ArrayList<ContaAnalitica>();

		for (Conta conta : getTodasContas()) {
			if (conta instanceof ContaAnalitica) {
				contas.add((ContaAnalitica) conta);
			}
		}

		return contas;
	}

	public List<ContaSintetica> getContasSinteticas() {
		List<ContaSintetica> contas = new ArrayList<ContaSintetica>();

		for (Conta conta : getTodasContas()) {
			if (conta instanceof ContaSintetica) {
				contas.add((ContaSintetica) conta);
			}
		}

		return contas;
	}

	public List<Conta> getTodasContas() {
		ArrayList<Conta> contas = new ArrayList<Conta>();

		contas.addAll(getAtivo().getTodasSubContas());
		contas.addAll(getPassivo().getTodasSubContas());
		contas.addAll(getCapital().getTodasSubContas());
		contas.addAll(getCustosDespesas().getTodasSubContas());
		contas.addAll(getReceitas().getTodasSubContas());

		return contas;
	}
}
