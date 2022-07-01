package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import uo.ri.cws.application.service.util.SqlInterventionUtil.InterventionDto;

public class AddInterventionSqlUnitOfWork {

	private InterventionDto dto;
	private ConnectionData connectionData;
	private PreparedStatement insertIntoInterventions;

	public AddInterventionSqlUnitOfWork(InterventionDto dto) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.dto = dto;
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			insertIntervention();
		});
	}

	private static final String INSERT_INTO_TINTERVENTIONS =
			"INSERT INTO TINTERVENTIONS"
				+ " ( ID, DATE, MINUTES, VERSION, MECHANIC_ID, WORKORDER_ID )"
				+ " VALUES ( ?, ?, ?, ?, ?, ?)";

	private void insertIntervention() throws SQLException {
		PreparedStatement st = insertIntoInterventions;
		int i = 1;
		st.setString(i++, dto.id);
		st.setTimestamp(i++, Timestamp.valueOf( dto.date ));
		st.setInt(i++, dto.minutes);
		st.setLong(i++, dto.version);
		st.setString(i++, dto.mechanicId);
		st.setString(i++, dto.workOrderId);

		st.executeUpdate();
	}

	private void prepareStatements(Connection con) throws SQLException {
		insertIntoInterventions = con.prepareStatement(INSERT_INTO_TINTERVENTIONS);
	}

}
