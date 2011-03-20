package nfrancois.poc.jerseyjaxbjsonguiceappengine.service;

import nfrancois.poc.jerseyjaxbjsonguiceappengine.model.Hello;

import com.google.inject.Singleton;


@Singleton
public class HelloService {
	
	public Hello saysHelloToSomeone(String who){
		return new Hello("Hello",who);
	}
	
	public Hello sendHello(String name) {
		return new Hello("Hello",name);
	}	

}
