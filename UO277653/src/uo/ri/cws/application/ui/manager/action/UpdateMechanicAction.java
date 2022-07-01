package uo.ri.cws.application.ui.manager.action;


import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.BusinessFactory;
import uo.ri.cws.application.business.mechanic.MechanicDto;

public class UpdateMechanicAction implements Action {


	@Override
	public void execute() throws BusinessException {
		
		// Get info
		String id = Console.readString("Type mechahic id to update"); 
		String name = Console.readString("Name"); 
		String surname = Console.readString("Surname");
		
		MechanicDto mechanic = new MechanicDto();
		
		mechanic.id = id;
		mechanic.name = name;
		mechanic.surname = surname;
		
		BusinessFactory.forMechanicCrudService().updateMechanic(mechanic);
		
		// Print result
		Console.println("Mechanic updated");
	}

}
