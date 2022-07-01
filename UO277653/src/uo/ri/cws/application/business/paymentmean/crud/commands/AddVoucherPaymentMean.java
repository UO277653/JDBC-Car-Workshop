package uo.ri.cws.application.business.paymentmean.crud.commands;

import alb.util.assertion.Argument;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.paymentmean.voucher.VoucherDto;
import uo.ri.cws.application.business.util.DtoAssembler;
import uo.ri.cws.application.business.util.command.Command;
import uo.ri.cws.application.persistence.PersistenceFactory;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.voucher.VoucherGateway;

public class AddVoucherPaymentMean implements Command<Void>{

	private VoucherDto voucher;
	private ClientGateway cg = PersistenceFactory.forClient();
	private PaymentMeanGateway pmg = PersistenceFactory.forPaymentmean();
	private VoucherGateway vg = PersistenceFactory.forVoucher();
	
	public AddVoucherPaymentMean(VoucherDto voucher) {
		Argument.isNotNull(voucher);
		Argument.isNotNull(voucher.code);
		Argument.isNotNull(voucher.description);
		Argument.isNotNull(voucher.clientId);
		Argument.isNotEmpty(voucher.code);
		Argument.isNotEmpty(voucher.description);
		Argument.isNotEmpty(voucher.clientId);
		Argument.isTrue(voucher.balance >= 0);
		this.voucher = voucher;
	}

	@Override
	public Void execute() throws BusinessException {
		
		if(!cg.findById(voucher.clientId).isPresent()) {
			throw new BusinessException("Client does not exist");
		}
		
		if(!vg.findByCode(voucher.code).isEmpty()) {
			throw new BusinessException("A voucher with the same code already exists");
		}
		
		pmg.add(DtoAssembler.toRecord(voucher));
		vg.add(DtoAssembler.toRecord(voucher));
		
		return null;
	}
	
	

}
