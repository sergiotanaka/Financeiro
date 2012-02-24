package domain.conta.validator;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;

import domain.conta.ContaSintetica;

public class ContaSinteticaValidator implements Validator<ContaSintetica> {
	@Override
	public ValidationResult validate(ContaSintetica conta) {
		ValidationResult result = new ValidationResult();
		
		if (conta.getContaPai() == null) {
			result.addError("A conta pai não pode ser vazia.");
		}
		
		if (conta.getNome() == null || conta.getNome().isEmpty()) {
			result.addError("A descrição não pode ser vazia.");
		}
		
		return result;
	}

}
