package util.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gerador de novos OIDs.
 * 
 * TODO colocar mais garantias.
 */
public class OidGenerator {
	public static long getNextOid() {
		try {
			long nextOid = -1;
			Statement st = ConnectionHolder.getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT get_next_oid()");
			if (rs.next()) {
				nextOid = rs.getLong(1);
			}
			rs.close();
			st.close();

			return nextOid;

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
