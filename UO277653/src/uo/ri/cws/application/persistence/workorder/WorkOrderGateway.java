package uo.ri.cws.application.persistence.workorder;

import java.util.List; 
import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;

public interface WorkOrderGateway extends Gateway<WorkOrderRecord> {

	public Optional<WorkOrderRecord> findByMechanicId(String id);
	
	public boolean checkFinishedWorkOrder(String id);
	
	public double findAmountById(String id);
	
	public void linkWorkOrderToInvoice(String invoiceId, String workOrderId);
	
	public void markWorkOrderAsInvoiced(String workOrderId);
	
	List<WorkOrderRecord> findInvoicedByVehicleId(String id);
	List<WorkOrderRecord> findNotInvoicedByVehicleId(String id);
}
