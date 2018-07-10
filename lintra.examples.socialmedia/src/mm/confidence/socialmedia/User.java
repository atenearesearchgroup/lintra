
package mm.confidence.socialmedia;

import java.util.LinkedList;
import java.util.List;

import blackboard.IdentifiableElement;
import uncertaintypes.UBoolean;
import uncertaintypes.UReal;

public class User extends ProbableElement implements IdentifiableElement{
	
	String id, trgId;
	
	public User() {
		this.followedByIDs = new LinkedList<String>();
		this.followsIDs = new LinkedList<String>();
		this.likesIDs = new LinkedList<String>();
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

	List<String> followedByIDs, followsIDs;	
	List<String> likesIDs;


	public List<String> getFollowedByIDs() {
		return followedByIDs;
	}
	public void setFollowedByIDs(List<String> followedByIDs) {
		this.followedByIDs = followedByIDs;
	}
	public List<String> getFollowsIDs() {
		return followsIDs;
	}
	public void setFollowsIDs(List<String> followsIDs) {
		this.followsIDs = followsIDs;
	}
	public List<String> getLikesIDs() {
		return likesIDs;
	}
	public void setLikesIDs(List<String> likesIDs) {
		this.likesIDs = likesIDs;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", trgId=" + trgId + ", followedByIDs="
				+ followedByIDs + ", followsIDs=" + followsIDs + ", likesIDs="
				+ likesIDs + "]";
	}
	
} 
