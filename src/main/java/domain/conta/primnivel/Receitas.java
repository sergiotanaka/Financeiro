package domain.conta.primnivel;

import domain.conta.NaturezaConta;

public class Receitas extends ContaPrimNivel {
	private static final String RECEITAS = "Receitas";
	private static final long serialVersionUID = 1L;

	@Override
	public NaturezaConta getNatureza() {
		return NaturezaConta.CREDORA;
	}

	@Override
	public long getOid() {
		return -5;
	}

	@Override
	public String getNome() {
		return RECEITAS;
	}
}
