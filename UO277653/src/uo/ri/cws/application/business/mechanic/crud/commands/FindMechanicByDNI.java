package uo.ri.cws.application.business.mechanic.crud.commands;

import java.util.Optional;

import alb.util.assertion.Argument;
import uo.ri.cws.application.business.mechanic.MechanicDto;
import uo.ri.cws.application.business.util.DtoAssembler;
import uo.ri.cws.application.business.util.command.Command;
import uo.ri.cws.application.persistence.PersistenceFactory;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;

public class FindMechanicByDNI implements Command<Optional<MechanicDto>>{

	private MechanicGateway mg = PersistenceFactory.forMechanic();
	private String dniMechanic;
	
	public FindMechanicByDNI(String arg) {
		
		Argument.isNotNull(arg);
		Argument.isTrue(!arg.trim().isEmpty());
		
		dniMechanic = arg;
	}

	public Optional<MechanicDto> execute() {

		return DtoAssembler.toDto(mg.findByDni(dniMechanic));
		
	}
	
}
