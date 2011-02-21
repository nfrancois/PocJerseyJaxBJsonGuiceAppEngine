package nfrancois.poc.jerseyjaxbjsonguiceappengine.resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nfrancois.poc.jerseyjaxbjsonguiceappengine.model.Hello;
import nfrancois.poc.jerseyjaxbjsonguiceappengine.service.HelloService;

@Path("doublehello")
@Singleton
@Produces({MediaType.APPLICATION_JSON/*, MediaType.APPLICATION_XML*/})
public class DoubleHelloResource {
	
	@Inject
	private HelloService helloService;
	
	@GET
	@Path("/{name}")
	public List<Hello> reply(@PathParam("name") String name){
		List<Hello> hellos = new ArrayList<Hello>();
		hellos.add(helloService.saysHelloToSomeone(name));
		hellos.add(helloService.saysHelloToSomeone(name));
		return hellos;
	}
	
}
