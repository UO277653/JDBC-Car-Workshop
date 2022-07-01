package uo.ri.cws.application.persistence.charge;

import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;

public interface ChargeGateway extends Gateway<ChargeRecord>{

	Optional<ChargeRecord> findByPaymentMeanId(String id);
}
