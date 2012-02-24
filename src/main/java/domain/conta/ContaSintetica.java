package domain.conta;

import java.util.ArrayList;
import java.util.List;

public class ContaSintetica extends Conta {
	private static final long serialVersionUID = 1L;

	private List<Conta> subContas = new ArrayList<Conta>();

	public ContaSintetica() {
		super();
	}

	public ContaSintetica(String nome) {
		super(nome);
	}

	public List<Conta> getSubContas() {
		return subContas;
	}

	public void addSubConta(Conta subConta) {
		this.subContas.add(subConta);
		subConta.contaPai = this;
	}

	public List<Conta> getTodasSubContas() {
		List<Conta> contas = new ArrayList<Conta>();

		contas.add(this);

		for (Conta conta : getSubContas()) {
			if (conta instanceof ContaAnalitica) {
				contas.add(conta);
			} else if (conta instanceof ContaSintetica) {
				contas.addAll(((ContaSintetica) conta).getTodasSubContas());
			}
		}

		return contas;
	}

	public boolean isPaiDe(Conta ref) {
		for (Conta conta : subContas) {
			if (conta.equals(ref)) {
				return true;
			}
			if (conta instanceof ContaSintetica) {
				if (((ContaSintetica) conta).isPaiDe(ref)) {
					return true;
				}
			}
		}

		return false;
	}

	public void removerSubConta(Conta conta) {
		conta.contaPai = null;
		this.getSubContas().remove(conta);
	}

	public void subirConta(Conta conta) {
		assert getSubContas().contains(conta);

		int index = getSubContas().indexOf(conta);

		if (index > 0) {
			getSubContas().remove(conta);
			getSubContas().add(index - 1, conta);
		}
	}

	public void descerConta(Conta conta) {
		assert getSubContas().contains(conta);

		int index = getSubContas().indexOf(conta);

		if (index < (getSubContas().size() - 1)) {
			getSubContas().remove(conta);
			getSubContas().add(index + 1, conta);
		}
	}

	@Override
	public double getValorSaldo() {
		double saldo = 0.0;

		for (Conta conta : getSubContas()) {
			saldo += conta.getValorSaldo();
		}

		return saldo;
	}
}
