package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import uo.ri.cws.application.business.paymentmean.CashDto;

public class FindCashSqlUnitOfWork {

	private String clientId;
	private CashDto maybe = null;
	private ConnectionData connectionData;
	private PreparedStatement findPaymentMeans;
	private PreparedStatement findCash;

	public FindCashSqlUnitOfWork(String id) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.clientId = id;
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			findPaymentMean();
			findCash();
			
		});
	}

	public Optional<CashDto> get() {
		return Optional.ofNullable(maybe);
	}

	private static final String FIND_PAYMENT_BY_CLIENTID =
			"SELECT * FROM TPAYMENTMEANS "
				+ " WHERE CLIENT_ID = ? AND DTYPE = 'CASH'";
 
	private static final String FIND_CASH_BY_CLIENTID = 
			"SELECT * FROM TCASHES "
					+ " WHERE ID = ?";

	private void findPaymentMean() throws SQLException {
		PreparedStatement st = findPaymentMeans;

		int i = 1;
		st.setString(i++, clientId);

		ResultSet rs = st.executeQuery();
		
		if ( rs.next() ) {
			maybe = new CashDto();
			maybe.id = rs.getString("id");
			maybe.clientId = rs.getString("client_id");
			maybe.accumulated = rs.getDouble("accumulated");
		}
	}
	
	private void findCash() throws SQLException {
		PreparedStatement st = findCash;
		ResultSet rs = null;
		
		if (this.maybe != null) {
			int i = 1;
			st.setString(i++, maybe.id);

			rs = st.executeQuery();			
		}
		if ( !rs.next() ) {
			maybe = null;
		}
	}

	private void prepareStatements(Connection con) throws SQLException {
		findPaymentMeans = con.prepareStatement(FIND_PAYMENT_BY_CLIENTID);
		findCash = con.prepareStatement(FIND_CASH_BY_CLIENTID);
	}

	
	
}
