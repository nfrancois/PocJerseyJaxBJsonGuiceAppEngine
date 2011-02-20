package nfrancois.poc.jerseyjaxbjsonguiceappengine.resource;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.ws.rs.core.MediaType;

import nfrancois.poc.jerseyjaxbjsonguiceappengine.model.Hello;
import nfrancois.poc.jerseyjaxbjsonguiceappengine.service.HelloService;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.GenericType;

public class DoubleHelloResourceTest extends AbstractResourceTest<DoubleHelloResource> {

	private HelloService helloServiceMock;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		HelloResource helloResource =  injector.getInstance(HelloResource.class);
		helloServiceMock = mock(HelloService.class);
		helloResource.setHelloService(helloServiceMock);
	}		
	
	@Test
	public void shoudHaveTwoHello(){
		String message = "Hello";
		String name ="Nicolas";
		when(helloServiceMock.saysHelloToSomeone("Nicolas")).thenReturn(new Hello(message, name));	
		ClientResponse response = resource().path("doublehello").path(name).get(ClientResponse.class);
		assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		assertThat(response.getType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
		List<Hello> hellos = response.getEntity(new GenericType<List<Hello>>(){});
		assertThat(hellos).isNotNull().hasSize(2);
	}
	
	@Test
	public void shoudBeInNaturalJson(){
		String message = "Hello";
		String name ="Nicolas";
		when(helloServiceMock.saysHelloToSomeone("Nicolas")).thenReturn(new Hello(message, name));	
		ClientResponse response = resource().path("doublehello").path(name).get(ClientResponse.class);
		assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		assertThat(response.getType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
		String hellos = response.getEntity(String.class);
		assertThat(hellos).isEqualTo(naturalHelloJSon(message, name));
	}
	
	public String naturalHelloJSon(String message, String name){
		StringBuilder sb = new StringBuilder();
		sb.append("[{\"message\":\"").append(message).append("\",\"name\":\"").append(name).append("\"},");
		sb.append("{\"message\":\"").append(message).append("\",\"name\":\"").append(name).append("\"}]");
		return sb.toString();
	}
	
	@Override
	protected Class<DoubleHelloResource> getTestingResourceClass() {
		return DoubleHelloResource.class;
	}		
}
