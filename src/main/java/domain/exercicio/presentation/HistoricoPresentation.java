package domain.exercicio.presentation;

import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.validation.Validatable;
import com.jgoodies.validation.ValidationResult;
import com.pinguin.validation.PresentationModel2;
import com.pinguin.validation.SelfValidatorPresentationModel;

import domain.conta.ContaAnalitica;
import domain.exercicio.Historico;

/**
 * TODO: <br>
 * 1. Abstrair a IHM de lancamento;<br>
 * 2. Modificar o {@link SelfValidatorPresentationModel} conforme necessidade;<br>
 * 3. Pensar sobre como validar os ValueHolder. Acho que este esquema não vai
 * funcionar.<br>
 */
public class HistoricoPresentation extends PresentationModel2<Historico>
		implements Validatable {

	private static final long serialVersionUID = 1L;

	private final ValueHolder contaDebitoHolder = new ValueHolder();
	private final ValueHolder contaCreditoHolder = new ValueHolder();

	public HistoricoPresentation(Historico bean) {
		super(bean);
	}

	public ValueHolder getContaDebitoHolder() {
		return contaDebitoHolder;
	}

	public ValueHolder getContaCreditoHolder() {
		return contaCreditoHolder;
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		if (getContaDebitoHolder().getValue() == null
				|| getContaCreditoHolder().getValue() == null) {
			result.addError("Conta débito/crédito não deve ser vazia.");
		}

		if (getContaDebitoHolder().getValue() == getContaCreditoHolder()
				.getValue()) {
			result.addError("As contas débito e crédito devem ser diferentes.");
		}

		if (getBuffer().getValor() <= 0.0) {
			result.addError("O valor deve ser maior que zero.");
		}

		return result;
	}

	@Override
	public void triggerCommit() {
		super.triggerCommit();

		// Setar o historico nas colecoes das contas
		ContaAnalitica contaDebito = (ContaAnalitica) getContaDebitoHolder()
				.getValue();
		ContaAnalitica contaCredito = (ContaAnalitica) getContaCreditoHolder()
				.getValue();

		contaDebito.getDebitos().add(getBean());
		contaCredito.getCreditos().add(getBean());
	}
}
