package uo.ri.cws.application.persistence.client;

import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;

public interface ClientGateway extends Gateway<ClientRecord> {

	Optional<ClientRecord> findByDni(String dni);
}
