package uo.ri.cws.application.service.util;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import uo.ri.cws.application.business.paymentmean.CashDto;
import uo.ri.cws.application.persistence.paymentmean.PaymentmeanRecord;
import uo.ri.cws.application.service.util.sql.AddPaymentMeanSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindCashSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindPaymentmeanByClientIdSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindPaymentmeanByIdSqlUnitOfWork;

public class PaymentMeansUtil {

	
	private PaymentmeanRecord pay = createDefaultPaymentmean();


	public PaymentmeanRecord get() {
		return pay;
	}

	private PaymentmeanRecord createDefaultPaymentmean() {
		
		PaymentmeanRecord res = new PaymentmeanRecord();

		res.id = UUID.randomUUID().toString();
		res.version = 1L;
		res.accumulated = 0.0;
		return res;
	}

	public PaymentMeansUtil withClient(String id) {
		pay.client_id = id;
		return this;
	}
	
	public PaymentMeansUtil withType(String dtype) {
		pay.dtype = dtype;
		return this;
	}
	
	public PaymentMeansUtil register() {
		new AddPaymentMeanSqlUnitOfWork(pay).execute();
		return this;
	}
	
	public Optional<CashDto> findCashForClient( String clientid ) {
		
		FindCashSqlUnitOfWork work = new FindCashSqlUnitOfWork( clientid );
		work.execute();
		return work.get();
		
	}
	
	public List<PaymentmeanRecord> findPaymentMeansForClient ( String id ) {
		FindPaymentmeanByClientIdSqlUnitOfWork work = new FindPaymentmeanByClientIdSqlUnitOfWork(id);
		work.execute();
		return work.get();
	}

	public Optional<PaymentmeanRecord> findById(String id) {
		FindPaymentmeanByIdSqlUnitOfWork work = new FindPaymentmeanByIdSqlUnitOfWork( id );
		work.execute();
		return work.get();

	}


}
