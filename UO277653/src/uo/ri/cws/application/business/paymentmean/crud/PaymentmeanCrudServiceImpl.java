package uo.ri.cws.application.business.paymentmean.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.paymentmean.CardDto;
import uo.ri.cws.application.business.paymentmean.PaymentMeanDto;
import uo.ri.cws.application.business.paymentmean.PaymentmeanCrudService;
import uo.ri.cws.application.business.paymentmean.crud.commands.AddCardPaymentMean;
import uo.ri.cws.application.business.paymentmean.crud.commands.AddVoucherPaymentMean;
import uo.ri.cws.application.business.paymentmean.crud.commands.DeletePaymentMean;
import uo.ri.cws.application.business.paymentmean.crud.commands.FindPaymentMeanByClientId;
import uo.ri.cws.application.business.paymentmean.crud.commands.FindPaymentMeanById;
import uo.ri.cws.application.business.paymentmean.voucher.VoucherDto;
import uo.ri.cws.application.business.util.command.CommandExecutor;

public class PaymentmeanCrudServiceImpl implements PaymentmeanCrudService {

	private CommandExecutor executor = new CommandExecutor();
	
	@Override
	public void addCardPaymentMean(CardDto card) throws BusinessException {
		executor.execute(new AddCardPaymentMean(card));
	}

	@Override
	public void addVoucherPaymentMean(VoucherDto voucher) throws BusinessException {
		executor.execute(new AddVoucherPaymentMean(voucher));
		
	}

	@Override
	public void deletePaymentMean(String id) throws BusinessException {
		executor.execute(new DeletePaymentMean(id));
		
	}

	@Override
	public Optional<PaymentMeanDto> findById(String id) throws BusinessException {
		return executor.execute(new FindPaymentMeanById(id));
	}

	@Override
	public List<PaymentMeanDto> findPaymentMeansByClientId(String id) throws BusinessException {
		
		return executor.execute(new FindPaymentMeanByClientId(id));
	}

}
