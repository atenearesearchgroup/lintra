
package mm.plain.surveillance;

import blackboard.IdentifiableElement;

public class Coordinate implements IdentifiableElement{
	
	String id, trgId;
	
	public Coordinate() {
	}
	
	public Coordinate(String id, double x, double y) {
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

	double x;
	double y;
	
	String shotID;
	String objectID;
	
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
	public double distance(Coordinate other){
		
		double a = Math.pow(this.getX()-other.getX(), 2);
		double b = Math.pow(this.getY()-other.getY(), 2);
		return Math.sqrt(a+b);
	}

	@Override
	public String toString() {
		return "Coordinate [id=" + id + ", trgId=" + trgId + ", x=" + x
				+ ", y=" + y + ", shotID=" + shotID + ", objectID=" + objectID
				+ "]";
	}

}
