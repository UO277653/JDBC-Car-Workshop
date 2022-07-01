package uo.ri.cws.application.service.paymentmean;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.BusinessFactory;
import uo.ri.cws.application.business.client.ClientDto;
import uo.ri.cws.application.business.invoice.InvoiceDto;
import uo.ri.cws.application.business.invoice.InvoicingService;
import uo.ri.cws.application.business.paymentmean.CardDto;
import uo.ri.cws.application.business.paymentmean.CashDto;
import uo.ri.cws.application.business.paymentmean.PaymentMeanDto;
import uo.ri.cws.application.business.paymentmean.PaymentmeanCrudService;
import uo.ri.cws.application.business.paymentmean.voucher.VoucherDto;
import uo.ri.cws.application.business.workorder.WorkOrderDto;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.util.ChargeUtil;
import uo.ri.cws.application.service.util.CardUtil;
//import uo.ri.cws.application.service.util.ClientUtil.ClientDto;
import uo.ri.cws.application.service.util.VoucherUtil;

public class DeletePaymentMeanSteps {

	private TestContext ctx;

	private CardDto card;//, faultyCard;
	private VoucherDto voucher;
	private ClientDto client;
	private String thePaymentId ;
	private InvoiceDto invoice = null;
	
	private PaymentmeanCrudService service = BusinessFactory.forPaymentMeanCrudService();

	public DeletePaymentMeanSteps(TestContext ctx) {
		this.ctx = ctx;
	}
	
	
	@Given("a credit card registered")
	public void aClientWithARegisteredCreditCard() throws BusinessException {
		card = new CardUtil().get();
		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
		card.clientId = client.id;
		service.addCardPaymentMean(card);
		thePaymentId = card.id;
		ctx.put(TestContext.Key.PAYMENTMEAN, card);
		@SuppressWarnings("unchecked")
		List<PaymentMeanDto> aux = (List<PaymentMeanDto>) ctx.getResultList();
		aux.add(card);
		ctx.setResultList(aux);

	}
	
	@Given("a voucher registered")
	public void aVoucherRegistered() throws BusinessException {
		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
		voucher = new VoucherUtil()
				.withClient(client.id)
				.get();

		service.addVoucherPaymentMean(voucher);
		thePaymentId = voucher.id;
		ctx.put(TestContext.Key.PAYMENTMEAN, voucher);
		@SuppressWarnings("unchecked")
		List<PaymentMeanDto> aux = (List<PaymentMeanDto>) ctx.getResultList();
		aux.add(voucher);
		ctx.setResultList(aux);
	}



	@When("I try to remove the cash payment mean")
	public void iTryToRemoveTheCashPaymentMean() throws BusinessException {
		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);

		List<PaymentMeanDto> all = service.findPaymentMeansByClientId(client.id);
		CashDto cashdto = (CashDto) all.stream().filter(pm -> pm instanceof CashDto).findFirst().get();
		tryDeleteAndKeepException( cashdto.id );
	}
	
	@When("I remove the credit card")
	public void iRemoveTheCreditCard() throws BusinessException {
		service.deletePaymentMean(card.id);
	}
	
	@When("I try to remove a non existent payment mean")
	public void iTryToRemoveANonExistentPaymentMean() throws BusinessException {
		tryDeleteAndKeepException(UUID.randomUUID().toString());

	}
	
	@When("I try to delete a null payment mean")
	public void iTryToDeleteANullPaymentMean() {
		tryDeleteAndKeepException(null);

	}

	@Given("an invoice")
	public void anInvoice() throws BusinessException {
	    InvoicingService invoiceService = BusinessFactory.forInvoicingService();
	    @SuppressWarnings("unchecked")
		List<WorkOrderDto> lista = (List<WorkOrderDto>)(this.ctx.get(TestContext.Key.WORKORDERS));
	    List<String> forInvoicing = lista.stream().map(wo -> wo.id).collect(Collectors.toList());
	    invoice = invoiceService.createInvoiceFor(forInvoicing);

	}

	@Given("some charges")
	public void someCharges() {
		new ChargeUtil()
				.forInvoice(invoice.id)
				.withPaymentMean(this.thePaymentId)
				.register();
		
	}

	@When("I try to remove the payment mean")
	public void iTryToRemoveThePaymentMean() {
	    tryDeleteAndKeepException(thePaymentId);
	}

	
	@Then("the credit card no longer exists")
	public void theCreditCardNoLongerExists() throws BusinessException {
	    
	    assertTrue(service.findById(card.id).isEmpty());
	}

	
	private void tryDeleteAndKeepException( String id ) {
		try {
			service.deletePaymentMean( id );
			fail();
		} catch (BusinessException ex) {
			ctx.setException( ex );		
		} catch (IllegalArgumentException ex) {
			ctx.setException( ex );
		}

	}
	
}
