package uo.ri.conf;

import uo.ri.cws.application.business.BusinessFactory;
import uo.ri.cws.application.business.invoice.InvoicingService;
import uo.ri.cws.application.business.mechanic.MechanicCrudService;

public class BusinessFactoryAdapter {

	public MechanicCrudService forMechanicService() {
		return BusinessFactory.forMechanicCrudService();
	}
	
	public InvoicingService forInvoicingService() {
		return BusinessFactory.forInvoicingService();
	}
//	public OrdersService forOrdersService() {
//		return BusinessFactory.forOrdersService();
//	}
//
//	public SparePartCrudService forSparePartCrudService() {
//		return BusinessFactory.forSparePartCrudService();
//	}
//
////	public ProvidersCrudService forProvidersService() {
////		return BusinessFactory.forProvidersService();
////	}
////
////	public SuppliesCrudService forSuppliesCrudService() {
////		return BusinessFactory.forSuppliesCrudService();
////	}
//
//	public SparePartReportService forSparePartReportService() {
//		return BusinessFactory.forSparePartReportService();
//	}

}
