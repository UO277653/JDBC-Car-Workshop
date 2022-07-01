package uo.ri.cws.application.persistence.paymentmean;

import java.util.List;

import uo.ri.cws.application.persistence.Gateway;

public interface PaymentMeanGateway extends Gateway<PaymentmeanRecord> {

	List<PaymentmeanRecord> findByClientId(String id);
	
	List<PaymentmeanRecord> findByClientIdVouchers(String id);
}
