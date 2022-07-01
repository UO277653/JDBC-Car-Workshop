package uo.ri.cws.application.ui.manager.action;

import alb.util.console.Console; 
import alb.util.menu.Action;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.BusinessFactory;
import uo.ri.cws.application.business.mechanic.MechanicCrudService;
import uo.ri.cws.application.business.mechanic.MechanicDto;

public class AddMechanicAction implements Action {

	@Override
	public void execute() throws BusinessException {
		
		// Get info
		String dni = Console.readString("Dni"); 
		String name = Console.readString("Name"); 
		String surname = Console.readString("Surname");
		
		// Process
		MechanicDto mechanic = new MechanicDto();
		
		mechanic.name = name;
		mechanic.surname = surname;
		mechanic.dni = dni;
		
		// new AddMechanic(mechanic).execute(); // We delete it to remove coupling
		// MechanicCrudServiceImpl service = new MechanicCrudServiceImpl(); For factory
		
		MechanicCrudService service = BusinessFactory.forMechanicCrudService();
		
		service.addMechanic(mechanic);
		
		// Print result
		Console.println("Mechanic added");
	}

}
