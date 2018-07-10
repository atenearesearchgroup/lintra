
package mm.plain.smarthome;

import blackboard.IdentifiableElement;

public class Location implements IdentifiableElement{
	
	String id, trgId;
	
	
	public Location(String id, double x, double y) {
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

	double x, y;
	String homeID, personID;

	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
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
