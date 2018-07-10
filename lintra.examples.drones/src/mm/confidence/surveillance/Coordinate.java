
package mm.confidence.surveillance;

import blackboard.IdentifiableElement;
import uncertaintypes.*;

public class Coordinate implements IdentifiableElement{
	
	String id, trgId;
	
	public Coordinate() {
	}
	
	public Coordinate(String id, UReal x, UReal y) {
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

	UReal x;
	UReal y;
	
	String shotID;
	String objectID;
	
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
	public String getShotID() {
		return shotID;
	}
	public void setShotID(String shotID) {
		this.shotID = shotID;
	}
	public String getObjectID() {
		return objectID;
	}
	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}
	public UReal distance(Coordinate other){
		
		UReal a = this.getX().minus(other.getX()).power(2);
		UReal b = this.getY().minus(other.getY()).power(2);
		return (a.add(b).sqrt());
	}

	@Override
	public String toString() {
		return "Coordinate [id=" + id + ", trgId=" + trgId + ", x=" + x
				+ ", y=" + y + ", shotID=" + shotID + ", objectID=" + objectID
				+ "]";
	}

}
