package uo.ri.cws.application.business.util.command;

import uo.ri.cws.application.business.BusinessException; 
// import uo.ri.cws.application.persistence.PersistenceException;

public interface Command<T> {

	T execute() throws BusinessException; 
}