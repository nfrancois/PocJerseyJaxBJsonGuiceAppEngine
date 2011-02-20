package nfrancois.poc.jerseyjaxbjsonguiceappengine.resource;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public abstract class AbstractResourceTest<T> extends JerseyTest {

	protected static Injector injector;	
	
	@Override
	protected AppDescriptor configure() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getClasses().add(JAXBContextResolver.class);
		injector = Guice.createInjector(new ServletModule() {
			
			@Override
			protected void configureServlets() {
				bind(getTestingResourceClass());
				bind(JAXBContextResolver.class);
				serve("/*").with(GuiceContainer.class);
			}

		});	
		return new WebAppDescriptor.Builder()
			        .contextListenerClass(GuiceTestConfig.class)
			        .filterClass(GuiceFilter.class)
			        .clientConfig(clientConfig)
			        .servletPath("/")
			        .build();
	}
	

	protected abstract Class<T> getTestingResourceClass();
	
	private class GuiceTestConfig extends GuiceServletContextListener {
		@Override
		public Injector getInjector() {
			return injector;
		}
	}		
	
}
