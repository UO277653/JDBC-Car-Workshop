package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.voucher.VoucherRecord;

public class AddVoucherSqlUnitOfWork {

	
	private ConnectionData connectionData;
	private VoucherRecord dto;

	private PreparedStatement insertIntoVouchers;

	
	public AddVoucherSqlUnitOfWork(VoucherRecord dto) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.dto = dto;
	}

	public VoucherRecord get() {
		return dto;
	}
	
	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			insertVoucher();
		});
	}
	
	private static final String INSERT_INTO_TVOUCHERS =
			"INSERT INTO TVOUCHERS"
				+ " ( ID, CODE, DESCRIPTION, AVAILABLE )"
				+ " VALUES ( ?, ?, ?, ?)";
	
	
	private void insertVoucher() throws SQLException {
		PreparedStatement st = insertIntoVouchers;
		int i = 1;
		st.setString(i++, dto.id);
		st.setString(i++, dto.code);
		st.setString(i++, dto.description);
		st.setDouble(i++, dto.available);
		
		st.executeUpdate();
		
	}
	private void prepareStatements(Connection con) throws SQLException {
		insertIntoVouchers = con.prepareStatement(INSERT_INTO_TVOUCHERS);

	}
}
