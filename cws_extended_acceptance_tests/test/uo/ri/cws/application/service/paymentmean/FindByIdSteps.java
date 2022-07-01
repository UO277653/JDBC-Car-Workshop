package uo.ri.cws.application.service.paymentmean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Optional;
import java.util.UUID;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.BusinessFactory;
import uo.ri.cws.application.business.paymentmean.CardDto;
import uo.ri.cws.application.business.paymentmean.PaymentMeanDto;
import uo.ri.cws.application.business.paymentmean.PaymentmeanCrudService;
import uo.ri.cws.application.business.paymentmean.voucher.VoucherDto;
import uo.ri.cws.application.service.common.TestContext;

public class FindByIdSteps {

	private TestContext ctx;
	private PaymentmeanCrudService service = BusinessFactory.forPaymentMeanCrudService();

	private PaymentMeanDto dto;
	private Optional<PaymentMeanDto> found;

	public FindByIdSteps(TestContext ctx) {
		this.ctx = ctx;
	}

	@When("I search payment mean by id")
	public void iSearchPaymentMeanById() throws BusinessException {
		dto = (PaymentMeanDto) ctx.get(TestContext.Key.PAYMENTMEAN);

		found = service.findById(dto.id);

	}

	@Then("I get the credit card")
	public void iGetTheCreditCard() throws BusinessException {
		assertTrue(found.isPresent());
		assertTrue(found.get() instanceof CardDto);
		CardDto foundDto = (CardDto) found.get();
		CardDto compare = (CardDto) dto;

		assertEquals(foundDto.id, compare.id);
		assertEquals(foundDto.clientId, compare.clientId);
		assertEquals(foundDto.cardExpiration, compare.cardExpiration);
		assertEquals(foundDto.cardNumber, compare.cardNumber);
		assertEquals(foundDto.cardType, compare.cardType);
		assertEquals(foundDto.accumulated, compare.accumulated);

	}

	@Then("I get the voucher")
	public void iGetTheVoucher() throws BusinessException {
		assertTrue(found.isPresent());
		assertTrue(found.get() instanceof VoucherDto);
		VoucherDto foundDto = (VoucherDto) found.get();
		VoucherDto compare = (VoucherDto) dto;

		assertEquals(foundDto.id, compare.id);
		assertEquals(foundDto.clientId, compare.clientId);
		assertEquals(foundDto.code, compare.code);
		assertEquals(foundDto.description, compare.description);
		assertEquals(foundDto.accumulated, compare.accumulated, 0.01);
		assertEquals(foundDto.balance, compare.balance, 0.01);

	}

	@When("I search a non existent payment mean")
	public void iSearchANonExistentPaymentMean() throws BusinessException {

		found = service.findById(UUID.randomUUID().toString());

	}

	@Then("payment mean not found")
	public void paymentMeanNotFound() {
		assertTrue(found.isEmpty());
	}

	@When("I try to find a payment mean with null argument")
	public void iTryToFindAPaymentMeanWithNullArgument() throws BusinessException {
		tryFindAndKeepException(null);

	}

	@When("I look for a payment mean by wrong id {string}")
	public void iLookForAPaymentMeanByWrongId(String arg) {
		tryFindAndKeepException(arg);

	}

	private void tryFindAndKeepException(String id) {
		try {
			service.findById(id);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}
	}
}
