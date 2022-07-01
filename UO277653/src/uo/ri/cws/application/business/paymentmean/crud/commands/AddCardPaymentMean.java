package uo.ri.cws.application.business.paymentmean.crud.commands;

import java.time.LocalDate; 

import alb.util.assertion.Argument;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.paymentmean.CardDto;
import uo.ri.cws.application.business.util.DtoAssembler;
import uo.ri.cws.application.business.util.command.Command;
import uo.ri.cws.application.persistence.PersistenceFactory;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.creditcard.CreditCardGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;

public class AddCardPaymentMean implements Command<Void>{

	private CardDto card;
	private ClientGateway cg = PersistenceFactory.forClient();
	private PaymentMeanGateway pmg = PersistenceFactory.forPaymentmean();
	private CreditCardGateway ccg = PersistenceFactory.forCreditCard();
	
	
	public AddCardPaymentMean(CardDto card) {
		
		Argument.isNotNull(card);
		Argument.isNotNull(card.id);
		Argument.isNotNull(card.cardExpiration);
		Argument.isNotNull(card.cardNumber);
		Argument.isNotNull(card.cardType);
		Argument.isNotNull(card.clientId);
		Argument.isNotEmpty(card.id);
		Argument.isNotEmpty(card.cardNumber);
		Argument.isNotEmpty(card.cardType);
		Argument.isNotEmpty(card.clientId);
		
		this.card = card;
	}

	@Override
	public Void execute() throws BusinessException {
		
		if(card.cardExpiration.isBefore(LocalDate.now())) {
			throw new BusinessException("The card is expired");
		}
		
		if(!cg.findById(card.clientId).isPresent()) {
			throw new BusinessException("Client does not exist");
		}
		
		if(ccg.findByNumber(card.cardNumber).isPresent()) {
			throw new BusinessException("A card with the same number already exists");
		}
		
		pmg.add(DtoAssembler.toRecord(card));
		ccg.add(DtoAssembler.toRecord(card));
		
		
		return null;
	}
}
