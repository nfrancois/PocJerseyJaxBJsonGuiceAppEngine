package nfrancois.poc.jerseyjaxbjsonguiceappengine.resource;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.google.inject.Singleton;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Provider
@Singleton
public class JAXBContextResolver implements ContextResolver<JAXBContext> {

	/** Package that contains object that can be mapped */
	private static final String JAXB_OBJECT_PACKAGE = "nfrancois.poc.jerseyjaxbjsonguiceappengine.model";

	private final JAXBContext context;

	public JAXBContextResolver() throws Exception {
		this.context = new JSONJAXBContext(JSONConfiguration.natural().build(), JAXB_OBJECT_PACKAGE);
	}

	@Override
	public JAXBContext getContext(Class<?> objectType) {
		if(objectType.getPackage().getName().equals(JAXB_OBJECT_PACKAGE)){
			return context;
         }
		return null;
	}
}
