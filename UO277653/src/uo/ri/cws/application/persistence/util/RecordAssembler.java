package uo.ri.cws.application.persistence.util;

import java.sql.ResultSet; 
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.charge.ChargeRecord;
import uo.ri.cws.application.persistence.client.ClientRecord;
import uo.ri.cws.application.persistence.creditcard.CreditCardRecord;
import uo.ri.cws.application.persistence.invoice.InvoiceRecord;
import uo.ri.cws.application.persistence.mechanic.MechanicRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentmeanRecord;
import uo.ri.cws.application.persistence.recommendation.RecommendationRecord;
import uo.ri.cws.application.persistence.vehicle.VehicleRecord;
import uo.ri.cws.application.persistence.voucher.VoucherRecord;
import uo.ri.cws.application.persistence.workorder.WorkOrderRecord;
//import uo.ri.cws.application.persistence.workorder.WorkOrderRecord;

public class RecordAssembler {

	public static Optional<MechanicRecord> toMechanicRecord(ResultSet m) throws SQLException {
		if (m.next()) {
			return Optional.of(resultSetToMechanicRecord(m));
		}
		else 	
			return Optional.ofNullable(null);
	}
	

	public static List<MechanicRecord> toMechanicRecordList(ResultSet rs) throws SQLException {
		List<MechanicRecord> res = new ArrayList<>();
		while(rs.next()) {
			res.add( resultSetToMechanicRecord(rs));
		}

		return res;
	}

	private static MechanicRecord resultSetToMechanicRecord(ResultSet rs) throws SQLException {
		MechanicRecord value = new MechanicRecord();
		value.id = rs.getString("id");

		value.dni = rs.getString("dni");
		value.name = rs.getString("name");
		value.surname = rs.getString("surname");
		return value;
	}

	public static Optional<WorkOrderRecord> toWorkOrderRecord ( ResultSet rs ) throws SQLException {
		WorkOrderRecord record = null;
		
		if (rs.next()) {
			record = resultSetToWorkOrderRecord(rs);
			}
		return Optional.ofNullable(record);
		
	}
	
	private static WorkOrderRecord resultSetToWorkOrderRecord ( ResultSet rs ) throws SQLException {
		WorkOrderRecord record = new WorkOrderRecord();
		
		record.id = rs.getString("id");
		record.version = rs.getLong("version");

		record.vehicle_id = rs.getString( "vehicle_Id"); // aqui cambie de vehicle_id a vehicleId
		record.description = rs.getString( "description");
		record.date =  rs.getTimestamp("date").toLocalDateTime();
		record.amount = rs.getDouble("amount");
		record.status = rs.getString( "status");
		record.mechanic_id = rs.getString( "mechanic_Id");
		record.invoice_id = rs.getString( "invoice_Id");
		record.usedForVoucher = rs.getBoolean("usedForVoucher");
		
		return record;		
	}
	
	
	public static Optional<ClientRecord> toClientRecord(ResultSet m) throws SQLException {
		if (m.next()) {
			return Optional.of(resultSetToClientRecord(m));
		}
		else 	
			return Optional.ofNullable(null);
	}
	
	private static ClientRecord resultSetToClientRecord ( ResultSet rs ) throws SQLException {
		ClientRecord record = new ClientRecord();
		
		record.id = rs.getString("id");
		record.version = rs.getLong("version");

		record.dni = rs.getString( "dni"); 
		record.name = rs.getString( "name");
		record.surname =  rs.getString( "surname");
		record.phone = rs.getString( "phone");
		record.email = rs.getString( "email");
		record.addressStreet =rs.getString( "street");
		record.addressCity = rs.getString( "city");
		record.addressZipcode = rs.getString( "zipcode");
		
		return record;		
	}


	public static List<VoucherRecord> toVoucherRecord(ResultSet rs) throws SQLException {
		List<VoucherRecord> result = new ArrayList<VoucherRecord>();
				
		while (rs.next()) {
			result.add(resultSetToVoucherRecord(rs));
		}
		
		return result;
	}
	
	public static Optional<VoucherRecord> toVoucherRecordOptional(ResultSet rs) throws SQLException {
		if (rs.next()) {
			return Optional.of(resultSetToVoucherRecordGateway(rs));
		}
		else 	
			return Optional.ofNullable(null);
	}
	
	public static Optional<VoucherRecord> toVoucherRecordOptionalGateway(ResultSet rs) throws SQLException {
		if (rs.next()) {
			return Optional.of(resultSetToVoucherRecordGateway(rs));
		}
		else 	
			return Optional.ofNullable(null);
	}
	
	private static VoucherRecord resultSetToVoucherRecord ( ResultSet rs ) throws SQLException {
		VoucherRecord record = new VoucherRecord();
		
		record.id = rs.getString("id");
		record.version = rs.getLong("version");

		record.accumulated = rs.getDouble("accumulated");
		record.available = rs.getDouble("available");
		record.client_id = rs.getString("client_id");
		record.code = rs.getString("code");
		record.description = rs.getString("description");
		record.dtype = rs.getString("dtype");
		
		return record;		
	}
	
	// Used in vouchergateway (only has the fields of the table)
	private static VoucherRecord resultSetToVoucherRecordGateway ( ResultSet rs ) throws SQLException {
		VoucherRecord record = new VoucherRecord();
		
		record.id = rs.getString("id");
		record.available = rs.getDouble("available");
		record.code = rs.getString("code");
		record.description = rs.getString("description");
		
		return record;		
	}

