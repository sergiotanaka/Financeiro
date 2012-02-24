package domain.exercicio;

import java.io.Serializable;
import java.util.Date;

/**
 * Contem o conceito de itervalo de tempo.
 */
public class Periodo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nome;
	private Date inicio;
	private Date fim;

	public Periodo() {
	}

	public Periodo(Date inicio, Date fim) {
		this();

		setInicio(inicio);
		setFim(fim);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	@Override
	public String toString() {
		return getNome();
	}

	public boolean estaDentro(Date dataHora) {
		return (dataHora.after(getInicio())) && (dataHora.before(getFim()));
	}
}
