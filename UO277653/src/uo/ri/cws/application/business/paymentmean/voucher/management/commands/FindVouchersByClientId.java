package uo.ri.cws.application.business.paymentmean.voucher.management.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import alb.util.assertion.Argument;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.paymentmean.voucher.VoucherDto;
import uo.ri.cws.application.business.util.DtoAssembler;
import uo.ri.cws.application.business.util.command.Command;
import uo.ri.cws.application.persistence.PersistenceFactory;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentmeanRecord;
import uo.ri.cws.application.persistence.voucher.VoucherGateway;
import uo.ri.cws.application.persistence.voucher.VoucherRecord;

public class FindVouchersByClientId implements Command<List<VoucherDto>>{

	private VoucherGateway vg = PersistenceFactory.forVoucher();
	private PaymentMeanGateway pmg = PersistenceFactory.forPaymentmean();
	private String idClient;
	
	public FindVouchersByClientId(String id) {
		
		Argument.isNotNull(id);
		Argument.isTrue(!id.trim().isEmpty());
		
		this.idClient = id;
	}
	
	@Override
	public List<VoucherDto> execute() throws BusinessException {
		
		List<VoucherDto> result = new ArrayList<VoucherDto>();
		List<PaymentmeanRecord> paymentMeanVouchers = pmg.findByClientIdVouchers(idClient);
		VoucherRecord vrecord;
		VoucherDto vdto;
		
		for(PaymentmeanRecord voucher: paymentMeanVouchers) {
			
			Optional<VoucherRecord> vrecordOpt = vg.findById(voucher.id);
			if(vrecordOpt.isPresent()) {
				vrecord = vrecordOpt.get();
				vdto = DtoAssembler.toDtoVoucher(vrecord);
				vdto.accumulated = voucher.accumulated;
				vdto.clientId = voucher.client_id;
				vdto.version = voucher.version;
				result.add(vdto);
			}
		}
		
		return result;
	}

}
