package uo.ri.cws.application.ui.manager.action;

import java.util.List;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.BusinessFactory;
import uo.ri.cws.application.business.mechanic.MechanicDto;
import uo.ri.cws.application.business.util.Printer;

public class FindAllMechanicsAction implements Action {

	
	
	@Override
	public void execute() throws BusinessException {

		Console.println("\nList of mechanics \n");  

		List<MechanicDto> mechanics = BusinessFactory.forMechanicCrudService().findAllMechanics();
		
		Printer.printMechanics(mechanics);
		
//		for(MechanicDto mechanic: mechanics) { // We use printer
//			Console.printf("\t %s %s %s %s \n", mechanic.id, mechanic.dni, mechanic.name, mechanic.surname);
//		}
	}
}
