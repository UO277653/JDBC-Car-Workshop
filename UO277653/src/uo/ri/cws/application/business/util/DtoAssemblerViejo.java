package uo.ri.cws.application.business.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.business.invoice.InvoicingWorkOrderDto;
import uo.ri.cws.application.business.mechanic.MechanicDto;

public class DtoAssemblerViejo {

	public static MechanicDto toMechanicDto(ResultSet m) throws SQLException {
		MechanicDto dto = new MechanicDto();
		dto.id = m.getString("id");
		//dto.version = m.getLong("version");

		dto.dni = m.getString("dni");
		dto.name = m.getString("name");
		dto.surname = m.getString("surname");
		return dto;
	}

	public static List<MechanicDto> toMechanicDtoList(ResultSet rs) throws SQLException {
		List<MechanicDto> res = new ArrayList<>();
		while(rs.next()) {
			res.add( toMechanicDto( rs ) );
		}

		return res;
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


}
