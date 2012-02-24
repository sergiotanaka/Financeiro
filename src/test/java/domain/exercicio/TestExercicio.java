package domain.exercicio;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import domain.conta.ContaAnalitica;
import domain.conta.PlanoDeContas;
import domain.exercicio.Exercicio;
import domain.exercicio.Historico;

public class TestExercicio {

	@Test
	public void testSaldo() {
		PlanoDeContas plano = PlanoDeContas.buildPlano();
		
		Exercicio exercicio = new Exercicio(new Date(), new Date(), plano);
		ContaAnalitica caixa = (ContaAnalitica) plano
				.findContaAnaliticaPorNome("Caixa").toArray()[0];
		ContaAnalitica itau = (ContaAnalitica) plano.findContaAnaliticaPorNome(
				"Itaú").toArray()[0];
		ContaAnalitica bbrasil = (ContaAnalitica) plano
				.findContaAnaliticaPorNome("B. Brasil").toArray()[0];
		
		double saldoConta = exercicio.getSaldoConta(caixa);

		Assert.assertEquals(0.0, saldoConta);

		exercicio.getHistoricos().add(
				new Historico(new Date(), caixa, itau, "deposito", 15.0));
		exercicio.getHistoricos().add(
				new Historico(new Date(), caixa, bbrasil, "deposito", 500.0));
		exercicio.getHistoricos()
				.add(new Historico(new Date(), itau, bbrasil, "transferencia",
						70.0));

		Assert.assertEquals(570.0, exercicio.getSaldoConta(bbrasil));
		Assert.assertEquals(-55.0, exercicio.getSaldoConta(itau));
	}

}
