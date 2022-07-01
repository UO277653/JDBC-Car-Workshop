package uo.ri.cws.application.business.paymentmean.crud.commands;

import java.util.Optional; 

import alb.util.assertion.Argument;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.util.command.Command;
import uo.ri.cws.application.persistence.PersistenceFactory;
import uo.ri.cws.application.persistence.charge.ChargeGateway;
import uo.ri.cws.application.persistence.creditcard.CreditCardGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentmeanRecord;
import uo.ri.cws.application.persistence.voucher.VoucherGateway;

public class DeletePaymentMean implements Command<Void>{

	private String id;
	private PaymentMeanGateway pmg = PersistenceFactory.forPaymentmean();
	private ChargeGateway cg = PersistenceFactory.forCharge();
	private CreditCardGateway ccg = PersistenceFactory.forCreditCard();
	private VoucherGateway vg = PersistenceFactory.forVoucher();
	
	public DeletePaymentMean(String id) {
		Argument.isNotNull(id);
		Argument.isNotEmpty(id);
		this.id = id;
	}

	@Override
	public Void execute() throws BusinessException {
		
		Optional<PaymentmeanRecord> pmgRecord = pmg.findById(id);
		
		if(pmgRecord.isEmpty()) {
			throw new BusinessException("There is no payment with such id");
		}
		
		if(pmgRecord.get().dtype.toLowerCase().equals("cash")) {
			throw new BusinessException("Cannot delete a cash payment");
		}
		
		if(!cg.findByPaymentMeanId(id).isEmpty()) {
			throw new BusinessException("There are charges associated with the payment");
		}
		
		if(pmgRecord.get().dtype.toLowerCase().equals("voucher")) {
			vg.remove(id);
			pmg.remove(id);
			
		} else if(pmgRecord.get().dtype.toLowerCase().equals("card")) {
			ccg.remove(id);
			pmg.remove(id);
		}
		
		
		return null;
	}

}
