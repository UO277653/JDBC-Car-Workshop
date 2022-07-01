package uo.ri.cws.application.service.paymentmean;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.BusinessFactory;
import uo.ri.cws.application.business.client.ClientDto;
import uo.ri.cws.application.business.paymentmean.voucher.VoucherDto;
import uo.ri.cws.application.business.paymentmean.voucher.VoucherService;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.util.ClientUtil;
import uo.ri.cws.application.service.util.VoucherUtil;

public class ListVouchersSteps {

	private TestContext ctx;
	private VoucherService service = BusinessFactory.forVoucherService();
	private ClientDto theClient;
	private List<ClientDto> theClients = new ArrayList<ClientDto>();
	
	private List<VoucherDto> vouchers;
	
	public ListVouchersSteps(TestContext ctx) {
		this.ctx = ctx;

	}

	@When("I list vouchers by non existent client")
	public void iListVouchersByNonExistentClient() throws BusinessException {
		vouchers = service.findVouchersByClientId(UUID.randomUUID().toString());
	}

	@Then("I get no vouchers")
	public void iGetNoVouchers() {
		assertTrue(vouchers.isEmpty());
	}

	@Given("the following clients and vouchers")
	public void theFollowingClientsAndVouchers(DataTable data) {
//	    | dni | name | surname | code | accumulated | balance |

		List<Map<String, String>> table = data.asMaps();
		for (Map<String, String> row : table) {
			ClientDto client = createClient(row.get("dni"), row.get("name"), row.get("surname"));
			theClients.add(client);
			createVoucher(client.id, row.get("code"), row.get("accumulated"), row.get("balance"));
		}
	}

	private VoucherDto createVoucher(String id, String code, String accumulated, String remain) {
		VoucherDto v = new VoucherUtil()
				.withClient(id)
				.withCode(code)
				.withAccumulated(accumulated)
				.withBalance(remain)
				.register()
				.get();
		return v;
	}

	private ClientDto createClient(String dni, String name, String surname) {
		ClientDto client = new ClientUtil()
				.withDni(dni)
				.withName(name)
				.withSurname(surname)
				.registerIfNew()
				.get();
		return client;
	}

	@When("I list vouchers for the first client")
	public void iListVouchersForTheSecondClient() throws BusinessException {
		theClient = theClients.get(0);
		vouchers = service.findVouchersByClientId(theClient.id);
	}

	@When("I list vouchers for the client")
	public void iListVouchersForTheClient() throws BusinessException {		
		theClient = (ClientDto) this.ctx.get(TestContext.Key.ACLIENT);
		vouchers = service.findVouchersByClientId(theClient.id);
	}



	
	@Then("I get the following vouchers")
	public void iGetTheFollowingVouchers(DataTable data) {

		List<Map<String, String>> table = data.asMaps();
		assertTrue (vouchers.size() == table.size());
		for (Map<String, String> row : table) {
			String code = row.get("code");
			Optional<VoucherDto> ov = vouchers.stream().filter(r -> r.code.equals(code)).findFirst();
			assertTrue(ov.isPresent());
			VoucherDto v = ov.get();
			assertTrue(v.clientId.equals(theClient.id));
			assertTrue(v.accumulated == Double.parseDouble(row.get("accumulated")));
			assertTrue(v.code.equals(row.get("code")));
			assertTrue(v.balance.equals(Double.parseDouble(row.get("balance"))));
			
		}

	}

}
