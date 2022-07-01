package uo.ri.cws.application.business.client.crud;

import java.util.List; 
import java.util.Optional;

import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.client.ClientCrudService;
import uo.ri.cws.application.business.client.ClientDto;

public class ClientCrudServiceImpl implements ClientCrudService {
	
	@Override
	public ClientDto addClient(ClientDto client, String recommenderId) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteClient(String idClient) throws BusinessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateClient(ClientDto client) throws BusinessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ClientDto> findAllClients() throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<ClientDto> findClientById(String idClient) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClientDto> findClientsRecommendedBy(String sponsorID) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

}
