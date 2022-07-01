package uo.ri.cws.application.service.invoice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.BusinessFactory;
import uo.ri.cws.application.business.client.ClientDto;
import uo.ri.cws.application.business.invoice.ChargeDto;
import uo.ri.cws.application.business.invoice.InvoicingService;
import uo.ri.cws.application.persistence.creditcard.CreditCardRecord;
import uo.ri.cws.application.persistence.invoice.InvoiceRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentmeanRecord;
import uo.ri.cws.application.persistence.voucher.VoucherRecord;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.util.CardUtil;
import uo.ri.cws.application.service.util.ChargeUtil;
import uo.ri.cws.application.service.util.InvoiceUtil;
import uo.ri.cws.application.service.util.PaymentMeansUtil;
import uo.ri.cws.application.service.util.VoucherUtil;
import uo.ri.cws.application.service.util.sql.AddCreditCardSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.AddVoucherSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindCardFromNumberSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindCashSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindVoucherByCodeSqlUnitOfWork;

public class SettleSteps {

	private TestContext ctx;
	private ClientDto theClient; // client registered
	private InvoiceRecord theInvoice; // invoice registered

	private InvoicingService service = BusinessFactory.forInvoicingService();
	private List<ChargeDto> theCharges = new ArrayList<ChargeDto>();

	@SuppressWarnings("unchecked")
	public SettleSteps(TestContext ctx) {
		this.ctx = ctx;
		theClient = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
		theInvoice = ((List<InvoiceRecord>) ctx.get(TestContext.Key.INVOICES)).get(0);
	}

	@Given("the following charges for the invoice")
	public void theFollowingCharges(DataTable data) {
		// # | type | invoice_id | amount |
		List<Map<String, String>> table = data.asMaps();
		for (Map<String, String> row : table) {
			ChargeDto charge = new ChargeDto();
			charge.invoice_id = theInvoice.id;
			charge.amount = Double.parseDouble(row.get("amount"));
			charge.paymentMean_id = getPaymentId(row);
			theCharges.add(charge);
		}
	}

	@Given("the following credit cards")
	public void theFollowingCreditCards(DataTable data) {
//	    | number | type |   validthru |
		List<Map<String, String>> table = data.asMaps();
		CreditCardRecord record;

		for (Map<String, String> row : table) {
			PaymentmeanRecord pmr = new PaymentMeansUtil().withClient(theClient.id).withType("CREDITCARD").register().get();
			record = new CreditCardRecord();
			record.id = pmr.id;
			record.number = row.get("number");
			record.type = row.get("type");
			record.validthru = LocalDate.parse(row.get("validthru"));
			new AddCreditCardSqlUnitOfWork(record).execute();
		}

	}

	private String getPaymentId(Map<String, String> row) {
		switch (row.get("type")) {
		case "CASH":
			return new PaymentMeansUtil().findCashForClient(theClient.id).get().id;

		case "CREDITCARD":
			return new CardUtil().findCardFromNumber(row.get("payident")).get().id;

		case "VOUCHER":
			return new VoucherUtil().findByCode(row.get("payident")).get().id;

		}
		return null;
	}

	@When("I settle the invoice")
	public void iSettleTheInvoice() throws BusinessException {
		service.settleInvoice(this.theInvoice.id, theCharges);
	}

	@When("I try to settle the invoice")
	public void iTryToSettleTheInvoice() {
		trySettleAndKeepException(this.theInvoice.id, theCharges);
	}

	private void trySettleAndKeepException(String id, List<ChargeDto> charges) {
		try {
			service.settleInvoice(id, charges);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}
	}

	@Then("the invoice is PAID")
	public void theInvoiceIsPAID() {
		InvoiceRecord invoice = new InvoiceUtil().find(theInvoice.id).get();
		assertTrue(invoice.status.equals("PAID"));
	}

	@Then("payment means are updated")
	public void paymentMeansAreUpdated() {

	}

	@Then("the following charges are created")
	public void theFollowingChargesAreCreated(DataTable data) {
//	    | paymentMean | invoiceNumber | amount |
		List<ChargeDto> chargesInDB = new ChargeUtil().findAll();

		List<Map<String, String>> table = data.asMaps();
		for (Map<String, String> row : table) {
			String invoiceid = new InvoiceUtil().findByNumber(row.get("invoiceNumber")).get().id;
			String paymentMeanId = getPaymentFromDB(row);

			Optional<ChargeDto> maybe = chargesInDB.stream().filter(ch -> ch.invoice_id.equals(invoiceid))
					.filter(ch -> ch.paymentMean_id.equals(paymentMeanId)).findFirst();
			assertTrue(maybe.isPresent());
			assertEquals(maybe.get().amount, Double.parseDouble(row.get("amount")), 0.01);
		}

	}

	private String getPaymentFromDB(Map<String, String> row) {
		String type = row.get("paymentMean");
		if (type.equals("CASH")) {
			FindCashSqlUnitOfWork finder = new FindCashSqlUnitOfWork(theClient.id);
			finder.execute();
			return finder.get().get().id;
		}
		if (type.equals("CREDITCARD")) {
			FindCardFromNumberSqlUnitOfWork finder = new FindCardFromNumberSqlUnitOfWork(row.get("payident"));
			finder.execute();
			return finder.get().get().id;
		}

		if (type.equals("VOUCHER")) {
			FindVoucherByCodeSqlUnitOfWork finder = new FindVoucherByCodeSqlUnitOfWork(row.get("payident"));
			finder.execute();
			return finder.get().id;
		}

		return null;
	}

	@Given("the following vouchers")
	public void theFollowingVouchers(DataTable data) {
//    | code | money | description |
		List<Map<String, String>> table = data.asMaps();
		VoucherRecord record;

		for (Map<String, String> row : table) {
			PaymentmeanRecord pmr = new PaymentMeansUtil().withClient(theClient.id).withType("VOUCHER").register()
					.get();
			record = new VoucherRecord();
			record.id = pmr.id;
			record.code = row.get("code");
			record.description = row.get("description");
			record.available = Double.parseDouble(row.get("money"));
			new AddVoucherSqlUnitOfWork(record).execute();
		}

	}

}
