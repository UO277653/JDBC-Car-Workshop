package uo.ri.cws.application.ui.manager.action;


import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.BusinessFactory;

public class DeleteMechanicAction implements Action {

	// private static String SQL = "delete from TMechanics where id = ?";

	@Override
	public void execute() throws BusinessException {
		String idMechanic = Console.readString("Type mechanic id "); 
		
		BusinessFactory.forMechanicCrudService().deleteMechanic(idMechanic);
		
		Console.println("Mechanic deleted");
	}

}
