package uo.ri.cws.application.business.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; 
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.business.invoice.InvoiceDto;
import uo.ri.cws.application.business.invoice.InvoicingWorkOrderDto;
//import uo.ri.cws.application.business.invoice.InvoiceDto;
//import uo.ri.cws.application.business.invoice.InvoiceDto.InvoiceStatus;
//import uo.ri.cws.application.business.invoice.InvoicingWorkOrderDto;
import uo.ri.cws.application.business.mechanic.MechanicDto;
import uo.ri.cws.application.business.paymentmean.CardDto;
import uo.ri.cws.application.business.paymentmean.CashDto;
import uo.ri.cws.application.business.paymentmean.PaymentMeanDto;
import uo.ri.cws.application.business.paymentmean.voucher.VoucherDto;
import uo.ri.cws.application.persistence.creditcard.CreditCardRecord;
import uo.ri.cws.application.persistence.invoice.InvoiceRecord;
//import uo.ri.cws.application.persistence.invoice.InvoiceRecord;
import uo.ri.cws.application.persistence.mechanic.MechanicRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentmeanRecord;
import uo.ri.cws.application.persistence.voucher.VoucherRecord;
//import uo.ri.cws.application.persistence.workorder.WorkOrderRecord;
import uo.ri.cws.application.persistence.workorder.WorkOrderRecord;

public class DtoAssembler {

	public static Optional<MechanicDto> toDto(Optional<MechanicRecord> arg) {
		Optional<MechanicDto> result = arg.isEmpty()?Optional.ofNullable(null)
				:Optional.ofNullable(toMechanicDto(arg.get()));
		return result;
	}

	public static List<MechanicDto> toDtoList(List<MechanicRecord> arg) {
		List<MechanicDto> result = new ArrayList<MechanicDto> ();
		for (MechanicRecord mr : arg) 
			result.add(toMechanicDto(mr));
		return result;
	}
	
	public static List<MechanicDto> toMechanicDtoList(ResultSet rs) throws SQLException {
		List<MechanicDto> res = new ArrayList<>();
		while(rs.next()) {
			res.add( toMechanicDto( rs ) );
		}

		return res;
	}
	
	public static MechanicDto toMechanicDto(ResultSet m) throws SQLException {
		MechanicDto dto = new MechanicDto();
		dto.id = m.getString("id");
		dto.dni = m.getString("dni");
		dto.name = m.getString("name");
		dto.surname = m.getString("surname");
		return dto;
	}
	
	public static InvoicingWorkOrderDto toWorkOrderForInvoicingDto(ResultSet rs) throws SQLException {
		InvoicingWorkOrderDto dto = new InvoicingWorkOrderDto();

		dto.id = rs.getString("id");
		dto.description = rs.getString("Description");
		dto.date =  rs.getTimestamp("date").toLocalDateTime();
		dto.total = rs.getDouble("amount");
		dto.status = rs.getString("status");

		return dto;
	}
	
	public static MechanicRecord toRecord(MechanicDto arg) {
		MechanicRecord result = new MechanicRecord ();
		result.id = arg.id;
		result.dni = arg.dni;
		result.name = arg.name;
		result.surname = arg.surname;
		return result;
	}
	
	public static InvoiceRecord toRecord(InvoiceDto arg) {
		InvoiceRecord result = new InvoiceRecord ();
		result.id = arg.id;
		result.number = arg.number;
		result.date = arg.date;
		result.vat = arg.vat;
		result.amount = arg.total;
		result.status = "NOT_YET_PAID";
		return result;
	}

	private static MechanicDto toMechanicDto(MechanicRecord arg) {

		MechanicDto result = new MechanicDto();
		result.id = arg.id;
		result.name = arg.name;
		result.surname = arg.surname;
		result.dni = arg.dni;
		return result;
	}
	


	public static InvoiceDto toDto(InvoiceRecord arg) {
		InvoiceDto result = new InvoiceDto();
		result.id = arg.id;
		result.number = arg.number;
		result.status = arg.status;
		result.date = arg.date;
		result.total = arg.amount;
		result.vat = arg.vat;
		return result;
	}

	public static List<InvoicingWorkOrderDto> toInvoicingWorkOrderList(List<WorkOrderRecord> arg) {
		List<InvoicingWorkOrderDto> result = new ArrayList<InvoicingWorkOrderDto> ();
		for (WorkOrderRecord record : arg) 
			result.add(toDto(record));
		return result;
	}
	
	private static InvoicingWorkOrderDto toDto(WorkOrderRecord record) {
		InvoicingWorkOrderDto dto = new InvoicingWorkOrderDto();
		dto.id = record.id;
		dto.date = record.date;
		dto.description = record.description;
		dto.date = record.date;
		dto.status = record.status;
		dto.total = record.amount;
		
		return dto;
	}

	public static List<VoucherDto> toDto(List<VoucherRecord> arg) {
		List<VoucherDto> result = new ArrayList<VoucherDto> ();
		for (VoucherRecord vr : arg) 
			result.add(toVoucherDto(vr));
		return result;
	}

