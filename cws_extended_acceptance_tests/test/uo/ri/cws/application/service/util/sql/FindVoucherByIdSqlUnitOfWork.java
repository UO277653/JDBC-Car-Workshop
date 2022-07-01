package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.business.paymentmean.voucher.VoucherDto;

public class FindVoucherByIdSqlUnitOfWork {

	private ConnectionData connectionData;
	private PreparedStatement findVouchers;
	private VoucherDto result = null;
	private String id ;
	
	public FindVoucherByIdSqlUnitOfWork(String id ) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.id = id;
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			findVouchers();
		});
	}

	public VoucherDto get() {
		return result;
	}

	private static final String FIND_VOUCHERS =
			"SELECT * FROM TVOUCHERS where Id = ? ";

	private void findVouchers() throws SQLException {
		PreparedStatement st = findVouchers;
		st.setString(1, this.id);
		ResultSet rs = st.executeQuery();
		
		if ( rs.next() ) {
			result = new VoucherDto();
			result.id = rs.getString("id");
			result.balance = rs.getDouble("available");
			result.description = rs.getString("description");
		}
	}

	private void prepareStatements(Connection con) throws SQLException {
		findVouchers = con.prepareStatement(FIND_VOUCHERS);
	}

	
	
}
