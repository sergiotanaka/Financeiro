package domain.conta;

import java.util.ArrayList;
import java.util.List;

import domain.exercicio.Historico;

public class ContaAnalitica extends Conta {

	private static final long serialVersionUID = 1L;

	private final List<Historico> creditos = new ArrayList<Historico>();
	private final List<Historico> debitos = new ArrayList<Historico>();

	public ContaAnalitica() {
	}

	public ContaAnalitica(String nome) {
		super(nome);
	}

	public List<Historico> getCreditos() {
		return creditos;
	}

	public List<Historico> getDebitos() {
		return debitos;
	}

	@Override
	public double getValorSaldo() {
		double saldo = 0.0;

		for (Historico historico : getCreditos()) {
			saldo += historico.getValor();
		}

		for (Historico historico : getDebitos()) {
			saldo -= historico.getValor();
		}

		return saldo;
	}
}
