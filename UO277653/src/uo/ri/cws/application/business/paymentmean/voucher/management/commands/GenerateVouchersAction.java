package uo.ri.cws.application.business.paymentmean.voucher.management.commands;

import java.util.ArrayList; 
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.paymentmean.voucher.VoucherDto;
import uo.ri.cws.application.business.util.DtoAssembler;
import uo.ri.cws.application.business.util.command.Command;
import uo.ri.cws.application.persistence.PersistenceFactory;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientRecord;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.invoice.InvoiceRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.recommendation.RecommendationGateway;
import uo.ri.cws.application.persistence.recommendation.RecommendationRecord;
import uo.ri.cws.application.persistence.vehicle.VehicleGateway;
import uo.ri.cws.application.persistence.vehicle.VehicleRecord;
import uo.ri.cws.application.persistence.voucher.VoucherGateway;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.cws.application.persistence.workorder.WorkOrderRecord;

public class GenerateVouchersAction implements Command<Integer>{

	private ClientGateway cg = PersistenceFactory.forClient();
	private RecommendationGateway rcg = PersistenceFactory.forRecommendation();
	private VehicleGateway vhg = PersistenceFactory.forVehicle();
	private WorkOrderGateway wog = PersistenceFactory.forWorkOrder();
	private InvoiceGateway ig = PersistenceFactory.forInvoice();
	private PaymentMeanGateway pmg = PersistenceFactory.forPaymentmean();
	private VoucherGateway vg = PersistenceFactory.forVoucher();
	
	@Override
	public Integer execute() throws BusinessException {
		
		int res = 0;
		boolean hasPaidInvoice;
		List<RecommendationRecord> validReferralList;
		
		List<ClientRecord> clients = cg.findAll();
		
		for(ClientRecord c: clients) {
			
			validReferralList = new ArrayList<RecommendationRecord>();
			hasPaidInvoice = false;
			
			List<RecommendationRecord> records = rcg.findRecommendedIdBySponsorIdNotUsedForVoucher(c.id);
			
			if(records.size() < 3) {
				continue; 
			}
			
			List<VehicleRecord> vehicles = vhg.findByClientId(c.id);
			
			for(VehicleRecord v: vehicles) {
				List<WorkOrderRecord> vehicleWorkOrders = wog.findInvoicedByVehicleId(v.id);
				
				for(WorkOrderRecord wo: vehicleWorkOrders) {
					
					Optional<InvoiceRecord> invoice = ig.findById(wo.invoice_id);
					
					if(invoice.isPresent()) {
						if(invoice.get().status.equals("PAID")) {
							hasPaidInvoice = true;
						}
					}
				}
			}
			
			if(!hasPaidInvoice) {
				continue;
			}
			
			for(RecommendationRecord recommendedClient: records) {
				
				Optional<ClientRecord> client = cg.findById(recommendedClient.recommended_id);
				
				vehicles = vhg.findByClientId(client.get().id);
				
				for(VehicleRecord v: vehicles) {
					List<WorkOrderRecord> vehicleWorkOrders = wog.findInvoicedByVehicleId(v.id);
					
					for(WorkOrderRecord wo: vehicleWorkOrders) {
						
						Optional<InvoiceRecord> invoice = ig.findById(wo.invoice_id);
						
						if(invoice.isPresent() ) {
							if(invoice.get().status.equals("PAID")) {
								validReferralList.add(recommendedClient);
							}
						} 
					}
				}
			}
			
			for(int i = 0; i < validReferralList.size()/3; i++) {
				RecommendationRecord client1 = validReferralList.get(3*i);
				RecommendationRecord client2 = validReferralList.get(3*i+1);
				RecommendationRecord client3 = validReferralList.get(3*i+2);
				
				VoucherDto voucher = new VoucherDto();
				voucher.accumulated = 0.0;
				voucher.balance = 25.0;
				voucher.clientId = c.id;
				// voucher.code
				voucher.code = UUID.randomUUID().toString();
				voucher.description = "By recommendation";
				voucher.id = UUID.randomUUID().toString();
				voucher.version = (long) 0;
				
				pmg.add(DtoAssembler.toRecord(voucher));
				vg.add(DtoAssembler.toRecord(voucher));
				rcg.updateUseVoucherForClient(client1.recommended_id);
				rcg.updateUseVoucherForClient(client2.recommended_id);
				rcg.updateUseVoucherForClient(client3.recommended_id);
				
				res++;
			}
		}
		
		return res;
	}

}
