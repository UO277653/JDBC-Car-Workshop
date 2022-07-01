package uo.ri.cws.application.persistence.client.impl;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import alb.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientRecord;
import uo.ri.cws.application.persistence.util.Conf;
import uo.ri.cws.application.persistence.util.RecordAssembler;

public class ClientGatewayImpl implements ClientGateway{

	Conf conf = Conf.getInstance();
	
	@Override
	public void add(ClientRecord t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ClientRecord t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<ClientRecord> findById(String id) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		Optional<ClientRecord> result = null;
		
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TCLIENTS_FINDBYID"));
			pst.setString(1, id);
			rs = pst.executeQuery();
			
			result = RecordAssembler.toClientRecord(rs);
			
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} 
		finally {
			Jdbc.close(rs,pst);
		}
		
		return result;
	}

	@Override
	public List<ClientRecord> findAll() {
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		List<ClientRecord> res = new ArrayList<>();
		
		try {
			c = Jdbc.getConnection();
			
			pst = c.prepareStatement(conf.getProperty("TCLIENTS_FINDALL"));
			
			rs = pst.executeQuery();
			
			res = RecordAssembler.toClientRecordList(rs);
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		finally {
			Jdbc.close(rs, pst, c);
		}
		
		return res;
	}

	@Override
	public Optional<ClientRecord> findByDni(String dni) {

		
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		Optional<ClientRecord> result = null;
		
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TCLIENTS_FINDBYDNI"));
			pst.setString(1, dni);
			rs = pst.executeQuery();
			
			result = RecordAssembler.toClientRecord(rs);
			
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} 
		finally {
			Jdbc.close(rs,pst);
		}
		
		return result;
	}

}
