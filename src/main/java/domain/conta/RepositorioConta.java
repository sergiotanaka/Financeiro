package domain.conta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import util.database.ConnectionHolder;

public class RepositorioConta {
	public List<Conta> getAll() {
		try {
			List<Conta> contas = new ArrayList<Conta>();
			
			Statement st = ConnectionHolder.getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT \"NOME\", \"NOME_CONTA_PAI\", \"TIPO_CONTA\" FROM \"CONTA\"");
			while (rs.next()) {
				// 1 == ContaSintetica
				// 2 == ContaAnalitica
				Conta conta = null;
				if (rs.getInt(3) == 1) {
					conta = new ContaSintetica();
				} else if (rs.getInt(3) == 2) {
					conta = new ContaAnalitica();
				}
				conta.setNome(rs.getString(1));
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
}
