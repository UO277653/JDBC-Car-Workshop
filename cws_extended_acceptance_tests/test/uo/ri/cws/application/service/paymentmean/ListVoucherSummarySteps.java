package uo.ri.cws.application.service.paymentmean;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.BusinessFactory;
import uo.ri.cws.application.business.paymentmean.voucher.VoucherService;
import uo.ri.cws.application.business.paymentmean.voucher.VoucherSummaryDto;
import uo.ri.cws.application.service.common.TestContext;

public class ListVoucherSummarySteps {

	private VoucherService service = BusinessFactory.forVoucherService();
	private List<VoucherSummaryDto> theSummary = new ArrayList<VoucherSummaryDto>();

	public ListVoucherSummarySteps(TestContext ctx) {

	}

	@When("I list voucher summary")
	public void iListVoucherSummary() throws BusinessException {
		theSummary = service.getVoucherSummary();
	}

	@Then("I get no result")
	public void iGetNoResult() {
		assertTrue(theSummary.isEmpty());
	}

	@Then("I get the following result")
	public void iGetTheFollowingResult(DataTable data) {
		List<Map<String, String>> table = data.asMaps();
		for (Map<String, String> row : table) {
			String dni = row.get("dni");
			List<VoucherSummaryDto> resultsByClient = theSummary.stream()
					.filter(s -> s.dni.equals(dni))
					.collect(Collectors.toList());
			assertTrue(resultsByClient.size() == 1);
			match(resultsByClient.get(0), row);
		}

	}

	private boolean match(VoucherSummaryDto arg, Map<String, String> row) {
//		| dni | name | surname | issued | accumulated | balance | total |		

		assertTrue (arg.name.equals(row.get("name")));
		assertTrue (arg.surname.equals(row.get("surname")));
		assertTrue (arg.issued ==  Integer.parseInt(row.get("issued")));
		assertTrue (arg.consumed ==  Double.parseDouble(row.get("accumulated")));
		assertTrue (arg.availableBalance ==  Double.parseDouble(row.get("balance")));
		assertTrue (arg.totalAmount ==  Double.parseDouble(row.get("total")));

		return false;
	}

//	@When("I list vouchers by non existent client")
//	public void iListVouchersByNonExistentClient() throws BusinessException {
//		vouchers = service.findVouchersByClientId(UUID.randomUUID().toString());
//	}
//
//	@Then("I get no vouchers")
//	public void iGetNoVouchers() {
//		assertTrue(vouchers.isEmpty());
//	}
//
//	@Given("the following clients and vouchers")
//	public void theFollowingClientsAndVouchers(DataTable data) {
////	    | dni | name | surname | code | accumulated | balance |
//
//		List<Map<String, String>> table = data.asMaps();
//		for (Map<String, String> row : table) {
//			ClientDto client = createClient(row.get("dni"), row.get("name"), row.get("surname"));
//			theClients.add(client);
//			createVoucher(client.id, row.get("code"), row.get("accumulated"), row.get("balance"));
//		}
//	}
//
//	private VoucherDto createVoucher(String id, String code, String accumulated, String remain) {
//		VoucherDto v = new VoucherUtil()
//				.withClient(id)
//				.withCode(code)
//				.withAccumulated(accumulated)
//				.withBalance(remain)
//				.register()
//				.get();
//		return v;
//	}
//
//	private ClientDto createClient(String dni, String name, String surname) {
//		ClientDto client = new ClientUtil()
//				.withDni(dni)
//				.withName(name)
//				.withSurname(surname)
//				.registerIfNew()
//				.get();
//		return client;
//	}
//
//	@When("I list vouchers for the first client")
//	public void iListVouchersForTheSecondClient() throws BusinessException {
//		theClient = theClients.get(0);
//		vouchers = service.findVouchersByClientId(theClient.id);
//	}
//
//	@When("I list vouchers for the client")
//	public void iListVouchersForTheClient() throws BusinessException {		
//		theClient = (ClientDto) this.ctx.get(TestContext.Key.ACLIENT);
//		vouchers = service.findVouchersByClientId(theClient.id);
//	}
//
//
//
//	
//	@Then("I get the following vouchers")
//	public void iGetTheFollowingVouchers(DataTable data) {
//
//		List<Map<String, String>> table = data.asMaps();
//		assertTrue (vouchers.size() == table.size());
//		for (Map<String, String> row : table) {
//			String code = row.get("code");
//			Optional<VoucherDto> ov = vouchers.stream().filter(r -> r.code.equals(code)).findFirst();
//			assertTrue(ov.isPresent());
//			VoucherDto v = ov.get();
//			assertTrue(v.clientId.equals(theClient.id));
//			assertTrue(v.accumulated == Double.parseDouble(row.get("accumulated")));
//			assertTrue(v.code.equals(row.get("code")));
//			assertTrue(v.balance.equals(Double.parseDouble(row.get("balance"))));
//			
//		}
//
//	}

}