	public static Optional<CreditCardRecord> toCreditCardRecord(ResultSet rs) throws SQLException {
		if (rs.next()) {
			return Optional.of(resultSetToCreditCardRecord(rs));
		}
		else 	
			return Optional.ofNullable(null);
	}

	private static CreditCardRecord resultSetToCreditCardRecord(ResultSet rs) throws SQLException {
		CreditCardRecord record = new CreditCardRecord();
		
		record.id = rs.getString("id");
		record.number = rs.getString("number");
		record.type = rs.getString("type");
		record.validthru = rs.getDate("validthru").toLocalDate();
		
		return record;	
	}

	public static Optional<PaymentmeanRecord> toPaymentmeanRecord(ResultSet rs) throws SQLException {
		if (rs.next()) {
			return Optional.of(resultSetToPaymentmeanRecord(rs));
		}
		else 	
			return Optional.ofNullable(null);
	}

	private static PaymentmeanRecord resultSetToPaymentmeanRecord(ResultSet rs) throws SQLException {
		
			PaymentmeanRecord record = new PaymentmeanRecord();
			record.accumulated = rs.getDouble("accumulated");
			record.client_id = rs.getString("client_id");
			record.dtype = rs.getString("dtype");
			record.id = rs.getString("id");
			record.version = rs.getLong("version");
			
			return record;
	}

	public static Optional<ChargeRecord> toChargeRecord(ResultSet rs) throws SQLException {
		if (rs.next()) {
			return Optional.of(resultSetToChargeRecord(rs));
		}
		else 	
			return Optional.ofNullable(null);
	}

	private static ChargeRecord resultSetToChargeRecord(ResultSet rs) throws SQLException {
		ChargeRecord value = new ChargeRecord();
		value.id = rs.getString("id");
		value.amount = rs.getDouble("amount");
		value.version = rs.getLong("version");
		value.invoice_id = rs.getString("invoice_id");
		value.paymentMean_id = rs.getString("paymentmean_id");
		
		return value;
	}

	public static List<PaymentmeanRecord> toPaymentMeanRecordList(ResultSet rs) throws SQLException {
		List<PaymentmeanRecord> res = new ArrayList<>();
		
		PaymentmeanRecord record;
		
		while(rs.next()) {
			record = new PaymentmeanRecord();
			record.accumulated = rs.getDouble("accumulated");
			record.client_id = rs.getString("client_id");
			record.dtype = rs.getString("dtype");
			record.id = rs.getString("id");
			record.version = rs.getLong("version");
			res.add(record);
		}

		return res;
	}

	public static List<ClientRecord> toClientRecordList(ResultSet rs) throws SQLException {
		List<ClientRecord> res = new ArrayList<>();
		ClientRecord record;
		
		while(rs.next()) {
			
			record = new ClientRecord();
			
			record.id = rs.getString("id");
			record.version = rs.getLong("version");

			record.dni = rs.getString( "dni"); 
			record.name = rs.getString( "name");
			record.surname =  rs.getString( "surname");
			record.phone = rs.getString( "phone");
			record.email = rs.getString( "email");
			record.addressStreet =rs.getString( "street");
			record.addressCity = rs.getString( "city");
			record.addressZipcode = rs.getString( "zipcode");
			res.add(record);
		}

		return res;
	}

	public static List<RecommendationRecord> toRecommendationRecordList(ResultSet rs) throws SQLException {
		List<RecommendationRecord> res = new ArrayList<>();
		
		RecommendationRecord record;
		
		while(rs.next()) {
			record = new RecommendationRecord();
			record.id = rs.getString("id");
			record.version = rs.getLong("version");
			record.sponsor_id = rs.getString("sponsor_id");
			record.recommended_id = rs.getString("recommended_id");
			record.usedForVoucher = rs.getBoolean("usedForVoucher");
			res.add(record);
		}

		return res;
	}

	public static List<VehicleRecord> toVehicleRecordList(ResultSet rs) throws SQLException {
		List<VehicleRecord> res = new ArrayList<>();
		
		VehicleRecord record;
		
		while(rs.next()) {
			record = new VehicleRecord();
			record.id = rs.getString("id");
			record.version = rs.getLong("version");
			record.client_id = rs.getString("client_id");
			record.make = rs.getString("make");
			record.model = rs.getString("model");
			record.platenumber = rs.getString("platenumber");
			record.vehicletype_id = rs.getString("vehicletype_id");
			
			res.add(record);
		}

		return res;
	}

	public static List<WorkOrderRecord> toWorkOrderRecordList(ResultSet rs) throws SQLException {
		List<WorkOrderRecord> res = new ArrayList<>();
		while(rs.next()) {
			res.add( resultSetToWorkOrderRecord(rs));
		}

		return res;
	}

	public static Optional<InvoiceRecord> toInvoiceRecord(ResultSet rs) throws SQLException {
		if (rs.next()) {
			return Optional.of(resultSetToInvoiceRecord(rs));
		}
		else 	
			return Optional.ofNullable(null);
	}

	private static InvoiceRecord resultSetToInvoiceRecord(ResultSet rs) throws SQLException {
		InvoiceRecord value = new InvoiceRecord();
		value.id = rs.getString("id");
		value.amount = rs.getDouble("amount");
		value.date = rs.getDate("date").toLocalDate();
		value.number = rs.getLong("number");
		value.status = rs.getString("status");
		value.usedforvoucher = rs.getBoolean("usedforvoucher");
		value.vat = rs.getDouble("vat");
		value.version = rs.getLong("version");
		
		return value;
	}
	

}
