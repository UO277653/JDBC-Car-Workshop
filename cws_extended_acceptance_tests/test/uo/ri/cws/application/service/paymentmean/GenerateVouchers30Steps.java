package uo.ri.cws.application.service.paymentmean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import uo.ri.cws.application.persistence.invoice.InvoiceRecord;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.util.InvoiceUtil;

public class GenerateVouchers30Steps {

	private TestContext ctx;


	public GenerateVouchers30Steps(TestContext ctx) {
		this.ctx = ctx;
	}
	
//	  | number | status | date | used | money | vat |
//	  | 1 | PAID | 2021-09-2 | false | 1500 | 21.0 |
	
	@Given("the following invoices")
	public void theFollowingInvoices(DataTable data) {
		List<Map<String, String>> table = data.asMaps();
		List<InvoiceRecord> invoices = new ArrayList<InvoiceRecord>();
		
		for (Map<String, String> row : table) {
			InvoiceRecord newInvoice = new InvoiceUtil()
					.withAmount(Double.parseDouble(row.get("money")) )
					.withStatus(row.get("status"))
					.withUse(row.get("use"))
					.withNumber(Long.parseLong(row.get("number")))
					.register()
					.get();		
			invoices.add(newInvoice);
		}	
		
		this.ctx.put(TestContext.Key.INVOICES, invoices);

	}

	
}
