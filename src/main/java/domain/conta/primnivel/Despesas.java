package domain.conta.primnivel;

import domain.conta.NaturezaConta;

public class Despesas extends ContaPrimNivel {
	private static final String DESPESAS = "Despesas";
	private static final long serialVersionUID = 1L;

	@Override
	public NaturezaConta getNatureza() {
		return NaturezaConta.DEVEDORA;
	}

	@Override
	public long getOid() {
		return -4;
	}
	
	@Override
	public String getNome() {
		return DESPESAS;
	}
}
