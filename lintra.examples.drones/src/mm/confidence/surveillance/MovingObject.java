
package mm.confidence.surveillance;

import blackboard.IdentifiableElement;
import uncertaintypes.UReal;

public abstract class MovingObject implements IdentifiableElement {
	
	UReal width;
	UReal angle;
	UReal speed;
	String positionID;
	
	
	
	public UReal getWidth() {
		return width;
	}



	public void setWidth(UReal width) {
		this.width = width;
	}



	public UReal getAngle() {
		return angle;
	}



	public void setAngle(UReal angle) {
		this.angle = angle;
	}



	public UReal getSpeed() {
		return speed;
	}



	public void setSpeed(UReal speed) {
		this.speed = speed;
	}



	public String getPositionID() {
		return positionID;
	}



	public void setPositionID(String positionID) {
		this.positionID = positionID;
	}

}
