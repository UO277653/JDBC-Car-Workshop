package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import uo.ri.cws.application.persistence.creditcard.CreditCardRecord;

public class FindCardFromNumberSqlUnitOfWork {

	private Optional<CreditCardRecord> creditcard;
	private String number;
	private ConnectionData connectionData;
	private PreparedStatement findCreditCard;


	private static final String FIND_BY_NUMBER =
			"SELECT * FROM TCREDITCARDS "
				+ " WHERE NUMBER = ?";
	
	public FindCardFromNumberSqlUnitOfWork(String cardNumber) {
		this.connectionData = PersistenceXmlScanner.scan();

		this.number = cardNumber;
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			find();
		});
	}

	private void find() throws SQLException {
		PreparedStatement st = findCreditCard;

		int i = 1;
		st.setString(i++, number);

		ResultSet rs = st.executeQuery();
		CreditCardRecord result = null;
		if ( rs.next() ) {
			result= new CreditCardRecord();
			result.id = rs.getString("id");
			result.type = rs.getString("type");
			result.number = rs.getString("number");
			result.validthru = rs.getDate("validthru").toLocalDate();
		}
		this.creditcard = Optional.ofNullable(result);
	}
	private void prepareStatements(Connection con) throws SQLException {
		findCreditCard = con.prepareStatement(FIND_BY_NUMBER);
	}

	public Optional<CreditCardRecord> get() {
		
		return this.creditcard;
	}

}
