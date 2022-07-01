package uo.ri.cws.application.service.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.business.BusinessException;
import uo.ri.cws.application.business.BusinessFactory;
import uo.ri.cws.application.business.client.ClientCrudService;
import uo.ri.cws.application.business.client.ClientDto;
import uo.ri.cws.application.business.paymentmean.CashDto;
import uo.ri.cws.application.persistence.paymentmean.PaymentmeanRecord;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.util.ClientUtil;
import uo.ri.cws.application.service.util.PaymentMeansUtil;
import uo.ri.cws.application.service.util.RecommendationUtil;
import uo.ri.cws.application.service.util.VehicleUtil;
import uo.ri.cws.application.service.util.sql.FindPaymentmeanByClientIdSqlUnitOfWork;

public class ClientSteps {

	private TestContext ctx;

	private ClientCrudService service = BusinessFactory.forClientCrudService();
	private ClientDto client;
	private List<ClientDto> clients;
	private ClientDto sponsor;
	private Optional<ClientDto> maybe;

	private ClientDto updatedClient;

	// private MechanicDto mechanic, found;
//	private List<MechanicDto> mechanics;

	public ClientSteps(TestContext ctx) {
		this.ctx = ctx;
	}

	@Given("a client")
	public void aClient() {
		client = new ClientUtil().get();
	}

	@Given("a client registered")
	public void aClientRegistered() {
		client = new ClientUtil().register().get();
		this.ctx.put(TestContext.Key.ACLIENT, client);

	}

	@Given("an sponsor registered")
	public void anSponsorRegistered() {
		sponsor = new ClientUtil().register().get();
		this.ctx.put(TestContext.Key.SPONSOR, sponsor);
	}

	@When("I look for clients recommended by sponsor id")
	public void iLookForClientsRecommendedBySponsorId() throws BusinessException {
		clients = service.findClientsRecommendedBy(sponsor.id);
	}

	@Then("clients not found")
	public void clientsNotFound() {
		assertTrue(clients.isEmpty());
	}

	@When("I add client")
	public void iAddClient() throws BusinessException {
		service.addClient(client, null);
	}

	@Given("the second has the same dni as the first")
	public void theSecondHasTheSameDniAsTheFirst() {
		ClientDto firstClient = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
		client.dni = firstClient.dni;
	}

	@When("I try to add the client")
	public void iTryToAddANewClient() {
		tryAddAndKeepException(client);
	}

	@When("I try to add a new client with null argument")
	public void iTryToAddANewClientWithNullArgument() {
		client = new ClientUtil().get();
		client.dni = null;
		tryAddAndKeepException(client);

	}

	@When("I try to add a new client with null dni")
	public void iTryToAddANewClientWithNullDni() {

		tryAddAndKeepException(client);

	}

	@When("I try to add a new client with {string}")
	public void iTryToAddANewClientWith(String dni) {
		client = new ClientUtil().get();
		client.dni = dni;
		tryAddAndKeepException(client);
	}

	@When("I remove the client")
	public void iRemoveTheClient() throws BusinessException {
		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
		service.deleteClient(client.id);
	}

	@When("I try to remove a non existent client")
	public void iTryToRemoveANonExistentClient() {
		tryDeleteAndKeepException(UUID.randomUUID().toString());
	}

	@Given("vehicles under client identity")
	public void vehiclesUnderClientIdentity() {
		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);

