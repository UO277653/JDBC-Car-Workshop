package uo.ri.cws.application.business.invoice.create.commands;

import java.util.ArrayList; 
import java.util.List;
import java.util.Optional;

import alb.util.assertion.Argument;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.invoice.InvoicingWorkOrderDto;
import uo.ri.cws.application.business.util.DtoAssembler;
import uo.ri.cws.application.business.util.command.Command;
import uo.ri.cws.application.persistence.PersistenceFactory;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientRecord;
import uo.ri.cws.application.persistence.vehicle.VehicleGateway;
import uo.ri.cws.application.persistence.vehicle.VehicleRecord;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.cws.application.persistence.workorder.WorkOrderRecord;

public class FindNotInvoicedWorkOrders implements Command<List<InvoicingWorkOrderDto>>{
	
	private String dni;
	private ClientGateway cg = PersistenceFactory.forClient();
	private VehicleGateway vg = PersistenceFactory.forVehicle();
	private WorkOrderGateway wog = PersistenceFactory.forWorkOrder();
	
	public FindNotInvoicedWorkOrders(String dni) {
		Argument.isNotEmpty(dni);
		this.dni = dni;
	}	
	
	public List<InvoicingWorkOrderDto> execute() throws BusinessException {
		
		if(!existsClient(dni)) {
			throw new BusinessException("Client does not exist");
		}
		
		List<InvoicingWorkOrderDto> res = new ArrayList<InvoicingWorkOrderDto>();
		
		String clientId = cg.findByDni(dni).get().id;
		
		List<VehicleRecord> vehicles = vg.findByClientId(clientId);
		
		for(VehicleRecord v: vehicles) {
			List<WorkOrderRecord> woRecords = wog.findNotInvoicedByVehicleId(v.id);
			
			for(WorkOrderRecord rec: woRecords) {
				res.add(DtoAssembler.toInvoicingDtoFromWorkOrder(rec));
			}
		}
		
		return res;
	}
	
	private boolean existsClient(String dni) {
	
		Optional<ClientRecord> omr = cg.findByDni(dni);
		
		if (omr.isPresent())
			return true;
		else
			return false;
		
	}

}
