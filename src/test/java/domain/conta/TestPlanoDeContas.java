package domain.conta;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

import domain.conta.ContaAnalitica;
import domain.conta.PlanoDeContas;

public class TestPlanoDeContas {

	@Test
	public void testFindConta() {
		PlanoDeContas plano = PlanoDeContas.buildPlano();

		Collection<ContaAnalitica> contasAnaliticas = plano
				.findContaAnaliticaPorNome("Santander");

		Assert.assertEquals(2, contasAnaliticas.size());
	}

}
