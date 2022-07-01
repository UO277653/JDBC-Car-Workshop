package uo.ri.cws.application.business.paymentmean.crud.commands;

import java.util.List;

import alb.util.assertion.Argument;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.paymentmean.PaymentMeanDto;
import uo.ri.cws.application.business.util.DtoAssembler;
import uo.ri.cws.application.business.util.command.Command;
import uo.ri.cws.application.persistence.PersistenceFactory;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;

public class FindPaymentMeanByClientId implements Command<List<PaymentMeanDto>>{

	private String id;
	private PaymentMeanGateway pmg = PersistenceFactory.forPaymentmean();
	
	public FindPaymentMeanByClientId(String id) {
		Argument.isNotNull(id);
		Argument.isNotEmpty(id);
		
		this.id = id;
	}

	@Override
	public List<PaymentMeanDto> execute() throws BusinessException {
		return DtoAssembler.toDtoListPaymentMean(pmg.findByClientId(id));
	}

}
