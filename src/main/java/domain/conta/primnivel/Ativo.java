package domain.conta.primnivel;

import domain.conta.NaturezaConta;

public class Ativo extends ContaPrimNivel {

	private static final String ATIVO = "Ativo";
	private static final long serialVersionUID = 1L;
	
	@Override
	public NaturezaConta getNatureza() {
		return NaturezaConta.DEVEDORA;
	}
	
	@Override
	public long getOid() {
		return -1;
	}
	
	@Override
	public String getNome() {
		return ATIVO;
	}
}
