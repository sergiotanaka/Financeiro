package domain.conta.validator;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;

import domain.conta.ContaAnalitica;

public class ContaAnaliticaValidator implements Validator<ContaAnalitica> {

	@Override
	public ValidationResult validate(ContaAnalitica conta) {
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
