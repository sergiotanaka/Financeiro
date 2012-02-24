package domain.conta;

import com.jgoodies.binding.beans.Model;

public class CentroCusto extends Model {
	private static final long serialVersionUID = 1L;

	private String nome;

	public CentroCusto() {
	}

	public CentroCusto(String nome) {
		setNome(nome);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return getNome();
	}

}
