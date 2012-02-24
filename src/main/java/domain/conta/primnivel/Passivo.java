package domain.conta.primnivel;

import domain.conta.NaturezaConta;

public class Passivo extends ContaPrimNivel {

	private static final String PASSIVO = "Passivo";
	private static final long serialVersionUID = 1L;

	@Override
	public NaturezaConta getNatureza() {
		return NaturezaConta.CREDORA;
	}

	@Override
	public String getNome() {
		return PASSIVO;
	}
}
