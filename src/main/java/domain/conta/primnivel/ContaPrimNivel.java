package domain.conta.primnivel;

import domain.conta.ContaSintetica;

/**
 * Conta de primeiro nivel nao tem pai. Eh ele que define a natureza de todas as
 * suas subcontas.
 */
public abstract class ContaPrimNivel extends ContaSintetica {

	private static final long serialVersionUID = 1L;

	@Override
	public void setContaPai(ContaSintetica conta) {
		throw new RuntimeException("Esta conta não tem conta pai.");
	}
	
	@Override
	public ContaSintetica getContaPai() {
		return null;
	}

}
