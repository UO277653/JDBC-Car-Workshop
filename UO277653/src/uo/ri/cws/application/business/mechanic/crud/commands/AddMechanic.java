package uo.ri.cws.application.business.mechanic.crud.commands;

import java.util.Optional; 
import java.util.UUID;

import alb.util.assertion.Argument;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.mechanic.MechanicDto;
import uo.ri.cws.application.business.util.DtoAssembler;
import uo.ri.cws.application.business.util.command.Command;
import uo.ri.cws.application.persistence.PersistenceFactory;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicRecord;

public class AddMechanic implements Command<MechanicDto>{

	private MechanicDto mechanic;
	private MechanicGateway mg = PersistenceFactory.forMechanic();

	public AddMechanic(MechanicDto arg) {
		Argument.isNotNull(arg);
		Argument.isNotNull(arg.dni);
		Argument.isTrue(!arg.dni.toString().trim().isEmpty());
		Argument.isTrue(!arg.toString().trim().isEmpty());
		
		mechanic = arg;
	}

	public MechanicDto execute() throws BusinessException {

		mechanic.id = UUID.randomUUID().toString();
		if (existMechanic(this.mechanic.dni))
			throw new BusinessException("Mechanic already exist");
		mg.add(DtoAssembler.toRecord(mechanic));
		
		return mechanic;
	}

	private boolean existMechanic(String dni) {

		Optional<MechanicRecord> omr = mg.findByDni(dni);
		if (omr.isPresent())
			return true;
		else
			return false;
	}
}