		new VehicleUtil().withOwner(client.id).register();

	}

	@When("I try to remove the client")
	public void iTryToRemoveTheClient() {
		tryDeleteAndKeepException(client.id);

	}

	@Then("the client no longer exists")
	public void theClientNoLongerExists() throws BusinessException {
		assertTrue(service.findClientById(client.id).isEmpty());
	}

	@Then("there are no payment mean for this client")
	public void thereAreNoPaymentMeanForThisClient() {
		FindPaymentmeanByClientIdSqlUnitOfWork work = new FindPaymentmeanByClientIdSqlUnitOfWork(client.id);
		work.execute();
		List<PaymentmeanRecord> pays = work.get();
		assertTrue(pays.isEmpty());
	}

	@Then("the client results added to the system")
	public void theClientResultsAddedToTheSystem() throws BusinessException {
		Optional<ClientDto> maybe = service.findClientById(client.id);

		assertTrue(maybe.isPresent());
		ClientDto found = maybe.get();
		assertTrue(match(client, found));

	}

	@Then("cash is created for the client")
	public void cashIsCreatedForTheClient() {
		Optional<CashDto> maybe = new PaymentMeansUtil().findCashForClient(client.id);
		assertTrue(maybe.isPresent());
		assertEquals(maybe.get().clientId, client.id);
		assertEquals(maybe.get().accumulated, 0.0, 0.0001);
	}

	@When("I try to remove a client with null argument")
	public void iTryToRemoveAClientWithNullArgument() {
		tryDeleteAndKeepException(null);

	}

	@When("I try to delete a client with {string}")
	public void iTryToDeleteAClientWith(String id) {
		tryDeleteAndKeepException(id);

	}

	@When("I update the client")
	public void iUpdateTheClient() throws BusinessException {
		updatedClient = this.client;

		updatedClient.name += "-updated";
		updatedClient.surname += "-updated";
		updatedClient.email += "-updated";
		updatedClient.phone += "-updated";

		service.updateClient(updatedClient);

	}

	@Then("the client results updated")
	public void theClientResultsUpdated() throws BusinessException {
		Optional<ClientDto> found = service.findClientById(client.id);
		assertTrue(found.isPresent());
		assertTrue(match(found.get(), updatedClient));
	}

	@When("I try to update a non existing client")
	public void iTryToUpdateANonExistingClient() {
		ClientDto aClient = new ClientUtil().get();
		aClient.id = UUID.randomUUID().toString();
		tryUpdateAndKeepException(aClient);
	}

	@When("I try to update a client with null id")
	public void iTryToUpdateAClientWithNullId() {

		ClientDto aClient = new ClientUtil().get();
		aClient.id = null;
		tryUpdateAndKeepException(aClient);
	}

	@When("I try to update the client with wrong {string}")
	public void iTryToUpdateTheClientWithWrong(String string) {

		ClientDto aClient = new ClientUtil().get();
		aClient.id = string;
		tryUpdateAndKeepException(aClient);
	}

	@When("I try to update the client with wrong args")
	public void iTryToUpdateTheClientWithWrongArgs() {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}

