package nfrancois.poc.jerseyjaxbjsonguiceappengine.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import nfrancois.poc.jerseyjaxbjsonguiceappengine.model.Hello;
import nfrancois.poc.jerseyjaxbjsonguiceappengine.service.HelloService;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.json.JSONWithPadding;

@Singleton
@Path("hello")
public class HelloResource {
	
	private static final String CALLBACK_DEFAULT_NAME = "jsonpCallback";
	
	@Inject
	private HelloService helloService;
	
	@GET
	@Path("{name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})	
	public Hello reply(@PathParam("name") String name){
		return helloService.saysHelloToSomeone(name);
	}
	
	@GET
	@Path("{name}.jsonp")
    @Produces("application/x-javascript")
	public JSONWithPadding replyWithJsonP(@PathParam("name") String name, @QueryParam("callback") String callback){
		if (callback == null || "".equals(callback)) {
			callback = CALLBACK_DEFAULT_NAME;
		}
		return new JSONWithPadding(helloService.saysHelloToSomeone(name), callback);
	}	
	
	
	public void setHelloService(HelloService helloService) {
		this.helloService = helloService;
	}
	
}
