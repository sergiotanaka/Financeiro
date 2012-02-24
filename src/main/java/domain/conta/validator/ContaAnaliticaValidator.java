package domain.conta.validator;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;

import domain.conta.ContaAnalitica;

public class ContaAnaliticaValidator implements Validator<ContaAnalitica> {

	@Override
	public ValidationResult validate(ContaAnalitica conta) {
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
