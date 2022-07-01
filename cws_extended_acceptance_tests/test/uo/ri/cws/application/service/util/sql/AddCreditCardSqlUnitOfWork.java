package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.creditcard.CreditCardRecord;

public class AddCreditCardSqlUnitOfWork {

	private ConnectionData connectionData;
	private CreditCardRecord card;

	private PreparedStatement insertIntoCards;

	
	public AddCreditCardSqlUnitOfWork(CreditCardRecord arg) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.card = arg;
	}

	public CreditCardRecord get() {
		return card;
	}
	
	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			insertVoucher();
		});
	}
	
	private static final String INSERT_INTO_TCARDS =
			"INSERT INTO TCREDITCARDS"
				+ " ( ID, NUMBER, TYPE, VALIDTHRU )"
				+ " VALUES ( ?, ?, ?, ?)";
	
	
	private void insertVoucher() throws SQLException {
		PreparedStatement st = insertIntoCards;
		int i = 1;
		st.setString(i++, card.id);
		st.setString(i++, card.number);
		st.setString(i++, card.type);
		st.setDate(i++, Date.valueOf(card.validthru));
		
		st.executeUpdate();
		
	}
	private void prepareStatements(Connection con) throws SQLException {
		insertIntoCards = con.prepareStatement(INSERT_INTO_TCARDS);

	}
}
