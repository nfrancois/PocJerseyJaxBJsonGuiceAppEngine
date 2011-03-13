package nfrancois.poc.jerseyjaxbjsonguiceappengine.resource;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.ws.rs.core.MediaType;

import nfrancois.poc.jerseyjaxbjsonguiceappengine.model.Hello;
import nfrancois.poc.jerseyjaxbjsonguiceappengine.service.HelloService;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.json.JSONWithPadding;

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
		Hello hello = new Hello(message, name);		
		when(helloServiceMock.saysHelloToSomeone("Nicolas")).thenReturn(hello);
		
		ClientResponse clientResponse = resource().path("hello").path(name).get(ClientResponse.class);
		
		verify(helloServiceMock).saysHelloToSomeone(name);
		assertThat(clientResponse.getType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
	}
	
	@Test
	public void shouldSendHello(){
		String message = "Hello";
		String name ="Nicolas";
		Hello hello = new Hello(message, name);
		when(helloServiceMock.sendHello(name)).thenReturn(hello);

		ClientResponse response = resource().path("hello").post(ClientResponse.class,name);

		verify(helloServiceMock).sendHello(name);
		assertThat(response.getClientResponseStatus()).isEqualTo(Status.CREATED);
		assertThat(response.getType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
		Hello entity = response.getEntity(Hello.class);
		assertThat(entity).isNotNull().isEqualTo(hello);
	}		
	
	@Test
	public void shoudBeJsonpWithCallbackNameParam(){
		String message = "Hello";
		String name ="Nicolas";
		Hello hello = new Hello(message, name);	
		when(helloServiceMock.saysHelloToSomeone("Nicolas")).thenReturn(new Hello(message, name));
		String callbackName = "monCallback";
		
		ClientResponse response = resource().path("hello").path(name+".jsonp").queryParam("callback", callbackName).get(ClientResponse.class);
		
		assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		assertThat(response.getType().toString()).isEqualTo("application/x-javascript");
		assertThat(response.getEntity(String.class)).isNotNull().startsWith(callbackName);
//		Hello entity = response.getEntity(Hello.class);		
//		assertThat(entity).isNotNull().isEqualTo(hello);		
	}
	
	
	@Test
	public void shoudBeJsonpWithoutCallbackNameParam(){
		String message = "Hello";
		String name ="Nicolas";
		Hello hello = new Hello(message, name);		
		when(helloServiceMock.saysHelloToSomeone("Nicolas")).thenReturn(hello);	
		
		ClientResponse response = resource().path("hello").path(name+".jsonp").get(ClientResponse.class);
		
		assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		assertThat(response.getType().toString()).isEqualTo("application/x-javascript");
//		assertThat(response.getEntity(String.class)).isNotNull().startsWith("jsonpCallback");
//		Hello entity = response.getEntity(new GenericType<Hello>(){});
//		Hello entity = response.getEntity(Hello.class);
//		assertThat(entity.getCallbackName()).isEqualTo("jsonpCallback");
//		assertThat(entity.getJsonSource()).isNotNull().isEqualTo(hello);	
	}	
	
	private void doShoulReplyHello(MediaType type){
		String message = "Hello";
		String name ="Nicolas";
		Hello hello = new Hello(message, name);
		when(helloServiceMock.saysHelloToSomeone("Nicolas")).thenReturn(hello);
		
		ClientResponse response = resource().path("hello").path(name).accept(type).get(ClientResponse.class);
		
		verify(helloServiceMock).saysHelloToSomeone(name);
		assertThat(response.getClientResponseStatus()).isEqualTo(Status.OK);
		assertThat(response.getType()).isEqualTo(type);
		Hello entity = response.getEntity(Hello.class);
		assertThat(entity).isNotNull().isEqualTo(hello);		
		
	}

	@Override
	protected Class<HelloResource> getTestingResourceClass() {
		return HelloResource.class;
	}
}
