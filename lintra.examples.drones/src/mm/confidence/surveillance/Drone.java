
package mm.confidence.surveillance;

import uncertaintypes.UReal;
import blackboard.IdentifiableElement;

public class Drone extends MovingObject implements IdentifiableElement{
	
	String id, trgId;
	
	public Drone(String id, UReal width, UReal angle, UReal speed, String positionID) {
		this.id = id;
		this.width = width;
		this. angle = angle;
		this.speed = speed;
		this.positionID = positionID;
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

	
	

}
