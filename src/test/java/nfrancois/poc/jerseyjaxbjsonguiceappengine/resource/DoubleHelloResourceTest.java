package nfrancois.poc.jerseyjaxbjsonguiceappengine.resource;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import javax.ws.rs.core.MediaType;

import nfrancois.poc.jerseyjaxbjsonguiceappengine.model.Hello;

import org.junit.Test;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.GenericType;

public class DoubleHelloResourceTest extends AbstractResourceTest<DoubleHelloResource> {
	
	@Test
	public void shoudHaveTwoHello(){
		String relativeUrl = "doublehello";
		String name ="Nicolas";
		ClientResponse response = resource().path(relativeUrl).path(name).get(ClientResponse.class);
		assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		assertThat(response.getType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
		List<Hello> hellos = response.getEntity(new GenericType<List<Hello>>(){});
		assertThat(hellos).isNotNull().hasSize(2);
	}
	
	@Test
	public void shoudBeInNaturalJson(){
		String relativeUrl = "doublehello";
		String name ="Nicolas";
		ClientResponse response = resource().path(relativeUrl).path(name).get(ClientResponse.class);
		assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		assertThat(response.getType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
		String hellos = response.getEntity(String.class);
		assertThat(hellos).isEqualTo("[{\"message\":\"Hello\",\"name\":\"Nicolas\"},{\"message\":\"Hello\",\"name\":\"Nicolas\"}]");
	}
	
	public class GuiceTestConfig extends GuiceServletContextListener {
		@Override
		public Injector getInjector() {
			return injector;
		}
	}

	@Override
	protected Class<DoubleHelloResource> getTestingResourceClass() {
		return DoubleHelloResource.class;
	}		
}
