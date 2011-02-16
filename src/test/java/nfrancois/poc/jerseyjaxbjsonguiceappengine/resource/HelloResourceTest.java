package nfrancois.poc.jerseyjaxbjsonguiceappengine.resource;

import static org.fest.assertions.Assertions.assertThat;

import javax.ws.rs.core.MediaType;

import nfrancois.poc.jerseyjaxbjsonguiceappengine.GuiceServletConfig;
import nfrancois.poc.jerseyjaxbjsonguiceappengine.model.Hello;

import org.junit.Test;

import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public class HelloResourceTest extends JerseyTest {
	
    public HelloResourceTest() {
        super(new WebAppDescriptor.Builder()
                .contextListenerClass(GuiceServletConfig.class)
                .filterClass(GuiceFilter.class)
                .servletPath("/")
                .build());
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
		String relativeUrl = "hello";
		String name ="Nicolas";
		ClientResponse clientResponse = resource().path(relativeUrl).path(name).getRequestBuilder().head();
		assertThat(clientResponse.getType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
	}
	
	@Test
	public void shoudBeJsonpWithCallbackNameParam(){
		String relativeUrl = "hello";
		String name ="Nicolas";
		String callbackName = "monCallback";
		WebResource path = resource().path(relativeUrl).path(name+".jsonp").queryParam("callback", callbackName);
		System.out.println(path.getURI().toString());
		ClientResponse clientResponse = path.getRequestBuilder().head();		
		int status = clientResponse.getStatus();
		assertThat(clientResponse.getType().getType()).isEqualTo("application");
		assertThat(clientResponse.getType().getSubtype()).isEqualTo("x-javascript");
		assertThat(status).isEqualTo(Status.OK.getStatusCode());
		String response = path.get(String.class);
		assertThat(response).isNotNull().startsWith(callbackName);	
	}
	
	
	@Test
	public void shoudBeJsonpWithoutCallbackNameParam(){
		String relativeUrl = "hello";
		String name ="Nicolas";
		WebResource path = resource().path(relativeUrl).path(name+".jsonp");
		ClientResponse clientResponse = path.getRequestBuilder().head();		
		int status = clientResponse.getStatus();
		assertThat(clientResponse.getType().getType()).isEqualTo("application");
		assertThat(clientResponse.getType().getSubtype()).isEqualTo("x-javascript");
		assertThat(status).isEqualTo(Status.OK.getStatusCode());
		String response = path.get(String.class);
		assertThat(response).isNotNull().startsWith("jsonpCallback");	
	}	
	
	@Test
	public void shoudBeJsonpWithBlankCallbackNameParam(){
		String relativeUrl = "hello";
		String name ="Nicolas";
		WebResource path = resource().path(relativeUrl).path(name+".jsonp").queryParam("callback", "");
		ClientResponse clientResponse = path.getRequestBuilder().head();		
		int status = clientResponse.getStatus();
		assertThat(clientResponse.getType().getType()).isEqualTo("application");
		assertThat(clientResponse.getType().getSubtype()).isEqualTo("x-javascript");
		assertThat(status).isEqualTo(Status.OK.getStatusCode());
		String response = path.get(String.class);
		assertThat(response).isNotNull().startsWith("jsonpCallback");	
	}		
	
	private void doShoulReplyHello(MediaType type){
		String relativeUrl = "hello";
		String name ="Nicolas";
		WebResource path = resource().path(relativeUrl).path(name);
		assertThat(path.getURI().toString()).isEqualTo(getFullUrl(relativeUrl, name));
		ClientResponse clientResponse = path.getRequestBuilder().accept(type).head();
		int status = clientResponse.getStatus();
		assertThat(clientResponse.getType()).isEqualTo(type);
		assertThat(status).isEqualTo(Status.OK.getStatusCode());
		Hello response = path.get(Hello.class);
		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("Hello");
		assertThat(response.getName()).isEqualTo("Nicolas");		
	}

	private String getFullUrl(String relativeUrl, String name){
		return getBaseURI().toString()+relativeUrl+"/"+name;
	}	
	
}
