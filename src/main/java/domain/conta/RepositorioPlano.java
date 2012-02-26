package domain.conta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import util.database.ConnectionHolder;
import util.database.OidGenerator;

/**
 * TODO extrair DAOs;<br>
 * TODO colocar pre/pos/invariante?
 */
public class RepositorioPlano {
	/**
	 * Recupera o plano de conta.
	 * 
	 * @return {@link PlanoDeContas}.
	 */
	public PlanoDeContas retrievePlano() {
		PlanoDeContas plano = new PlanoDeContas();

		buildArvoreDaConta(plano.getAtivo());
		buildArvoreDaConta(plano.getPassivo());
		buildArvoreDaConta(plano.getCapital());
		buildArvoreDaConta(plano.getCustosDespesas());
		buildArvoreDaConta(plano.getReceitas());

		return plano;
	}

	/**
	 * Persiste as contas e sua hierarquia.<br>
	 * 
	 * @param plano
	 *            {@link PlanoDeContas}.
	 */
	public void storePlano(PlanoDeContas plano) {
		// 1. atualiza ou insere a conta.
		for (Conta conta : plano.getTodasContas()) {
			updateOrCreateConta(conta);
		}

		// 2. verificar se alguma conta foi excluída.

	}

	private void buildArvoreDaConta(ContaSintetica conta) {
		List<Conta> subcontas = getContasPorPai(conta);
		for (Conta subconta : subcontas) {
			if (subconta instanceof ContaSintetica) {
				conta.addSubConta(subconta);
				buildArvoreDaConta((ContaSintetica) subconta);
			} else if (subconta instanceof ContaAnalitica) {
				conta.addSubConta(subconta);
			}
		}
	}

	private List<Conta> getContasPorPai(ContaSintetica contaPai) {
		try {
			List<Conta> subContas = new ArrayList<Conta>();

			Statement st = ConnectionHolder.getConnection().createStatement();
			ResultSet rs = st
					.executeQuery("SELECT oid_conta, nome, tipo_conta FROM conta WHERE oid_conta_pai = "
							+ contaPai.getOid());
			while (rs.next()) {
				// 1 == ContaSintetica
				// 2 == ContaAnalitica
				Conta subConta = null;
				if (rs.getInt(3) == 1) {
					subConta = new ContaSintetica();
				} else if (rs.getInt(3) == 2) {
					subConta = new ContaAnalitica();
				}
				subConta.setOid(rs.getLong(1));
				subConta.setNome(rs.getString(2));

				subContas.add(subConta);
			}
			rs.close();
			st.close();

			return subContas;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Premissa: conta com OID = 0 é uma conta nova.
	 * 
	 * @param conta
	 */
	private void updateOrCreateConta(Conta conta) {
		// 1. atualiza ou insere a conta.
		if (conta.getOid() == 0) {
			createConta(conta);
		} else {
			updateConta(conta);
		}
	}

	private void updateConta(Conta conta) {
		try {
			// 2. preparar o statement
			PreparedStatement st = ConnectionHolder.getConnection()
					.prepareStatement(
							"update conta set nome = ?,  oid_conta_pai = ?, tipo_conta =? "
									+ " where oid_conta = ?");
			st.setString(1, conta.getNome());
			st.setLong(2, conta.getContaPai().getOid());
			st.setInt(3, conta instanceof ContaSintetica ? 1 : 2);
			st.setLong(4, conta.getOid());

			// 3. executar
			st.executeUpdate();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createConta(Conta conta) {
		// 1. obter o proximo OID
		conta.setOid(OidGenerator.getNextOid());

		try {
			// 2. preparar o statement
			PreparedStatement st = ConnectionHolder.getConnection()
					.prepareStatement(
							"insert into conta(oid_conta, nome, oid_conta_pai, tipo_conta) "
									+ " values(?, ?, ?, ?)");
			st.setLong(1, conta.getOid());
			st.setString(2, conta.getNome());
			st.setLong(3, conta.getContaPai().getOid());
			st.setInt(4, conta instanceof ContaSintetica ? 1 : 2);

			// 3. executar
			st.executeUpdate();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
