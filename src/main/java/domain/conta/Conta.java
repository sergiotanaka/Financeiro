package domain.conta;

import com.jgoodies.binding.beans.Model;

import domain.conta.primnivel.Ativo;
import domain.conta.primnivel.Capital;
import domain.conta.primnivel.Despesas;
import domain.conta.primnivel.Passivo;
import domain.conta.primnivel.Receitas;

/**
 * Representa conceito de conta.
 */
public abstract class Conta extends Model {
	private static final long serialVersionUID = 1L;

	private long oid = 0;
	private String nome;
	protected ContaSintetica contaPai;
	private final Saldo saldo = new Saldo();

	public Conta() {
	}

	public Conta(String nome) {
		this.nome = nome;
	}

	public long getOid() {
		return oid;
	}

	public void setOid(long oid) {
		this.oid = oid;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public NaturezaConta getNatureza() {
		return getContaPai().getNatureza();
	}

	public ContaSintetica getContaPai() {
		return contaPai;
	}

	public void setContaPai(ContaSintetica contaPai) {
		if (getContaPai() != null) {
			this.getContaPai().removerSubConta(this);
		}

		this.contaPai = contaPai;
		this.contaPai.getSubContas().add(this);
	}

	public Saldo getSaldo() {
		return saldo;
	}

	protected String getCodigo() {
		String codigo = "";

		if (getContaPai() == null) {
			if (this instanceof Ativo) {
				return "1";
			} else if (this instanceof Passivo) {
				return "2";
			} else if (this instanceof Capital) {
				return "3";
			} else if (this instanceof Despesas) {
				return "4";
			} else if (this instanceof Receitas) {
				return "5";
			}
			return "";
		}

		codigo = getContaPai().getCodigo();

		if (!codigo.isEmpty()) {
			codigo = codigo.concat(".");
		}
		codigo = codigo.concat(""
				+ (this.getContaPai().getSubContas().indexOf(this) + 1));

		return codigo;
	}

	@Override
	public String toString() {
		return String.format("%s %s", this.getCodigo(), this.getNome());
	}

	public double getValorSaldo() {
		return 0;
	}

}
