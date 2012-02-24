package domain.exercicio;

import java.util.Date;

import com.jgoodies.binding.beans.Model;

import domain.conta.CentroCusto;
import domain.conta.Conta;
import domain.conta.ContaAnalitica;

public class Historico extends Model {
	private static final long serialVersionUID = 1L;

	private Date dataHora;
//	private ContaAnalitica contaDebito;
//	private ContaAnalitica contaCredito;
	private CentroCusto centroCusto;
	private String descricao;
	private double valor;

	public Historico() {
	}

	public Historico(Date dataHora, ContaAnalitica debito,
			ContaAnalitica credito, String descricao, double valor) {
		this.setDataHora(dataHora);
//		this.setContaCredito(credito);
//		this.setContaDebito(debito);
		this.setDescricao(descricao);
		this.setValor(valor);
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

//	public ContaAnalitica getContaDebito() {
//		return contaDebito;
//	}
//
//	public void setContaDebito(ContaAnalitica contaDebito) {
//		this.contaDebito = contaDebito;
//	}
//
//	public ContaAnalitica getContaCredito() {
//		return contaCredito;
//	}
//
//	public void setContaCredito(ContaAnalitica contaCredito) {
//		this.contaCredito = contaCredito;
//	}

	public String getDescricao() {
		return descricao;
	}

	public CentroCusto getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(CentroCusto centroCusto) {
		this.centroCusto = centroCusto;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public ContaAnalitica getContrapartida(ContaAnalitica conta) {
//		if (conta.equals(getContaCredito())) {
//			return getContaDebito();
//		} else {
//			return getContaCredito();
//		}
		return null;
	}

	public double getValorEmRelacaoA(ContaAnalitica conta) {
//		if (conta.equals(getContaCredito())) {
//			if (conta.getNatureza().equals(NaturezaConta.DEVEDORA)) {
//				return -getValor();
//			} else {
//				return getValor();
//			}
//		} else {
//			if (conta.getNatureza().equals(NaturezaConta.DEVEDORA)) {
//				return getValor();
//			} else {
//				return -getValor();
//			}
//		}
		return getValor();
	}

	public boolean isCreditoPara(Conta conta, CentroCusto centroCusto) {
//		if (conta instanceof ContaAnalitica) {
//			if (centroCusto == null) {
//				return getContaCredito().equals(conta);
//			} else {
//				return getContaCredito().equals(conta)
//						&& (getCentroCusto() != null && getCentroCusto()
//								.equals(centroCusto));
//			}
//		} else if (conta instanceof ContaSintetica) {
//			if (centroCusto == null) {
//				return ((ContaSintetica) conta).isPaiDe(getContaCredito());
//			} else {
//				return ((ContaSintetica) conta).isPaiDe(getContaCredito())
//						&& (getCentroCusto() != null && getCentroCusto()
//								.equals(centroCusto));
//			}
//		}
		return false;
	}

	public boolean isDebitoPara(Conta conta, CentroCusto centroCusto) {
//		if (conta instanceof ContaAnalitica) {
//			if (centroCusto == null) {
//				return getContaDebito().equals(conta);
//			} else {
//				return getContaDebito().equals(conta)
//						&& (getCentroCusto() != null && getCentroCusto()
//								.equals(centroCusto));
//			}
//		} else if (conta instanceof ContaSintetica) {
//			if (centroCusto == null) {
//				return ((ContaSintetica) conta).isPaiDe(getContaDebito());
//			} else {
//				return ((ContaSintetica) conta).isPaiDe(getContaDebito())
//						&& (getCentroCusto() != null && getCentroCusto()
//								.equals(centroCusto));
//			}
//		}
		return false;
	}
}
