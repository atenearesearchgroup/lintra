
package smarthome;

import blackboard.IdentifiableElement;
import uncertaintypes.UBoolean;
import uncertaintypes.UReal;

public class Person extends ProbableTimedElement implements IdentifiableElement{
	
	String id, trgId;
	
	public Person(String id, String pIdentifier, UReal ts) {
		this.id = id;
		this.pIdentifier = pIdentifier;
		this.ts = ts;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTrgId() {
		return trgId;
	}
	public void setTrgId(String trgId) {
		this.trgId = trgId;
	}

	String pIdentifier;	
	String locationID;


	public String getpIdentifier() {
		return pIdentifier;
	}
	public void setpIdentifier(String pIdentifier) {
		this.pIdentifier = pIdentifier;
	}
	public String getLocationID() {
		return locationID;
	}
	public void setLocationID(String locationID) {
		this.locationID = locationID;
	}
	
} 
