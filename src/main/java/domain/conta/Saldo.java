package domain.conta;

import com.jgoodies.binding.beans.Model;

public class Saldo extends Model {
	private static final long serialVersionUID = 1L;

	public static Saldo ZERO = new Saldo();
	
	private double debito = 0;
	private double credito = 0;

	public double getDebito() {
		return debito;
	}

	public void addDebito(double debito) {
		this.debito += debito;
	}

	public double getCredito() {
		return credito;
	}

	public void addCredito(double credito) {
		this.credito += credito;
	}

	public double getValor(NaturezaConta natureza) {
		if (natureza.equals(NaturezaConta.CREDORA)) {
			return getCredito() - getDebito();
		} else {
			return getDebito() - getCredito();
		}
	
	
	}

	public void zerar() {
		this.debito = 0;
		this.credito = 0;
	}
}
