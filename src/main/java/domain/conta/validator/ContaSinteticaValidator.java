package domain.conta.validator;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;

import domain.conta.ContaSintetica;

public class ContaSinteticaValidator implements Validator<ContaSintetica> {
	@Override
	public ValidationResult validate(ContaSintetica conta) {
		ValidationResult result = new ValidationResult();
		
		if (conta.getContaPai() == null) {
			result.addError("A conta pai n�o pode ser vazia.");
		}
		
		if (conta.getNome() == null || conta.getNome().isEmpty()) {
			result.addError("A descri��o n�o pode ser vazia.");
		}
		
		return result;
	}

}
