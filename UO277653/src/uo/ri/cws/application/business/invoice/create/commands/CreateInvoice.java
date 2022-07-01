package uo.ri.cws.application.business.invoice.create.commands;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import alb.util.assertion.Argument;
import alb.util.math.Round;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.invoice.InvoiceDto;
import uo.ri.cws.application.business.util.DtoAssembler;
import uo.ri.cws.application.business.util.command.Command;
import uo.ri.cws.application.persistence.PersistenceFactory;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.cws.application.persistence.workorder.WorkOrderRecord;

public class CreateInvoice implements Command<InvoiceDto>{

	List<String> workOrderIds = new ArrayList<>();
	
	private WorkOrderGateway wog = PersistenceFactory.forWorkOrder();
	private InvoiceGateway ig = PersistenceFactory.forInvoice();
	
	public CreateInvoice(List<String> workOrderIds) {
		
		Argument.isNotNull(workOrderIds);
		Argument.isTrue(!workOrderIds.isEmpty());
		checkNotEmpty(workOrderIds);
		this.workOrderIds = workOrderIds;
	}
	
	private void checkNotEmpty(List<String> workOrderIds) {
		for(String s: workOrderIds) {
			Argument.isNotEmpty(s);
		}
	}

	public InvoiceDto execute() throws BusinessException {
		
		InvoiceDto res = new InvoiceDto();

		if (! checkWorkOrdersExist(workOrderIds) )
			throw new BusinessException ("Workorder does not exist");
		if (! checkWorkOrdersFinished(workOrderIds) )
			throw new BusinessException ("Workorder is not finished yet");

		long numberInvoice = generateInvoiceNumber();
		LocalDate dateInvoice = LocalDate.now();
		double amount = calculateTotalInvoice(workOrderIds); // vat not included
		double vat = vatPercentage(amount, dateInvoice);
		double total = amount * (1 + vat/100); // vat included
		total = Round.twoCents(total);

		String idInvoice = createInvoice(numberInvoice, dateInvoice, vat, total);
		linkWorkordersToInvoice(idInvoice, workOrderIds);
		markWorkOrderAsInvoiced(workOrderIds);
		
		res.id = idInvoice;
		res.total = total;
		res.vat = vat;
		res.number = numberInvoice;
		res.date = dateInvoice;
		res.status = "NOT_YET_PAID";
		
		return res;
	}
	
	/*
	 * checks whether every work order exist	 
	 */
	private boolean checkWorkOrdersExist(List<String> workOrderIDS) {
		
		for(String id: workOrderIDS) {
			Optional<WorkOrderRecord> omr = wog.findById(id);
			if (!omr.isPresent())
				return false;
		}
		
		return true;
	}

	/*
	 * checks whether every work order id is FINISHED	 
	 */
	private boolean checkWorkOrdersFinished(List<String> workOrderIDS) {
		for(String id: workOrderIDS) {
			if(!wog.checkFinishedWorkOrder(id)) {
				return false;
			}
		}
		
		return true;
	}

	/*
	 * Generates next invoice number (not to be confused with the inner id)
	 */
	private Long generateInvoiceNumber() {
		
		return ig.getNextInvoiceNumber();
	}

	/*
	 * Compute total amount of the invoice  (as the total of individual work orders' amount 
	 */
	private double calculateTotalInvoice(List<String> workOrderIDS) {

		double totalInvoice = 0.0;
		for (String workOrderID : workOrderIDS) {
			totalInvoice += getWorkOrderTotal(workOrderID);
		}
		return totalInvoice;
	}

	/*
	 * checks whether every work order id is FINISHED	 
	 */
	private Double getWorkOrderTotal(String workOrderID) {
		
		return wog.findAmountById(workOrderID);
	}

	/*
	 * returns vat percentage 
	 */
	private double vatPercentage(double totalInvoice, LocalDate dateInvoice) {
		return LocalDate.parse("2012-07-01").isBefore(dateInvoice) ? 21.0 : 18.0;

	}

	/*
	 * Creates the invoice in the database; returns the id
	 */
	private String createInvoice(long numberInvoice, LocalDate dateInvoice, 
			double vat, double total) {

		InvoiceDto inv = new InvoiceDto();
		String idInvoice = UUID.randomUUID().toString();
		inv.id = idInvoice;
		inv.number = numberInvoice;
		inv.date = dateInvoice;
		inv.vat = vat;
		inv.total = total;
		inv.status = "NOT_YET_PAID";
		
		ig.add(DtoAssembler.toRecord(inv));
		
		
		return idInvoice; 
	}

	/*
	 * Set the invoice number field in work order table to the invoice number generated
	 */
	private void linkWorkordersToInvoice (String invoiceId, List<String> workOrderIDS) {
		
		for (String breakdownId : workOrderIDS) {
			
			wog.linkWorkOrderToInvoice(invoiceId, breakdownId);
		}
		
	}

	/*
	 * Sets status to INVOICED for every workorder
	 */
	private void markWorkOrderAsInvoiced(List<String> ids) {

		for (String id: ids) {
			wog.markWorkOrderAsInvoiced(id);
		}
	}


	


}
