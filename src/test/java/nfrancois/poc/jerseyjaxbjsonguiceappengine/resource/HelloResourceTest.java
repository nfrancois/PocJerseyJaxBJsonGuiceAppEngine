package nfrancois.poc.jerseyjaxbjsonguiceappengine.resource;

import static org.fest.assertions.Assertions.assertThat;

import javax.ws.rs.core.MediaType;

import nfrancois.poc.jerseyjaxbjsonguiceappengine.model.Hello;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;

public class HelloResourceTest extends AbstractResourceTest<HelloResource> {
	
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
		String relativeUrl = "hello";
		String name ="Nicolas";
		ClientResponse clientResponse = resource().path(relativeUrl).path(name).get(ClientResponse.class);
		assertThat(clientResponse.getType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
	}
	
	@Test
	public void shoudBeJsonpWithCallbackNameParam(){
		String relativeUrl = "hello";
		String name ="Nicolas";
		String callbackName = "monCallback";
		ClientResponse response = resource().path(relativeUrl).path(name+".jsonp").queryParam("callback", callbackName).get(ClientResponse.class);
		assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		assertThat(response.getType().toString()).isEqualTo("application/x-javascript");
		assertThat(response.getEntity(String.class)).isNotNull().startsWith(callbackName);	
	}
	
	
	@Test
	public void shoudBeJsonpWithoutCallbackNameParam(){
		String relativeUrl = "hello";
		String name ="Nicolas";
		ClientResponse response = resource().path(relativeUrl).path(name+".jsonp").get(ClientResponse.class);
		assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		assertThat(response.getType().toString()).isEqualTo("application/x-javascript");
		assertThat(response.getEntity(String.class)).isNotNull().startsWith("jsonpCallback");	
	}	
	
	@Test
	public void shoudBeJsonpWithBlankCallbackNameParam(){
		String relativeUrl = "hello";
		String name ="Nicolas";
		ClientResponse response = resource().path(relativeUrl).path(name+".jsonp").queryParam("callback", "").get(ClientResponse.class);
		assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		assertThat(response.getType().toString()).isEqualTo("application/x-javascript");
		assertThat(response.getEntity(String.class)).isNotNull().startsWith("jsonpCallback");
	}		
	
	private void doShoulReplyHello(MediaType type){
		String relativeUrl = "hello";
		String name ="Nicolas";
		ClientResponse response = resource().path(relativeUrl).path(name).accept(type).get(ClientResponse.class);
		assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		assertThat(response.getType()).isEqualTo(type);
		Hello hello = response.getEntity(Hello.class);
		assertThat(response).isNotNull();
		assertThat(hello.getMessage()).isEqualTo("Hello");
		assertThat(hello.getName()).isEqualTo("Nicolas");		
	}

	@Override
	protected Class<HelloResource> getTestingResourceClass() {
		return HelloResource.class;
	}
}
