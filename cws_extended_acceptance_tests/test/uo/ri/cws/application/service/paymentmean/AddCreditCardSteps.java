package uo.ri.cws.application.service.paymentmean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.BusinessFactory;
import uo.ri.cws.application.business.paymentmean.CardDto;
import uo.ri.cws.application.business.paymentmean.PaymentMeanDto;
import uo.ri.cws.application.business.paymentmean.PaymentmeanCrudService;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.util.CardUtil;
//import uo.ri.cws.application.service.util.ClientUtil.ClientDto;
import uo.ri.cws.application.business.client.ClientDto;


public class AddCreditCardSteps {

	private TestContext ctx;

	private CardDto card, faultyCard;
	private ClientDto client;
	
	private PaymentmeanCrudService service = BusinessFactory.forPaymentMeanCrudService();

	public AddCreditCardSteps(TestContext ctx) {
		this.ctx = ctx;
		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);

	}

	@Given("a credit card")
	public void aCreditCard() {

		card = new CardUtil().get();
	}

	@When("I add the credit card")
	public void iAddTheCreditCard() throws BusinessException {
//		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
		card.clientId = client.id;
		
		service.addCardPaymentMean(card);
		
	}

	@When("I try to add the credit card to a non existent client")
	public void iTryToAddTheCreditCardToANonExistentClient() throws BusinessException {
		card.clientId = UUID.randomUUID().toString();

		tryAddAndKeepException ( card );

	}
	
	@Then("the credit card results added to the system")
	public void theCreditCardResultsAddedToTheSystem() throws BusinessException {

		Optional<PaymentMeanDto> found = service.findById(card.id);//.f.findAllCreditCards();//.findPaymentMeansByClientId(client.id);
//		Optional<CardDto> found = addedCard.stream().filter(pm -> pm.id.equals(card.id)).findFirst();
		
		assertTrue(found.isPresent());
		CardDto cardFound = (CardDto) (found.get());
		assertTrue(cardFound.clientId.equals(card.clientId));
		assertTrue(cardFound.cardExpiration.equals(card.cardExpiration));
		assertTrue(cardFound.cardNumber.equals(card.cardNumber));
		assertEquals(cardFound.accumulated, card.accumulated, 0.001);
	}

	
//	@Given("a credit card with repeated number")
//	public void aCreditCardWithARepeatedNumber() {
//		faultyCard = new SqlCardUtil().get();
//		faultyCard.cardNumber = card.cardNumber;
//		faultyCard.clientId = card.clientId;
//
//	}


	@When("I try to add another credit card with the same number")
	public void iTryToAddARepeatedCreditCardToTheClient() {
		faultyCard = new CardUtil().get();
		faultyCard.cardNumber = card.cardNumber;
		faultyCard.clientId = card.clientId;

		tryAddAndKeepException ( faultyCard );
	}
	
	@When("I try to add the credit card to the client")
	public void iTryToAddTheCreditCardToTheClient() {
//		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
		card.clientId = client.id;
		
		tryAddAndKeepException ( card );

	}
	
	@Given("an outdated credit card")
	public void anOutdatedCreditCard() {
		card = new CardUtil().get();
		card.cardExpiration  
	            = LocalDate.parse("2018-12-27");
	}
	
	@When("I try to add a new credit card with null argument")
	public void iTryToAddANewCreditCardWithNullArgument() {
		tryAddAndKeepException ( null );

	}

	@When("I try to add a new credit card with null id")
	public void iTryToAddANewCreditCardWithNullId() {
		card = new CardUtil().get();
//		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
		card.clientId = client.id;
		card.id = null;
		
		tryAddAndKeepException ( card );

	}

	@When("I try to add a new credit card with null number")
	public void iTryToAddANewCreditCardWithNullNumber() {
		card = new CardUtil().get();
//		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
		card.clientId = client.id;
		card.cardNumber = null;
		
		tryAddAndKeepException ( card );
	}
	
	
	@When("I try to add a new credit card with null type")
	public void iTryToAddANewCreditCardWithNullType() {
		card = new CardUtil().get();
//		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
		card.clientId = client.id;
		card.cardType = null;
		
		tryAddAndKeepException ( card );
	}

	@When("I try to add a new credit card with null client id")
	public void iTryToAddANewCreditCardWithNullClientId() {
		card = new CardUtil().get();
		card.clientId = null;
		
		tryAddAndKeepException ( card );
	}
	
	@When("I try to add a new credit card with null expiration date")
	public void iTryToAddANewCreditCardWithNullExpirationDate() {
		card = new CardUtil().get();
//		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
		card.clientId = client.id;
		card.cardExpiration = null;
		
		tryAddAndKeepException ( card );
	}
	
	@When("I try to add a new credit card with {string}, {string}, {string}, {string}")
	public void iTryToAddANewCreditCardWithClientid(String id, String number, String type, String clientId) {
		card = new CardUtil().get();
//		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
		card.id = id;
		card.cardNumber = number;
		card.cardType = type;
		card.clientId = clientId;
		
		tryAddAndKeepException ( card );
	}
	
	private void tryAddAndKeepException(CardDto dto) {
		try {
			service.addCardPaymentMean(dto);
			fail();
		} catch (BusinessException ex) {
			ctx.setException( ex );		
		} catch (IllegalArgumentException ex) {
			ctx.setException( ex );
		}

	}
	
}
