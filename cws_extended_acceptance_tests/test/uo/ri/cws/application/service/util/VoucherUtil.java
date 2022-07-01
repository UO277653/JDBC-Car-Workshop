package uo.ri.cws.application.service.util;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import uo.ri.cws.application.business.paymentmean.voucher.VoucherDto;
import uo.ri.cws.application.persistence.paymentmean.PaymentmeanRecord;
import uo.ri.cws.application.persistence.voucher.VoucherRecord;
import uo.ri.cws.application.service.util.sql.AddPaymentMeanSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.AddVoucherSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindVoucherByCodeSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindVoucherByIdSqlUnitOfWork;

public class VoucherUtil {

	
	private VoucherDto dto = createDefaultVoucher();

	
	public VoucherDto get() {
		return dto;
	}

	private VoucherDto createDefaultVoucher() {
		
		VoucherDto res = new VoucherDto();


		Random r = new Random();
		int rangeMin = 10;
		int rangeMax = 100;
		double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		
		res.id = UUID.randomUUID().toString();
		res.version = 1L;
		res.accumulated = 0.0;
		res.balance = randomValue;
		res.description = UUID.randomUUID().toString();
		res.code = UUID.randomUUID().toString();
		return res;
	}

	public VoucherUtil withClient(String id) {
		dto.clientId = id;
		return this;
	}
	
	public VoucherUtil withCode(String arg) {
		dto.code = arg;
		return this;
	}
	
	public VoucherUtil withAccumulated (String arg ) {
		dto.accumulated = Double.parseDouble(arg);
		return this;
	}

	public VoucherUtil withBalance ( String arg ) {
		dto.balance = Double.parseDouble(arg);
		return this;		
	}
	
	public Optional<VoucherDto> findById (String id) {
		FindVoucherByIdSqlUnitOfWork findPayments = new FindVoucherByIdSqlUnitOfWork(id);
		findPayments.execute();
		VoucherDto paymentRecord = findPayments.get();
		return Optional.ofNullable(paymentRecord);
		
	}
	
	public Optional<VoucherDto> findByCode(String code) {
		FindVoucherByCodeSqlUnitOfWork findPayments = new FindVoucherByCodeSqlUnitOfWork(code);
		findPayments.execute();
		VoucherDto paymentRecord = findPayments.get();
		return Optional.ofNullable(paymentRecord);
		
	}

	public VoucherUtil register () {
		registerPayment();
		registerVoucher();
		return this;
	}

	private void registerVoucher() {
		VoucherRecord vRecord = new VoucherRecord ();
		vRecord.id = dto.id;
		vRecord.available = dto.balance;
		vRecord.code = dto.code;
		new AddVoucherSqlUnitOfWork(vRecord).execute();		

		
	}

	private void registerPayment() {
		PaymentmeanRecord payRecord = new PaymentmeanRecord();
		payRecord.id = dto.id;
		payRecord.accumulated = dto.accumulated;
		payRecord.client_id = dto.clientId;
		payRecord.version = dto.version;
		payRecord.dtype = "VOUCHER";
		new AddPaymentMeanSqlUnitOfWork(payRecord).execute();		
	}
}
