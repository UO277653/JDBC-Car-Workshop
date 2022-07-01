package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.paymentmean.PaymentmeanRecord;

public class FindPaymentmeanByClientIdSqlUnitOfWork {

	private String client;
	private List<PaymentmeanRecord> maybe = new ArrayList<PaymentmeanRecord>();
	private ConnectionData connectionData;
	private PreparedStatement findPaymentMeans;
	
	public FindPaymentmeanByClientIdSqlUnitOfWork(String id) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.client = id;
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			findPaymentMean();	
		});
	}
	

	public List<PaymentmeanRecord> get() {
		return maybe;
	}

	private static final String FIND_PAYMENT_BY_CLIENT_ID =
			"SELECT * FROM TPAYMENTMEANS "
				+ " WHERE CLIENT_ID = ? ";
 

	private void findPaymentMean() throws SQLException {
		PreparedStatement st = findPaymentMeans;
		PaymentmeanRecord record = null;
		int i = 1;
		st.setString(i++, client);

		ResultSet rs = st.executeQuery();
		
		while ( rs.next() ) {
			record = new PaymentmeanRecord();
			record.id = rs.getString("id");
			record.accumulated = rs.getDouble("accumulated");
			record.dtype = rs.getString("dtype");
			record.client_id = rs.getString("client_id");
			record.version = rs.getLong("version");
			maybe.add(record);
			}
	}
	


	private void prepareStatements(Connection con) throws SQLException {
		findPaymentMeans = con.prepareStatement(FIND_PAYMENT_BY_CLIENT_ID);
	}


}
