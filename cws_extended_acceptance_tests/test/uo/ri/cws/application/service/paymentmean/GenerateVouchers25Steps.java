package uo.ri.cws.application.service.paymentmean;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.BusinessFactory;
import uo.ri.cws.application.business.client.ClientDto;
import uo.ri.cws.application.business.paymentmean.voucher.VoucherDto;
import uo.ri.cws.application.business.paymentmean.voucher.VoucherService;
import uo.ri.cws.application.business.workorder.WorkOrderDto;
import uo.ri.cws.application.persistence.invoice.InvoiceRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentmeanRecord;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.util.ClientUtil;
import uo.ri.cws.application.service.util.PaymentMeansUtil;
import uo.ri.cws.application.service.util.RecommendationUtil;
import uo.ri.cws.application.service.util.VehicleUtil;
import uo.ri.cws.application.service.util.VehicleUtil.VehicleDto;
import uo.ri.cws.application.service.util.VoucherUtil;
import uo.ri.cws.application.service.util.WorkOrderUtil;
import uo.ri.cws.application.service.util.sql.FindClientByDNISqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindVehicleSqlUnitOfWork;

public class GenerateVouchers25Steps {

	private TestContext ctx;
	private List<VehicleDto> vehicles = new ArrayList<VehicleDto>() ;
	private VoucherService service = BusinessFactory.forVoucherService();
	private ClientDto sponsor = null;
	private VehicleDto vehicle = null;
	private List<ClientDto> clients = new ArrayList<>();
	private int numGeneratedVouchersSecondTime;
	private int numGeneratedVouchers = 0;
//	private  invoices = null;
	
	public GenerateVouchers25Steps(TestContext ctx) {
		this.ctx = ctx;

	}

	@Given("this client as sponsor")
	public void thisClientAsSponsor(DataTable data) {
		List<Map<String, String>> table = data.asMaps();
		Map<String, String> row = table.get(0);
		
		this.sponsor = new ClientUtil()
			.withDni(row.get("clientdni"))
			.register()
			.get();
 
		this.vehicle = new VehicleUtil()
			.withPlate(row.get("vehiclePlate"))
			.withOwner(sponsor.id)
			.register()
			.get();
		vehicles.add(vehicle);
	}

	@Given("the following relation of clients and vehicles")
	public void theFollowingClientsAndVehicles(DataTable data) {
		List<Map<String, String>> table = data.asMaps();
		for (Map<String, String> row : table) {
			ClientDto newClient = new ClientUtil()
					.withDni(row.get("clientdni"))
					.registerIfNew()
					.get();
			this.clients.add(newClient);
			
			vehicles.add( new VehicleUtil()
					.withPlate(row.get("vehiclePlate"))
					.withOwner(newClient.id)
					.register()
					.get() );
		}

	}

	@SuppressWarnings("unchecked")
	@Given("the following workorders")
	public void theFollowingWorkorders(DataTable data) {
		List<Map<String, String>> table = data.asMaps();		
		List<WorkOrderDto> finishedWorkorders = new ArrayList<WorkOrderDto>();
		List<InvoiceRecord> invoices = ((List<InvoiceRecord>)this.ctx.get(TestContext.Key.INVOICES)); 
		String invoiceNumber = null;
		for (Map<String, String> row : table) {
			FindVehicleSqlUnitOfWork finder = new FindVehicleSqlUnitOfWork( row.get("platenum") );
			finder.execute();
			WorkOrderUtil util = new WorkOrderUtil();
			WorkOrderDto dto = util
					.forVehicle(finder.get().id)
					.withStatus(row.get("status"))
					.notUserForVoucher()
					.withDate(row.get("date"))
					.withUse(row.get("usedForVoucher"))
//					.withAmount(Double.parseDouble(row.get("money")) )
//					.register()
					.get();
			if (dto.status.equals("INVOICED")) {
				invoiceNumber = invoices
						.stream()
						.filter(i -> i.number == Long.parseLong(row.get("invoiceNumber")))
						.findFirst()
						.get()
						.id;
				util = util.withInvoice(invoiceNumber);
			}
			
			dto = util
					.register()
					.get();


			if (dto.status.equals("FINISHED"))
				finishedWorkorders.add(dto);
				
//			new WorkOrderUtil()
//				.forVehicle(finder.get().id)
//				.withStatus(row.get("status"))
//				.notUserForVoucher()
//				.withDate(row.get("date"))
//				.withUse(row.get("used"))
//				.withAmount(Double.parseDouble(row.get("money")) )
//				.register();

		}
		this.ctx.put(TestContext.Key.WORKORDERS, finishedWorkorders);

	}
	
