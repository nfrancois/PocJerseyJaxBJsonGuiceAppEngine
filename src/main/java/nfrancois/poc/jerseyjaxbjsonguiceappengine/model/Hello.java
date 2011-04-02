package nfrancois.poc.jerseyjaxbjsonguiceappengine.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Hello {
	
	private String message;
	private String name;
	
	public Hello(){
	}
	
	public Hello(String message, String name) {
		super();
		this.message = message;
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public int hashCode() {
		return Objects.hashCode(message, name);
	}
	@Override
	public boolean equals(Object obj) {
	    if(obj instanceof Hello){
	        final Hello other = (Hello) obj;
	        return Objects.equal(message, other.message)
	            && Objects.equal(name, other.name);
	    } else{
	        return false;
	    }
	}

}
