package domain.exercicio.validator;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;

import domain.exercicio.Historico;

public class HistoricoValidator implements Validator<Historico> {

	@Override
	public ValidationResult validate(Historico historico) {
		ValidationResult result = new ValidationResult();
		
//		if (historico.getContaCredito() == null || historico.getContaDebito() == null) {
//			result.addError("As contas de crédito/débito não podem ser nulos.");
//		}
//		
//		if (historico.getValor() <= 0) {
//			result.addError("O valor deve ser maior que zero.");
//		}
		
		return result;
	}

}
