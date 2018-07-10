
package mm.plain.surveillance;

import blackboard.IdentifiableElement;

public class GunShot extends ProbableElement implements IdentifiableElement{
	
	String id, trgId;
	
	
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

	String positionID;
	String droneID;
	String targetID;
	
	double angle;
	boolean hitsTarget;
	
	public void updateAngle(Coordinate cDrone, Coordinate cUO){
		
		double x = cUO.x-cDrone.x;
		double y = cUO.y-cDrone.y;
		
		if (x==0){
			if (y <= 0){
				this.angle = 1.570796326791001;
			}else{
				this.angle = 4.712388980373001246;
			}
		} else {
			this.angle = Math.atan(y/x);
		}
	}

	public void updateHitsTarget(Coordinate cDrone, Coordinate cUO, double targetWidth){
		double distance = cDrone.distance(cUO);
		double a = cDrone.x-cUO.x + distance*Math.abs(Math.cos(angle));
		double b = cDrone.y-cUO.y + distance*Math.abs(Math.sin(angle));
		this.hitsTarget = a<=targetWidth && b<=targetWidth; 
	}
	
	public String getPositionID() {
		return positionID;
	}
	public void setPositionID(String positionID) {
		this.positionID = positionID;
	}
	public String getDroneID() {
		return droneID;
	}
	public void setDroneID(String droneID) {
		this.droneID = droneID;
	}
	public String getTargetID() {
		return targetID;
	}
	public void setTargetID(String targetID) {
		this.targetID = targetID;
	}
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public boolean getHitsTarget() {
		return hitsTarget;
	}
	public void setHitsTarget(boolean hitsTarget) {
		this.hitsTarget = hitsTarget;
	}
	@Override
	public String toString() {
		return "GunShot [id=" + id + ", trgId=" + trgId + ", positionID="
				+ positionID + ", droneID=" + droneID + ", targetID="
				+ targetID + ", angle=" + angle + ", hitsTarget=" + hitsTarget
				+ "]";
	}
	
} 
