package uo.ri.cws.application.ui.foreman;

import alb.util.menu.BaseMenu;
import alb.util.menu.NotYetImplementedAction;

public class ClientMenu extends BaseMenu {

	public ClientMenu() {
		menuOptions = new Object[][] { 
			{ "Foreman > Customer management", null },

			{ "Add customer", NotYetImplementedAction.class }, 
			{ "Update customer", NotYetImplementedAction.class }, 
			{ "Delete customer", NotYetImplementedAction.class }, 
			{ "List customers", NotYetImplementedAction.class }, 
		};
	}

}
