package uo.ri.cws.application.service.util;

import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;

import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.mechanic.MechanicDto;
import uo.ri.cws.application.business.mechanic.crud.commands.AddMechanic;
import uo.ri.cws.application.business.mechanic.crud.commands.FindMechanicById;
import uo.ri.cws.application.business.util.command.CommandExecutor;

public class MechanicUtil {

	private MechanicDto dto = createDefaultDto();

	public MechanicUtil unique() {
		this.dto.dni = RandomStringUtils.randomAlphanumeric( 9 );
		this.dto.name = RandomStringUtils.randomAlphabetic(4) + "-name";
		this.dto.surname = RandomStringUtils.randomAlphabetic(4) + "-surname";
		return this;
	}

	public MechanicUtil withId(String arg) {
		this.dto.id = arg;
		return this;
	}
	
	public MechanicUtil withDni(String arg) {
		this.dto.dni = arg;
		return this;
	}

	public MechanicUtil withName(String name) {
		this.dto.name = name;
		return this;
	}

	public MechanicUtil withSurname(String arg) {
		this.dto.surname= arg;
		return this;
	}

	public MechanicUtil loadById(String id) throws BusinessException {
		this.dto = new CommandExecutor().execute( new FindMechanicById(id) )
				.orElse(null);
		return this;
	}

//	public MechanicUtil loadByDni(String dni) throws BusinessException, SQLException {
//	this.dto = new CommandExecutor().execute(new FindByNIF(nif))
//			.orElse(null);
//	return this;
//}
	
	public MechanicUtil register() throws BusinessException {
		
		dto.id = new CommandExecutor().execute( new AddMechanic(this.dto) ).id;
		return this;
	}

	public MechanicUtil registerIfNew() throws BusinessException {
		Optional<MechanicDto> op = 
				new CommandExecutor().execute( new FindMechanicById(dto.id) ); 
		if ( op.isEmpty() ) {
			register();
		}
		else {
			dto.id = op.get().id;
		}
		return this;
	}

	public MechanicDto get() {
		return dto;
	}

	private MechanicDto createDefaultDto() {
		MechanicDto dto = new MechanicDto();
		dto.name = "dummy-mechanic-name";
		dto.dni = "dummy-mechanic-dni";
		dto.surname = "dummy-mechanic-surname";
		return dto;
	}

}
