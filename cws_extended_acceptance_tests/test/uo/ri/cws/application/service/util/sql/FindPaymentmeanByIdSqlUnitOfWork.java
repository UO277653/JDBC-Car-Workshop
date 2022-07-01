package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import uo.ri.cws.application.persistence.paymentmean.PaymentmeanRecord;

public class FindPaymentmeanByIdSqlUnitOfWork {

	private String id;
	private Optional<PaymentmeanRecord> maybe = Optional.ofNullable(null);
	private ConnectionData connectionData;
	private PreparedStatement findPaymentMeans;
	
	public FindPaymentmeanByIdSqlUnitOfWork(String id) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.id = id;
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			findPaymentMean();			
		});
	}
	
	
	

	public Optional<PaymentmeanRecord> get() {
		return maybe;
	}

	private static final String FIND_PAYMENT_BY_ID =
			"SELECT * FROM TPAYMENTMEANS "
				+ " WHERE ID = ? ";
 

	private void findPaymentMean() throws SQLException {
		PreparedStatement st = findPaymentMeans;
		PaymentmeanRecord record = null;
		int i = 1;
		st.setString(i++, id);

		ResultSet rs = st.executeQuery();
		
		while ( rs.next() ) {
			record = new PaymentmeanRecord();
			record.id = rs.getString("id");
			record.accumulated = rs.getDouble("accumulated");
			record.dtype = rs.getString("dtype");
			record.client_id = rs.getString("client_id");
			record.version = rs.getLong("version");
			}
		maybe = Optional.ofNullable(record);
	}
	
	private void prepareStatements(Connection con) throws SQLException {
		findPaymentMeans = con.prepareStatement(FIND_PAYMENT_BY_ID);
	}


}