	private static VoucherDto toVoucherDto(VoucherRecord vr) {
		VoucherDto result = new VoucherDto();
		
		result.id = vr.id;
		result.version = vr.version;

		result.accumulated = vr.accumulated;
		result.balance = vr.available;
		result.clientId = vr.client_id;
		result.code = vr.code;
		result.description = vr.description;
		
		return result;
	}

	public static CreditCardRecord toRecord(CardDto card) {
		CreditCardRecord result = new CreditCardRecord ();
		result.id = card.id;
		result.accumulated = card.accumulated;
		result.client_id = card.clientId;
		result.dtype = "CARD";
		result.number = card.cardNumber;
		result.type = card.cardType;
		result.validthru = card.cardExpiration;
		result.version = card.version;
		
		return result;
	}

	public static Optional<PaymentMeanDto> toDtoPaymentMean(Optional<PaymentmeanRecord> id) {
		Optional<PaymentMeanDto> result = id.isEmpty()?Optional.ofNullable(null)
				:Optional.ofNullable(toPaymentMeanDto(id.get()));
		return result;
	}
	
	private static PaymentMeanDto toPaymentMeanDto(PaymentmeanRecord paymentmeanRecord) {
		
		if(paymentmeanRecord.dtype.toLowerCase().equals("card")) {
			return toCreditCardDto(paymentmeanRecord);
		} else if(paymentmeanRecord.dtype.toLowerCase().equals("cash")) {
			return toCashDto(paymentmeanRecord);
		} else if(paymentmeanRecord.dtype.toLowerCase().equals("voucher")) {
			return toVoucherDto(paymentmeanRecord);
		} else {
			return null;
		}
	}
	
	private static PaymentMeanDto toVoucherDto(PaymentmeanRecord voucherRecord) {
		
		VoucherDto result = new VoucherDto();
		
		result.accumulated = voucherRecord.accumulated;
		result.clientId = voucherRecord.client_id;
		result.id = voucherRecord.id;
		result.version = voucherRecord.version;
		
		return result;
	}

	private static PaymentMeanDto toCashDto(PaymentmeanRecord cashRecord) {
		CashDto result = new CashDto();
		
		result.accumulated = cashRecord.accumulated;
		result.clientId = cashRecord.client_id;
		result.id = cashRecord.id;
		result.version = cashRecord.version;
		
		return result;
	}

	private static PaymentMeanDto toCreditCardDto(PaymentmeanRecord creditCardRecord) {
		
		
		CardDto result = new CardDto();
		
		result.accumulated = creditCardRecord.accumulated;
		result.clientId = creditCardRecord.client_id;
		result.id = creditCardRecord.id;
		result.version = creditCardRecord.version;
		
		return result;
	}

	public static Optional<PaymentMeanDto> toDtoVoucher(Optional<VoucherRecord> voucherRecord) {
		VoucherDto result = new VoucherDto();
		
		result.accumulated = voucherRecord.get().accumulated;
		result.clientId = voucherRecord.get().client_id;
		result.id = voucherRecord.get().id;
		result.version = voucherRecord.get().version;
		result.balance = voucherRecord.get().available;
		result.code = voucherRecord.get().code;
		result.description = voucherRecord.get().description;
		
		return Optional.ofNullable(result);
	}

	public static Optional<PaymentMeanDto> toDtoCreditCard(Optional<CreditCardRecord> creditCardRecord) {
		CardDto result = new CardDto();
		
		result.accumulated = creditCardRecord.get().accumulated;
		result.clientId = creditCardRecord.get().client_id;
		result.id = creditCardRecord.get().id;
		result.version = creditCardRecord.get().version;
		result.cardExpiration = creditCardRecord.get().validthru;
		result.cardNumber = creditCardRecord.get().number;
		result.cardType = creditCardRecord.get().type;
		
		return Optional.ofNullable(result);
	}

	public static VoucherRecord toRecord(VoucherDto voucher) {
		VoucherRecord result = new VoucherRecord();
		result.id = voucher.id;
		result.accumulated = voucher.accumulated;
		result.client_id = voucher.clientId;
		result.dtype = "VOUCHER";
		result.available = voucher.balance;
		result.code = voucher.code;
		result.description = voucher.description;
		result.version = voucher.version;
		
		return result;
	}

	public static List<PaymentMeanDto> toDtoListPaymentMean(List<PaymentmeanRecord> arg) {
		List<PaymentMeanDto> result = new ArrayList<PaymentMeanDto> ();
		for (PaymentmeanRecord pmr : arg) 
			result.add(toPaymentMeanDto(pmr));
		return result;
	}

	public static VoucherDto toDtoVoucher(VoucherRecord voucher) {
		
		VoucherDto result = new VoucherDto();
		result.id = voucher.id;
		result.balance = voucher.available;
		result.code = voucher.code;
		result.description = voucher.description;
		
		return result;
	}

	public static InvoicingWorkOrderDto toInvoicingDtoFromWorkOrder(WorkOrderRecord rec) {
		
		InvoicingWorkOrderDto result = new InvoicingWorkOrderDto();
		
		result.date = rec.date;
		result.description = rec.description;
		result.id = rec.id;
		result.status = rec.status;
		result.total = rec.amount;
		
		return result;
	}

	
}
