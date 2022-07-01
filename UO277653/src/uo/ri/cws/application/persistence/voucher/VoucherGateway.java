package uo.ri.cws.application.persistence.voucher;

import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;

public interface VoucherGateway extends Gateway<VoucherRecord> {
	
	public Optional<VoucherRecord> findByCode(String code);
	
}
