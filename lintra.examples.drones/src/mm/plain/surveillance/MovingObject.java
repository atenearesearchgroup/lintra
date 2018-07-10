
package mm.plain.surveillance;

import blackboard.IdentifiableElement;

public abstract class MovingObject implements IdentifiableElement {
	
	double width;
	double angle;
	double speed;
	String positionID;
	
	
	
	public double getWidth() {
		return width;
	}



	public void setWidth(double width) {
		this.width = width;
	}



	public double getAngle() {
		return angle;
	}



	public void setAngle(double angle) {
		this.angle = angle;
	}



	public double getSpeed() {
		return speed;
	}



	public void setSpeed(double speed) {
		this.speed = speed;
	}



	public String getPositionID() {
		return positionID;
	}



	public void setPositionID(String positionID) {
		this.positionID = positionID;
	}

}
