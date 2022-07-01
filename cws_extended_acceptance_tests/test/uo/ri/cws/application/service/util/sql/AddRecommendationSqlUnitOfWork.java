package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.util.RecommendationUtil.RecommendationDto;

public class AddRecommendationSqlUnitOfWork {

	private ConnectionData connectionData;
	private RecommendationDto dto;

	private PreparedStatement insertIntoRecommendations;

	
	public AddRecommendationSqlUnitOfWork(RecommendationDto dto) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.dto = dto;
	}

	public RecommendationDto get() {
		return dto;
	}
	
	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			insertRecommendation();
		});
	}
	
	private static final String INSERT_INTO_TRECOMMENDATION =
			"INSERT INTO TRECOMMENDATIONS"
				+ " ( ID, VERSION, SPONSOR_ID, RECOMMENDED_ID, USEDFORVOUCHER )"
				+ " VALUES ( ?, ?, ?, ?, ? )";
	
	
	private void insertRecommendation() throws SQLException {
		PreparedStatement st = insertIntoRecommendations;
		int i = 1;
		st.setString(i++, dto.id);
		st.setLong(i++, dto.version);
		st.setString(i++, dto.sponsor_id);
		st.setString(i++, dto.recommended_id);
		st.setBoolean(i++, dto.usedForVoucher);
		
		st.executeUpdate();
		
	}
	private void prepareStatements(Connection con) throws SQLException {
		insertIntoRecommendations = con.prepareStatement(INSERT_INTO_TRECOMMENDATION);

	}
}
