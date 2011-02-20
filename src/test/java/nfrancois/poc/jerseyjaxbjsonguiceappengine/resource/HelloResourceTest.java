package nfrancois.poc.jerseyjaxbjsonguiceappengine.resource;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.MediaType;

import nfrancois.poc.jerseyjaxbjsonguiceappengine.model.Hello;
import nfrancois.poc.jerseyjaxbjsonguiceappengine.service.HelloService;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;

public class HelloResourceTest extends AbstractResourceTest<HelloResource> {

	private HelloService helloServiceMock;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		HelloResource helloResource =  injector.getInstance(HelloResource.class);
		helloServiceMock = mock(HelloService.class);
		helloResource.setHelloService(helloServiceMock);
	}	
	
	@Test
	public void shoulReplyHelloInXml(){
		doShoulReplyHello(MediaType.APPLICATION_XML_TYPE);
	}
	
	@Test
	public void shoulReplyHelloInJson(){
		doShoulReplyHello(MediaType.APPLICATION_JSON_TYPE);
	}	
	
	@Test
	public void shoudBeJsonHelloByDefault(){
		String message = "Hello";
		String name ="Nicolas";
		when(helloServiceMock.saysHelloToSomeone("Nicolas")).thenReturn(new Hello(message, name));	
		ClientResponse clientResponse = resource().path("hello").path(name).get(ClientResponse.class);
		assertThat(clientResponse.getType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
	}
	
	@Test
	public void shoudBeJsonpWithCallbackNameParam(){
		String message = "Hello";
		String name ="Nicolas";
		when(helloServiceMock.saysHelloToSomeone("Nicolas")).thenReturn(new Hello(message, name));		
		String callbackName = "monCallback";
		ClientResponse response = resource().path("hello").path(name+".jsonp").queryParam("callback", callbackName).get(ClientResponse.class);
		assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		assertThat(response.getType().toString()).isEqualTo("application/x-javascript");
		assertThat(response.getEntity(String.class)).isNotNull().startsWith(callbackName);	
	}
	
	
	@Test
	public void shoudBeJsonpWithoutCallbackNameParam(){
		String message = "Hello";
		String name ="Nicolas";
		when(helloServiceMock.saysHelloToSomeone("Nicolas")).thenReturn(new Hello(message, name));	
		ClientResponse response = resource().path("hello").path(name+".jsonp").get(ClientResponse.class);
		assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		assertThat(response.getType().toString()).isEqualTo("application/x-javascript");
		assertThat(response.getEntity(String.class)).isNotNull().startsWith("jsonpCallback");	
	}	
	
	private void doShoulReplyHello(MediaType type){
		String message = "Hello";
		String name ="Nicolas";
		when(helloServiceMock.saysHelloToSomeone("Nicolas")).thenReturn(new Hello(message, name));		
		ClientResponse response = resource().path("hello").path(name).accept(type).get(ClientResponse.class);
		assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		assertThat(response.getType()).isEqualTo(type);
		Hello hello = response.getEntity(Hello.class);
		assertThat(response).isNotNull();
		assertThat(hello.getMessage()).isEqualTo(message);
		assertThat(hello.getName()).isEqualTo(name);		
	}

	@Override
	protected Class<HelloResource> getTestingResourceClass() {
		return HelloResource.class;
	}
}
