package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.invoice.InvoiceRecord;

public class AddInvoiceSqlUnitOfWork {


	private InvoiceRecord record;
	private ConnectionData connectionData;

	private PreparedStatement insertIntoInvoices;


	public AddInvoiceSqlUnitOfWork(InvoiceRecord arg) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.record = arg;
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			insertInvoice();
		});
	}
	
	private static final String INSERT_INTO_TINVOICES =
			"INSERT INTO TINVOICES"
				+ " ( ID, VERSION, NUMBER, AMOUNT, VAT, DATE, STATUS, USEDFORVOUCHER )"
				+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";

	private void insertInvoice() throws SQLException {
		
		PreparedStatement st = insertIntoInvoices;
		int i = 1;
		
		st.setString(i++, record.id);
		st.setLong(i++, record.version);
		st.setLong(i++, record.number);
		st.setDouble(i++, record.amount);
		st.setDouble(i++, record.vat);
		st.setDate(i++, Date.valueOf(record.date) );
		st.setString(i++, record.status.toString());
		st.setBoolean(i++, record.usedforvoucher);
		st.executeUpdate();
		
	}

	private void prepareStatements(Connection con) throws SQLException {
		insertIntoInvoices = con.prepareStatement(INSERT_INTO_TINVOICES);

	}
	
}
