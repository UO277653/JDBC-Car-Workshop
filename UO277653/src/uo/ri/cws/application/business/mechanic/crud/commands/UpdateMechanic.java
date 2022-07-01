package uo.ri.cws.application.business.mechanic.crud.commands;

import java.util.Optional;

import alb.util.assertion.Argument;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.mechanic.MechanicDto;
import uo.ri.cws.application.business.util.DtoAssembler;
import uo.ri.cws.application.business.util.command.Command;
import uo.ri.cws.application.persistence.PersistenceFactory;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicRecord;

public class UpdateMechanic implements Command<Void>{

	private MechanicDto mechanic;
	
	private MechanicGateway mg = PersistenceFactory.forMechanic();
	
	public UpdateMechanic(MechanicDto mechanic) {
		Argument.isNotNull(mechanic);
		Argument.isNotNull(mechanic.id);
		Argument.isNotNull(mechanic.dni);
		Argument.isNotNull(mechanic.name);
		Argument.isNotNull(mechanic.surname);
		Argument.isNotEmpty(mechanic.id);
		Argument.isNotEmpty(mechanic.dni);
		Argument.isNotEmpty(mechanic.name);
		Argument.isNotEmpty(mechanic.surname);
		
		this.mechanic = mechanic;
	}

	public Void execute() throws BusinessException {

		if(!existMechanic(mechanic.id)) {
			throw new BusinessException("Mechanic does not exist, so we cannot delete it");
		}
		
		mg.update(DtoAssembler.toRecord(mechanic));
		
		
		return null;
		
	}
	
	private boolean existMechanic(String id) {

		Optional<MechanicRecord> omr = mg.findById(id);
		if (omr.isPresent())
			return true;
		else
			return false;
	}

}
