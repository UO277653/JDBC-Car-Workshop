package uo.ri.cws.application.business.paymentmean.voucher.management.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.paymentmean.voucher.VoucherSummaryDto;
import uo.ri.cws.application.business.util.command.Command;
import uo.ri.cws.application.persistence.PersistenceFactory;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentmeanRecord;
import uo.ri.cws.application.persistence.voucher.VoucherGateway;
import uo.ri.cws.application.persistence.voucher.VoucherRecord;

public class GetVoucherSummary implements Command<List<VoucherSummaryDto>>{

	private ClientGateway cg = PersistenceFactory.forClient();
	private PaymentMeanGateway pmg = PersistenceFactory.forPaymentmean();
	private VoucherGateway vg = PersistenceFactory.forVoucher();
	
	@Override
	public List<VoucherSummaryDto> execute() throws BusinessException {

		List<VoucherSummaryDto> res = new ArrayList<VoucherSummaryDto>();
		VoucherSummaryDto summary;
		int issued;
		double totalAmount, availableBalance, consumed;
		
		List<ClientRecord> clients = cg.findAll();
		
		if(clients.isEmpty()) {
			return res;
		}
		
		for(ClientRecord c: clients) {
			
			summary = new VoucherSummaryDto();
			issued = 0;
			totalAmount = 0;
			availableBalance = 0;
			consumed = 0;
			
			List<PaymentmeanRecord> clientRecords = pmg.findByClientIdVouchers(c.id);
			
			if(!clientRecords.isEmpty()) {
				
				for(PaymentmeanRecord r: clientRecords) {
					issued++;
					
					consumed += r.accumulated;
					
					Optional<VoucherRecord> voucher = vg.findById(r.id);
					
					availableBalance += voucher.get().available;
					
					totalAmount += r.accumulated + voucher.get().available;
					
				}
				
				summary.dni = c.dni;
				summary.name = c.name;
				summary.surname = c.surname;
				summary.issued = issued;
				summary.consumed = consumed;
				summary.availableBalance = availableBalance;
				summary.totalAmount = totalAmount;
				
				res.add(summary);
			
			}
		}
		
		return res;
	}

}
