package uo.ri.cws.application.persistence.mechanic.impl;

import java.sql.Connection;  
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import alb.util.jdbc.Jdbc;
// import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicRecord;
import uo.ri.cws.application.persistence.util.Conf;
import uo.ri.cws.application.persistence.util.RecordAssembler;

public class MechanicGatewayImpl implements MechanicGateway {
	
	Conf conf = Conf.getInstance();
	
	@Override
	public void add(MechanicRecord mechanic) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;

		try {
			c = Jdbc.getCurrentConnection(); // we share the connection
			//
			pst = c.prepareStatement(conf.getProperty("TMECHANICS_ADD"));
			pst.setString(1, mechanic.id);
			pst.setString(2, mechanic.dni);
			pst.setString(3, mechanic.name);
			pst.setString(4, mechanic.surname);
			
			pst.executeUpdate();
			//
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
		finally {
			Jdbc.close(rs, pst);
		}
	}

	@Override
	public void remove(String id) {
		
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			c = Jdbc.getConnection();
			
			pst = c.prepareStatement(conf.getProperty("TMECHANICS_REMOVE"));
			pst.setString(1, id);
			
			pst.executeUpdate();
			
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		finally {
			Jdbc.close(rs, pst, c);
		}
	}

	@Override
	public void update(MechanicRecord mechanic) {
		
		// Process
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			c = Jdbc.getConnection();
			
			pst = c.prepareStatement(conf.getProperty("TMECHANICS_UPDATE"));
			pst.setString(1, mechanic.name);
			pst.setString(2, mechanic.surname);
			pst.setString(3, mechanic.id);
			
			pst.executeUpdate();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		finally {
			Jdbc.close(rs, pst, c);
		}
		
	}

	@Override
	public Optional<MechanicRecord> findById(String id) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		Optional<MechanicRecord> result = null;
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TMECHANICS_FINDBYID"));
			pst.setString(1, id);
			rs = pst.executeQuery();
			
			result = RecordAssembler.toMechanicRecord(rs);
			
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} 
		finally {
			Jdbc.close(rs,pst);
		}
		
		return result;
	}

	@Override
	public List<MechanicRecord> findAll() {
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		List<MechanicRecord> res = new ArrayList<>();
		
		try {
			c = Jdbc.getConnection();
			
			pst = c.prepareStatement(conf.getProperty("TMECHANICS_FINDALL"));
			
			rs = pst.executeQuery();
			
			MechanicRecord mechanic;
			
			//res = DtoAssembler.toMechanicDtoList(rs);
			
			while(rs.next()) {
				mechanic = new MechanicRecord();	
				mechanic.id = rs.getString(1);
				mechanic.dni = rs.getString("dni");
				mechanic.name = rs.getString("name");
				mechanic.surname = rs.getString(4);			
				res.add(mechanic);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		finally {
			Jdbc.close(rs, pst, c);
		}
		
		return res;
	}

	@Override
	public Optional<MechanicRecord> findByDni(String dni) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		Optional<MechanicRecord> result = null;
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TMECHANICS_FINDBYDNI"));
			pst.setString(1, dni);
			rs = pst.executeQuery();
			
			result = RecordAssembler.toMechanicRecord(rs);
			
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} 
		finally {
			Jdbc.close(rs,pst);
		}
		
		return result;
	}

}
