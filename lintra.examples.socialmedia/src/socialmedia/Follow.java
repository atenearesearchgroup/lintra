
package socialmedia;

import java.util.List;

import blackboard.IdentifiableElement;
import uncertaintypes.UBoolean;
import uncertaintypes.UReal;

public class Follow extends ProbableElement implements IdentifiableElement{
	
	String id, trgId;
	
	public Follow(double confidence) {
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

	String followedID, followerID;	

	public String getFollowedID() {
		return followedID;
	}
	public void setFollowedID(String followedID) {
		this.followedID = followedID;
	}
	public String getFollowerID() {
		return followerID;
	}
	public void setFollowerID(String followerID) {
		this.followerID = followerID;
	}

	@Override
	public String toString() {
		return "Follow [id=" + id + ", trgId=" + trgId + ", followedID="
				+ followedID + ", followerID=" + followerID + "]";
	}

	
} 
