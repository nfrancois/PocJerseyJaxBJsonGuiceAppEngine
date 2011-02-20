package nfrancois.poc.jerseyjaxbjsonguiceappengine.resource;

import static org.fest.assertions.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import nfrancois.poc.jerseyjaxbjsonguiceappengine.GuiceServletConfig;
import nfrancois.poc.jerseyjaxbjsonguiceappengine.model.Hello;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public class DoubleHelloResourceTest extends JerseyTest {
	
//	private static Injector injector =  Guice.createInjector(new ServletModule() {
//		
//		@Override
//		protected void configureServlets() {
//			final Map<String, String> params = new HashMap<String, String>();
//			bind(DoubleHelloResource.class);
//			serve("/*").with(GuiceContainer.class, params);
//		}
//	});	
	
    public DoubleHelloResourceTest() {
        super(new WebAppDescriptor.Builder(/*JAXBContextResolver.class.getPackage().getName()*/)
                .contextListenerClass(GuiceServletConfig.class)
                .filterClass(GuiceFilter.class)
                .clientConfig(createClientConfigTest())
                .servletPath("/")
                .build());
    }
	
	
	private static ClientConfig createClientConfigTest() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getClasses().add(JAXBContextResolver.class);
		return clientConfig;
	}


	@Test
	public void shoudHaveTwoHelloInNormalJson(){
		String relativeUrl = "doublehello";
		String name ="Nicolas";
		WebResource path = resource().path(relativeUrl).path(name);
		assertThat(path.getURI().toString()).isEqualTo(getFullUrl(relativeUrl, name));
		ClientResponse clientResponse = path.getRequestBuilder().head();
		int status = clientResponse.getStatus();
		assertThat(clientResponse.getType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
		assertThat(status).isEqualTo(Status.OK.getStatusCode());
		List<Hello> responseAsList = path.get(new GenericType<List<Hello>>(){});
		assertThat(responseAsList).isNotNull().hasSize(2);
		String reponseAsString = path.get(String.class);
		assertThat(reponseAsString).isEqualTo("[{\"message\":\"Hello\",\"name\":\"Nicolas\"},{\"message\":\"Hello\",\"name\":\"Nicolas\"}]");
	}
	
	private String getFullUrl(String relativeUrl, String name){
		return getBaseURI().toString()+relativeUrl+"/"+name;
	}	
	
//	
//	public class GuiceTestConfig extends GuiceServletContextListener {
//		@Override
//		public Injector getInjector() {
//			return injector;
//		}
//	}		
}
