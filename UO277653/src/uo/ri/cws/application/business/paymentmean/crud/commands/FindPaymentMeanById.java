package uo.ri.cws.application.business.paymentmean.crud.commands;

import java.util.Optional;

import alb.util.assertion.Argument;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.paymentmean.PaymentMeanDto;
import uo.ri.cws.application.business.util.DtoAssembler;
import uo.ri.cws.application.business.util.command.Command;
import uo.ri.cws.application.persistence.PersistenceFactory;
import uo.ri.cws.application.persistence.creditcard.CreditCardGateway;
import uo.ri.cws.application.persistence.creditcard.CreditCardRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentmeanRecord;
import uo.ri.cws.application.persistence.voucher.VoucherGateway;
import uo.ri.cws.application.persistence.voucher.VoucherRecord;

public class FindPaymentMeanById implements Command<Optional<PaymentMeanDto>>{

	private PaymentMeanGateway pmg = PersistenceFactory.forPaymentmean();
	private VoucherGateway vg = PersistenceFactory.forVoucher();
	private CreditCardGateway ccg = PersistenceFactory.forCreditCard();
	private String id;
	
	public FindPaymentMeanById(String id) {
		Argument.isNotNull(id);
		Argument.isNotEmpty(id);
		
		this.id = id;
	}

	@Override
	public Optional<PaymentMeanDto> execute() throws BusinessException {
		
		Optional<PaymentmeanRecord> pmgRecord = pmg.findById(id);
		
		if(pmgRecord.isEmpty()) {
			return DtoAssembler.toDtoPaymentMean(pmgRecord);
		}
		
		if(pmgRecord.get().dtype.toLowerCase().equals("voucher")) {
			Optional<VoucherRecord> pmgVoucherRecord = vg.findById(id);
			
			pmgVoucherRecord.get().version = pmgRecord.get().version;
			pmgVoucherRecord.get().accumulated = pmgRecord.get().accumulated;
			pmgVoucherRecord.get().client_id = pmgRecord.get().client_id;
			
			return DtoAssembler.toDtoVoucher(pmgVoucherRecord);
			
		} else if(pmgRecord.get().dtype.toLowerCase().equals("card")) {
			Optional<CreditCardRecord> ccRecord = ccg.findById(id);
			
			ccRecord.get().version = pmgRecord.get().version;
			ccRecord.get().accumulated = pmgRecord.get().accumulated;
			ccRecord.get().client_id = pmgRecord.get().client_id;
			
			return DtoAssembler.toDtoCreditCard(ccRecord);
		}
		
		return DtoAssembler.toDtoPaymentMean(pmg.findById(id));
	}

}
