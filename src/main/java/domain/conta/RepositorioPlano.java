package domain.conta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import util.database.ConnectionHolder;

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
	public void updatePlano(PlanoDeContas plano) {
		// 1. atualiza ou insere a conta.
		// 2. verificar se alguma conta foi excluída.

	}

	private void buildArvoreDaConta(ContaSintetica conta) {
		List<Conta> subcontas = getContasPorPai(conta.getNome());
		for (Conta subconta : subcontas) {
			if (subconta instanceof ContaSintetica) {
				conta.addSubConta(subconta);
				buildArvoreDaConta((ContaSintetica) subconta);
			} else if (subconta instanceof ContaAnalitica) {
				conta.addSubConta(subconta);
			}
		}
	}

	private List<Conta> getContasPorPai(String idContaPai) {
		try {
			List<Conta> contas = new ArrayList<Conta>();

			Statement st = ConnectionHolder.getConnection().createStatement();
			ResultSet rs = st
					.executeQuery("SELECT oid, \"NOME\", \"NOME_CONTA_PAI\", \"TIPO_CONTA\" FROM \"CONTA\" WHERE \"NOME_CONTA_PAI\" LIKE '"
							+ idContaPai + "'");
			while (rs.next()) {
				// 1 == ContaSintetica
				// 2 == ContaAnalitica
				Conta conta = null;
				if (rs.getInt(4) == 1) {
					conta = new ContaSintetica();
				} else if (rs.getInt(4) == 2) {
					conta = new ContaAnalitica();
				}
				conta.setOid(rs.getLong(1));
				conta.setNome(rs.getString(2));
				contas.add(conta);
			}
			rs.close();
			st.close();

			return contas;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void updateConta(Conta conta) {
		try {
			// 1. atualiza ou insere a conta.

			int foovalue = 500;
			PreparedStatement st = ConnectionHolder
					.getConnection()
					.prepareStatement(
							"UPDATE \"CONTA\" SET \"NOME\" = ?, \"NOME_CONTA_PAI\" = ? WHERE oid = ?");
			st.setInt(1, foovalue);
			int rowsDeleted = st.executeUpdate();
			System.out.println(rowsDeleted + " rows deleted");
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
