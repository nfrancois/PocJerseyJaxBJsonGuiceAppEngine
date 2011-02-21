package nfrancois.poc.jerseyjaxbjsonguiceappengine.service;

import javax.inject.Singleton;

import nfrancois.poc.jerseyjaxbjsonguiceappengine.model.Hello;


@Singleton
public class HelloService {
	
	public Hello saysHelloToSomeone(String who){
		return new Hello("Hello",who);
	}

}
