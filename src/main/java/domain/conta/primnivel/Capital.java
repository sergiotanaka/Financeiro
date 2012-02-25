package domain.conta.primnivel;

import domain.conta.NaturezaConta;

public class Capital extends ContaPrimNivel {
	private static final String CAPITAL = "Capital";
	private static final long serialVersionUID = 1L;

	@Override
	public NaturezaConta getNatureza() {
		return NaturezaConta.CREDORA;
	}
	
	@Override
	public long getOid() {
		return -3;
	}

	@Override
	public String getNome() {
		return CAPITAL;
	}

}
