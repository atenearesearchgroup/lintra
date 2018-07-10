
package mm.confidence.surveillance;

import blackboard.IdentifiableElement;
import uncertaintypes.UBoolean;
import uncertaintypes.UReal;

public class GunShot extends ProbableElement implements IdentifiableElement{
	
	String id;
	UReal angle;
	UBoolean hitsTarget;
	String positionID, droneID, targetID;
	
	public void updateHitsTarget(Coordinate srcC, Coordinate trgC, UReal trgtWidth){
		UReal d = srcC.distance(trgC);
		UReal a = srcC.x.minus(trgC.x).add(d.mult(angle.cos())).abs();
		UReal b = srcC.y.minus(trgC.y).add(d.mult(angle.sin())).abs();
		this.hitsTarget = a.lt(trgtWidth).and(b.lt(trgtWidth)); 
	}
	
	public void updateAngle(Coordinate cDrone, Coordinate cUO){
		UReal x = cUO.x.minus(cDrone.x);
		UReal y = cUO.y.minus(cDrone.y);
		
		if (x.equals(new UReal(0,0))){
			if (y.gt(new UReal(0,0)).getB()){
				this.angle = new UReal(1.570796326791001, y.getU());
			}else{
				this.angle = new UReal(4.712388980373001246, y.getU());
			}
		} else {
			this.angle = y.divideBy(x).atan();
		}
	}

	String trgId;
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
	public UReal getAngle() {
		return angle;
	}
	public void setAngle(UReal angle) {
		this.angle = angle;
	}
	public UBoolean getHitsTarget() {
		return hitsTarget;
	}
	public void setHitsTarget(UBoolean hitsTarget) {
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
