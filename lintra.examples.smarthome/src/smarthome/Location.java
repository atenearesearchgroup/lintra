
package smarthome;

import blackboard.IdentifiableElement;
import uncertaintypes.UBoolean;
import uncertaintypes.UReal;

public class Location implements IdentifiableElement{
	
	String id, trgId;
	
	
	public Location(String id, UReal x, UReal y) {
		this.id = id;
		this.x = x;
		this.y = y;
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

	UReal x, y;
	String homeID, personID;

	public UReal getX() {
		return x;
	}
	public void setX(UReal x) {
		this.x = x;
	}
	public UReal getY() {
		return y;
	}
	public void setY(UReal y) {
		this.y = y;
	}
	public String getHomeID() {
		return homeID;
	}
	public void setHomeID(String homeID) {
		this.homeID = homeID;
	}
	public String getPersonID() {
		return personID;
	}
	public void setPersonID(String personID) {
		this.personID = personID;
	}
	
} 