	@Given("the following recommendations")
	public void theFollowingRecommendations(DataTable data) {
		List<Map<String, String>> table = data.asMaps();
		FindClientByDNISqlUnitOfWork finder;
		String sponsorId, clientId;
		
		for (Map<String, String> row : table) {
			finder = new FindClientByDNISqlUnitOfWork(row.get("sponsordni"));
			finder.execute();
			sponsorId = finder.get().get().id;
			finder = new FindClientByDNISqlUnitOfWork(row.get("clientdni"));
			finder.execute();
			clientId = finder.get().get().id;
			new RecommendationUtil()
				.withSponsor(sponsorId)
				.withClient(clientId)
				.withUse(row.get("usedForVoucher"))
				.register();

		}
		
	}
	@When("I generate vouchers")
	public void iGenerateVouchers() throws BusinessException {
		this.numGeneratedVouchers  = service.generateVouchers();
	}
	
	@When("I generate vouchers again")
	public void iGenerateVouchersAgain() throws BusinessException {
		this.numGeneratedVouchersSecondTime = service.generateVouchers();
	}
	
	@Then("We get the following vouchers")
	public void weGetTheFollowingVouchers(DataTable data) {
		
		/*
		 * For each row, search database by client
		 */
		List<Map<String, String>> list = data.asMaps();

		int expectednumber = list.stream()
				.map(row -> Integer.parseInt(row.get("num")))
				.reduce(0, Integer::sum);
				
		assertTrue (this.numGeneratedVouchers == expectednumber );
		for ( Map<String, String> row : list ) {
			/*
			 * Find client id
			 */
			String clientid = new ClientUtil().findByDni(row.get("client")).id;
			/*
			 * Find payment mean for this client id. 
			 */
			List<PaymentmeanRecord> payRecords = new PaymentMeansUtil().findPaymentMeansForClient(clientid);
			/*
			 * Then filter by VOUCHER type
			 */
			List<PaymentmeanRecord> filter = payRecords.stream().filter(pr -> pr.dtype.equals("VOUCHER")).collect(Collectors.toList());
			
			/*
			 * there should be the same than the number in the table 
			 */
			assertTrue (filter.size() == Integer.parseInt(row.get("num")) );
			/*
			 * Find these vouchers 
			 */
			List<VoucherDto> voucherRecords = filter
					.stream()
					.map(p -> new VoucherUtil().findById(p.id).get())
					.collect(Collectors.toList()); 
					
			/*
			 * there should be the same number as in the table
			 */
			assertTrue (voucherRecords.size() == Integer.parseInt(row.get("num")) );
			/*
			 * Match fields
			 */
			assertTrue ( matchAccumulatedField(filter, row) );
			assertTrue ( matchBalanceField(voucherRecords, row) );
			assertTrue ( matchDescriptions(voucherRecords, row) );
		}
		
	}
	
	@Then("We get no vouchers")
	public void weGetNoVouchers() {
	   assertTrue(this.numGeneratedVouchersSecondTime == 0);
	   
	}

	private boolean matchDescriptions(List<VoucherDto> arg, Map<String, String> row) {

		List<String> descriptioninDB = arg.stream().map(r -> r.description).collect(Collectors.toList());
		String[] temp = row.get("description").split(",");
		List<String> asList = Arrays.asList(temp);
		Collections.sort(asList);
		Collections.sort(descriptioninDB);
		return asList.equals( descriptioninDB);

	}

	private boolean matchAccumulatedField(List<PaymentmeanRecord> payRecords, Map<String, String> row) {
		List<PaymentmeanRecord> list = payRecords
			.stream()
			.filter( c -> c.dtype.equals("VOUCHER") )
			.collect(Collectors.toList());
		List<Double> accumulatedinDB = list.stream().map(r -> r.accumulated).collect(Collectors.toList());
		String[] temp = row.get("accumulated").split(",");
		List<Double> asList = Arrays.stream(temp)    // stream of String
                .map(Double::valueOf) // stream of Integer
                .collect(Collectors.toList());
		Collections.sort(asList);
		Collections.sort(accumulatedinDB);

		return asList.equals( accumulatedinDB);

	}


	private boolean matchBalanceField(List<VoucherDto> arg, Map<String, String> row) {
		
		List<Double> balanceinDB = arg.stream().map(item -> item.balance).collect(Collectors.toList());
		String[] temp = row.get("balance").split(",");
		List<Double> asList = Arrays.stream(temp)    // stream of String
                .map(Double::valueOf) // stream of Integer
                .collect(Collectors.toList());
		Collections.sort(asList);
		Collections.sort(balanceinDB);
		return asList.equals( balanceinDB);

	}
	
	
}
