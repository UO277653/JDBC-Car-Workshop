package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.paymentmean.PaymentmeanRecord;

public class AddPaymentMeanSqlUnitOfWork {



	private PaymentmeanRecord dto;
	private ConnectionData connectionData;
	private PreparedStatement insertIntoPaymentMeans;

	public AddPaymentMeanSqlUnitOfWork(PaymentmeanRecord payRecord) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.dto = payRecord;
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			insertPaymentMean();
		});
	}

	private static final String INSERT_INTO_TPAYMENTMEANSS =
			"INSERT INTO TPAYMENTMEANS"
				+ " ( ID, VERSION, DTYPE, ACCUMULATED, CLIENT_ID )"
				+ " VALUES ( ?, ?, ?, ?, ?)";

	private void insertPaymentMean() throws SQLException {
		PreparedStatement st = insertIntoPaymentMeans;
		int i = 1;
		st.setString(i++, dto.id);
		st.setLong(i++, dto.version);

		st.setString(i++, dto.dtype.toString());
		st.setDouble(i++, dto.accumulated);
		st.setString(i++, dto.client_id);

		st.executeUpdate();
	}

	private void prepareStatements(Connection con) throws SQLException {
		insertIntoPaymentMeans = con.prepareStatement(INSERT_INTO_TPAYMENTMEANSS);
	}
}
