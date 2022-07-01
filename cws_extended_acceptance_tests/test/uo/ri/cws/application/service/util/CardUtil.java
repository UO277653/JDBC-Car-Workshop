package uo.ri.cws.application.service.util;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import uo.ri.cws.application.business.paymentmean.CardDto;
import uo.ri.cws.application.persistence.creditcard.CreditCardRecord;
import uo.ri.cws.application.service.util.sql.FindCardFromNumberSqlUnitOfWork;

public class CardUtil {

	
	private CardDto dto = createDefaultCard();

	
	public CardDto get() {
		return dto;
	}

	private CardDto createDefaultCard() {
		CardDto res = new CardDto();
		res.id = UUID.randomUUID().toString();
		res.version = 1L;
		res.accumulated = 0.0;
		res.cardNumber = UUID.randomUUID().toString();
		res.cardType = "VISA";
		res.cardExpiration = LocalDate.of(2022, 12, 30);
		return res;
	}
	
	public Optional<CreditCardRecord> findCardFromNumber(String cardNumber) {
		FindCardFromNumberSqlUnitOfWork finder = new FindCardFromNumberSqlUnitOfWork(cardNumber);
		finder.execute();
		return finder.get();
	}
}
