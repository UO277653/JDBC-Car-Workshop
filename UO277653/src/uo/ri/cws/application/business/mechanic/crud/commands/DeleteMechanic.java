package uo.ri.cws.application.business.mechanic.crud.commands;

import java.util.Optional;

import alb.util.assertion.Argument;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.util.command.Command;
import uo.ri.cws.application.persistence.PersistenceFactory;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicRecord;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.cws.application.persistence.workorder.WorkOrderRecord;

public class DeleteMechanic implements Command<Void>{

	
	private String idMechanic;
	private MechanicGateway mg = PersistenceFactory.forMechanic();
	private WorkOrderGateway wog = PersistenceFactory.forWorkOrder();
	
	public DeleteMechanic(String arg) {
		// validate is not empty
		Argument.isNotNull(arg);
		Argument.isTrue(!arg.trim().isEmpty());
		
		idMechanic = arg;
	}
	
	public Void execute() throws BusinessException{

		if(!existMechanic(idMechanic)) {
			throw new BusinessException("Mechanic does not exist, so we cannot delete it");
		}
		
		if(hasWorkOrdersAssociated(idMechanic)) {
			throw new BusinessException("Mechanic has work orders associated");
		}
		
		mg.remove(idMechanic);
		
		return null;
	}
	
	private boolean existMechanic(String id) {

		Optional<MechanicRecord> omr = mg.findById(id);
		if (omr.isPresent())
			return true;
		else
			return false;
	}
	
	private boolean hasWorkOrdersAssociated(String id) {
		
		Optional<WorkOrderRecord> omr = wog.findByMechanicId(id);
		
		if (omr.isPresent())
			return true;
		else
			return false;
	}
}
