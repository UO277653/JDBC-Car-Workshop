package uo.ri.cws.application.persistence.workorder;

import java.time.LocalDateTime;

public class WorkOrderRecord {

	public String id;
	public long version;

	public String vehicle_id;
	public String description;
	public LocalDateTime date;
	public double amount;
	public String status;
	public boolean usedForVoucher;

	// might be null
	public String mechanic_id;
	public String invoice_id;
}
