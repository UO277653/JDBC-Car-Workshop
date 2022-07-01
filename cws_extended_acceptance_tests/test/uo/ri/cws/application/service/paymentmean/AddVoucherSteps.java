package uo.ri.cws.application.service.paymentmean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Optional;
import java.util.UUID;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.BusinessFactory;
import uo.ri.cws.application.business.paymentmean.PaymentMeanDto;
import uo.ri.cws.application.business.paymentmean.PaymentmeanCrudService;
import uo.ri.cws.application.business.paymentmean.voucher.VoucherDto;
import uo.ri.cws.application.service.common.TestContext;
//import uo.ri.cws.application.service.util.ClientUtil.ClientDto;
import uo.ri.cws.application.business.client.ClientDto;

import uo.ri.cws.application.service.util.VoucherUtil;

public class AddVoucherSteps {

	private TestContext ctx;

	private VoucherDto voucher;
	private ClientDto client;
	
	private PaymentmeanCrudService service = BusinessFactory.forPaymentMeanCrudService();


	public AddVoucherSteps(TestContext ctx) {
		this.ctx = ctx;
	}
	@Given("a voucher")
	public void aVoucherRegistered() throws BusinessException {
		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
		voucher = new VoucherUtil()
				.get();

	}

	@When("I add the voucher")
	public void iAddTheVoucher() throws BusinessException {
		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
		voucher.clientId = client.id;
		
		service.addVoucherPaymentMean(voucher);
		
	}

	@When("I try to add the voucher to a non existent client")
	public void iTryToAddTheVoucherToANonExistentClient() throws BusinessException {
		voucher.clientId = UUID.randomUUID().toString();

		tryAddAndKeepException ( voucher );

	}
	
	@Then("the voucher results added to the system")
	public void theVoucherResultsAddedToTheSystem() throws BusinessException {

		Optional<PaymentMeanDto> found = service.findById(voucher.id);
		
		assertTrue(found.isPresent());
		assertTrue ( found.get() instanceof VoucherDto );
		VoucherDto voucherFound = (VoucherDto) (found.get());
		assertTrue(voucherFound.clientId.equals(voucher.clientId));
		assertTrue(voucherFound.description.equals(voucher.description));
		assertEquals(voucherFound.balance, voucher.balance, 0.001);
		assertEquals(voucherFound.accumulated, voucher.accumulated, 0.001);
	}

	@When("I try to add another voucher with the same code")
	public void iTryToAddARepeatedCreditCardToTheClient() {
		VoucherDto faulty = new VoucherUtil().get();
		faulty.code = voucher.code;
		faulty.clientId = voucher.clientId;

		tryAddAndKeepException ( faulty );
	}

	@When("I try to add a new voucher with null argument")
	public void iTryToAddANewVoucherWithNullArgument() {
		tryAddAndKeepException ( null );

	}

	@When("I try to add a new voucher with null id")
	public void iTryToAddANewVoucherWithNullId() {
		voucher = new VoucherUtil().get();
		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
		voucher.clientId = client.id;
		voucher.id = null;
		
		tryAddAndKeepException ( voucher );

	}

	@When("I try to add a new voucher with null code")
	public void iTryToAddANewVoucherWithNullNumber() {
		voucher = new VoucherUtil().get();
		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
		voucher.clientId = client.id;
		voucher.code = null;
		
		tryAddAndKeepException ( voucher );
	}

	@When("I try to add a new voucher with null client id")
	public void iTryToAddANewVoucherWithNullClientId() {
		voucher = new VoucherUtil().get();
		voucher.clientId = null;
		
		tryAddAndKeepException ( voucher );
	}

	@When("I try to add a new voucher with {string}, {string}, {string}, {string}, {double}")
	public void iTryToAddANewVoucherWith(String id, String code, String description, String clientid, double balance) {
		voucher = new VoucherUtil().get();
		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
		voucher.id = id;
		voucher.code = code;
		voucher.description = description;
		voucher.clientId = clientid;
		voucher.balance = balance;
		
		tryAddAndKeepException ( voucher );
	}
	
	private void tryAddAndKeepException(VoucherDto dto) {
		try {
			service.addVoucherPaymentMean(voucher);
			fail();
		} catch (BusinessException ex) {
			ctx.setException( ex );		
		} catch (IllegalArgumentException ex) {
			ctx.setException( ex );
		}

	}
	
}
