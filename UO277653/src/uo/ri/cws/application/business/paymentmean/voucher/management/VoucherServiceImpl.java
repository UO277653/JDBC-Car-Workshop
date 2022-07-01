package uo.ri.cws.application.business.paymentmean.voucher.management;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.paymentmean.voucher.VoucherDto;
import uo.ri.cws.application.business.paymentmean.voucher.VoucherService;
import uo.ri.cws.application.business.paymentmean.voucher.VoucherSummaryDto;
import uo.ri.cws.application.business.paymentmean.voucher.management.commands.FindVouchersByClientId;
import uo.ri.cws.application.business.paymentmean.voucher.management.commands.GenerateVouchersAction;
import uo.ri.cws.application.business.paymentmean.voucher.management.commands.GetVoucherSummary;
import uo.ri.cws.application.business.util.command.CommandExecutor;

public class VoucherServiceImpl implements VoucherService{

	private CommandExecutor executor = new CommandExecutor();
	
	@Override
	public int generateVouchers() throws BusinessException {
		return executor.execute(new GenerateVouchersAction());
	}

	@Override
	public Optional<VoucherDto> findVouchersById(String id) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VoucherDto> findVouchersByClientId(String id) throws BusinessException {
		return executor.execute(new FindVouchersByClientId(id));
	}

	@Override
	public List<VoucherSummaryDto> getVoucherSummary() throws BusinessException {
		return executor.execute(new GetVoucherSummary());
	}

}
