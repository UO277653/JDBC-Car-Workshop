package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.invoice.InvoiceRecord;

public class FindInvoiceByNumberSqlUnitOfWork {


	private Long number;
	private InvoiceRecord result = null;

	private ConnectionData connectionData;
	private PreparedStatement findInvoice;

	public FindInvoiceByNumberSqlUnitOfWork(String id) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.number = Long.parseLong(id);
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			findInvoice();
		});
	}

	public InvoiceRecord get() {
		return result;
	}

	private static final String FIND_BY_NUMBER =
			"SELECT * FROM TINVOICES "
				+ " WHERE number = ?";

	private void findInvoice() throws SQLException {
		PreparedStatement st = findInvoice;

		int i = 1;
		st.setLong(i++, number);

		ResultSet rs = st.executeQuery();
		
		if ( rs.next() ) {
			result = new InvoiceRecord();
			result.id = rs.getString("id");
			result.number = rs.getLong("number");
			result.status = rs.getString("status");
			result.amount = rs.getDouble("amount");
		}
//		return result;
	}

	private void prepareStatements(Connection con) throws SQLException {
		findInvoice = con.prepareStatement(FIND_BY_NUMBER);
	}

	
}
