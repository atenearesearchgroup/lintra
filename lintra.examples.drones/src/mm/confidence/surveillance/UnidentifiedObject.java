package mm.confidence.surveillance;

import java.util.List;

import uncertaintypes.UReal;
import blackboard.IdentifiableElement;

public class UnidentifiedObject extends MovingObject implements IdentifiableElement{
	
	String id, trgId;
	
	public UnidentifiedObject(String id, UReal width, UReal angle, UReal speed, String positionID, double confidence) {
		this.id = id;
		this.width = width;
		this. angle = angle;
		this.speed = speed;
		this.positionID = positionID;
		this.confidence = confidence;
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

	String gunShotID;
	List<String> shotIDs;
	
	double confidence;

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	
	
	public String getGunShotID() {
		return gunShotID;
	}
	public void setGunShotID(String gunShotID) {
		this.gunShotID = gunShotID;
	}
	public List<String> getShotIDs() {
		return shotIDs;
	}
	public void setShotIDs(List<String> shotIDs) {
		this.shotIDs = shotIDs;
	}
	
	

}