//
//	@Given("the following relation of clients with {string}, {string}, {string}")
//	public void theFollowingRelationOfClientsWith(String dni, String name, String surname) throws BusinessException {
//	   ClientDto client = new ClientDto();
//	   client.dni = dni;
//	   client.name = name;
//	   client.surname = surname;
//	   client.id = service.addClient(client).id;
//	}
//
//
//
//	@Then("we get the following {string}")
//	public void weGetTheFollowing(String string) {
//	    // Write code here that turns the phrase above into concrete actions
//	    throw new io.cucumber.java.PendingException();
//	}

	@Given("the following relation of clients with data")
	public void theFollowingRelationOfClientsWithDniNameSurname(DataTable dataTable) {
		List<Map<String, String>> table = dataTable.asMaps();
		for (Map<String, String> row : table) {
			new ClientUtil().withName(row.get("name")).withDni(row.get("dni")).withSurname(row.get("surname"))
					.register();
		}

	}

	@Given("a recommendation registered")
	public void aRecommendationRegistered() {
		new RecommendationUtil().withSponsor(this.sponsor.id).withClient(this.client.id).register();
	}

	@Then("we get the following {string}")
	public void weGetTheFollowing(String string) {
		assertTrue(canBeFound(string));
	}

	private boolean canBeFound(String clientString) {
		String[] splitted = clientString.split(",");
		String dni = splitted[0];
		String name = splitted[1];
		String surname = splitted[2];
		for (ClientDto client : clients)
			if (client.dni.equals(dni) && client.name.equals(name) && client.surname.equals(surname))
				return true;
		return false;
	}

	@Then("we get the following <client>")
	public void weGetTheFollowingClient(DataTable dataTable) {
//   for (ClientDto client : allClients)
//	   assertTrue ( isIn)
//    throw new io.cucumber.java.PendingException();
	}

	@When("we read all clients")
	public void weReadAllClients() throws BusinessException {
		clients = service.findAllClients();
	}

	@Then("we get an empty list of clients")
	public void weGetAnEmptyListOfClients() {
		assertTrue(this.clients.isEmpty());
	}

	@When("I look for client")
	public void iLookForClient() throws BusinessException {
		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
		maybe = service.findClientById(client.id);
	}

	@When("I find clients recommended by a non existent sponsor id")
	public void iFindClientsRecommendedByANonExistentSponsorId() throws BusinessException {
		clients = service.findClientsRecommendedBy(UUID.randomUUID().toString());
	}

	@When("I try to find clients recommended by null sponsor id")
	public void iTryToFindClientsRecommendedByNullSponsorId() {
		tryFindAndKeepException(null);
	}

	@When("I look for a client recommended by wrong sponsor id {string}")
	public void iLookForAClientRecommendedByWrongSponsorId(String string) {
		tryFindAndKeepException(string);

	}

	@When("I try to find a non existent client")
	public void iTryToFindANonExistentClient() throws BusinessException {
		maybe = service.findClientById(UUID.randomUUID().toString());

	}

	@When("I try to find a client with null argument")
	public void iTryToFindAClientWithNullArgument() {
		tryFindAndKeepException(null);
	}

	@When("I look for a client by wrong id {string}")
	public void iLookForAClientByWrongId(String id) throws BusinessException {
		tryFindAndKeepException(id);

//    found = service.findClientById(id);
	}

	@Then("client not found")
	public void clientNotFound() {
		assertTrue(maybe.isEmpty());
	}

	@Then("I get client")
	public void iGetClient() {

		assertTrue(this.maybe.isPresent());

		ClientDto foundDto = this.maybe.get();
		assertTrue(match(foundDto, client));
	}
	
	@Then("client is found")
	public void clientIsFound() {
		assertTrue(!this.clients.isEmpty());
		assertTrue(resultContains(this.clients, this.client));
//		
//		ClientDto foundDto = this.clients.get(0);
//		assertTrue(match(foundDto, client));
	}

	private boolean resultContains(List<ClientDto> list, ClientDto dto) {
		for (ClientDto item : list ) {
			if (match(item, dto))
				return true;
		}
		return false;
	}

	private boolean match(ClientDto one, ClientDto other) {
//		return (one.id.equals(other.id)
//					&& one.dni.equals(other.dni)
//					&& one.name.equals(other.name)
//					&& one.surname.equals(other.surname)
//					&& one.phone.equals(other.phone)
//					&& one.email.equals(other.email)
//					&& one.addressCity.equals(other.addressCity)
//					&& one.addressStreet.equals(other.addressStreet)
//					&& one.addressZipcode.equals(other.addressZipcode) );

		return (one.id.equals(other.id) && one.dni.equals(other.dni) && compare(one.name, other.name)
				&& compare(one.surname, other.surname) && compare(one.phone, other.phone)
				&& compare(one.email, other.email) && compare(one.addressCity, other.addressCity)
				&& compare(one.addressStreet, other.addressStreet)
				&& compare(one.addressZipcode, other.addressZipcode));
	}

	private static boolean compare(String str1, String str2) {
		return (str1 == null ? str2 == null : str1.equals(str2));
	}

	private void tryAddAndKeepException(ClientDto dto) {
		try {
			service.addClient(dto, null);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}

	}

	private void tryDeleteAndKeepException(String id) {
		try {
			service.deleteClient(id);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}

	}

	private void tryFindAndKeepException(String id) {
		try {
			service.findClientById(id);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}

	}

	private void tryUpdateAndKeepException(ClientDto rec) {
		try {
			service.updateClient(rec);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}
	}
//	@When("I add client")
//	public void iAddClient() {
//		ClientDto client = (ClientDto)ctx.get(TestContext.Key.ACLIENT);
//		
//	}
//
//
//	Some other steps were also undefined:
//
//	@Then("the client results added to the system")
//	public void theClientResultsAddedToTheSystem() {
//	    // Write code here that turns the phrase above into concrete actions
//	    throw new io.cucumber.java.PendingException();
//	}

}
