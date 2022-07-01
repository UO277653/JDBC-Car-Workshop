package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.business.invoice.ChargeDto;

public class AddChargeSqlUnitOfWork {

	private ChargeDto dto;
//	private CashDto cash;
	private ConnectionData connectionData;
	private PreparedStatement insertIntoCharges;


	public AddChargeSqlUnitOfWork(ChargeDto dto) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.dto = dto;
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			insertCharge();

		});
	}
	
	private static final String INSERT_INTO_TCHARGE =
			"INSERT INTO TCHARGES"
				+ " ( ID, AMOUNT, INVOICE_ID, PAYMENTMEAN_ID, VERSION )"
				+ " VALUES ( ?, ?, ?, ?, ?)";

	private void insertCharge() throws SQLException {
		PreparedStatement st = insertIntoCharges;
		int i = 1;
		st.setString(i++, dto.id);
		st.setDouble(i++, dto.amount);
		st.setString(i++, dto.invoice_id);
		st.setString(i++, dto.paymentMean_id);
		st.setLong(i++, dto.version);

		st.executeUpdate();
	}
	


	private void prepareStatements(Connection con) throws SQLException {
		insertIntoCharges = con.prepareStatement(INSERT_INTO_TCHARGE);

	}

}
