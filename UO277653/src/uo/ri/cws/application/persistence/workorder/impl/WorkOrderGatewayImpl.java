package uo.ri.cws.application.persistence.workorder.impl;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import alb.util.jdbc.Jdbc;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.util.Conf;
import uo.ri.cws.application.persistence.util.RecordAssembler;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.cws.application.persistence.workorder.WorkOrderRecord;

public class WorkOrderGatewayImpl implements WorkOrderGateway  {

	Conf conf = Conf.getInstance();
	
	
	
	@Override
	public void add(WorkOrderRecord t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(WorkOrderRecord t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<WorkOrderRecord> findByMechanicId(String id) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		Optional<WorkOrderRecord> result = null;
		
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TWORKORDERS_FINDBYMECHANICID"));
			pst.setString(1, id);
			rs = pst.executeQuery();
			
			result = RecordAssembler.toWorkOrderRecord(rs);
			
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} 
		finally {
			Jdbc.close(rs,pst);
		}
		
		return result;
	}
	
	@Override
	public Optional<WorkOrderRecord> findById(String id) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		Optional<WorkOrderRecord> result = null;
		
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TWORKORDERS_FINDBYID"));
			pst.setString(1, id);
			rs = pst.executeQuery();
			
			result = RecordAssembler.toWorkOrderRecord(rs);
			
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} 
		finally {
			Jdbc.close(rs,pst);
		}
		
		return result;
	}

	@Override
	public List<WorkOrderRecord> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkFinishedWorkOrder(String id) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TWORKORDERS_CHECKFINISHEDWORKORDER"));

			pst.setString(1, id);

			rs = pst.executeQuery();
			rs.next();
			String status = rs.getString(1); 
			if (! "FINISHED".equalsIgnoreCase(status) ) {
				return false;
			}

		} catch (SQLException e) {
			throw new PersistenceException(e);
		} finally {
			Jdbc.close(rs, pst);
		}
		return true;
	}

	@Override
	public double findAmountById(String id) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		
		Double money = 0.0;
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TWORKORDERS_FINDAMOUNTBYID"));
			pst.setString(1, id);

			rs = pst.executeQuery();
			if (rs.next() == false) {
				throw new BusinessException("Workorder " + id + " doesn't exist");
			}

			money = rs.getDouble(1); 

		} catch (SQLException | BusinessException e) {
			throw new PersistenceException(e);
		} finally {
			Jdbc.close(rs, pst);
		}
		return money;
	}

	@Override
	public void linkWorkOrderToInvoice(String invoiceId, String workOrderId) {
		
		PreparedStatement pst = null;
		Connection c = null;
		
		try {
			
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TWORKORDERS_LINKWORKORDERTOINVOICE"));
			pst.setString(1, invoiceId);
			pst.setString(2, workOrderId);

			pst.executeUpdate();
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} finally {
			Jdbc.close(pst);
		}
		
	}

	@Override
	public void markWorkOrderAsInvoiced(String workOrderId) {
		PreparedStatement pst = null;
		Connection c = null;
		
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TWORKORDERS_MARKWORKORDERASINVOICED"));

			pst.setString(1, workOrderId);

			pst.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} finally {
			Jdbc.close(pst);
		}
	}

	@Override
	public List<WorkOrderRecord> findInvoicedByVehicleId(String id) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		List<WorkOrderRecord> res = new ArrayList<>();
		
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TWORKORDERS_FINDINVOICEDBYVEHICLEID"));
			pst.setString(1, id);
			rs = pst.executeQuery();
			
			res = RecordAssembler.toWorkOrderRecordList(rs);
			
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} 
		finally {
			Jdbc.close(rs,pst);
		}
		
		return res;
	}

	@Override
	public List<WorkOrderRecord> findNotInvoicedByVehicleId(String id) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		List<WorkOrderRecord> res = new ArrayList<>();
		
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TWORKORDERS_FINDNOTINVOICEDBYVEHICLEID"));
			pst.setString(1, id);
			rs = pst.executeQuery();
			
			res = RecordAssembler.toWorkOrderRecordList(rs);
			
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} 
		finally {
			Jdbc.close(rs,pst);
		}
		
		return res;
	}

}
