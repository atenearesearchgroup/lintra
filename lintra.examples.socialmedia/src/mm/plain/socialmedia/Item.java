
package mm.plain.socialmedia;

import java.util.List;

import blackboard.IdentifiableElement;
import uncertaintypes.UBoolean;
import uncertaintypes.UReal;

public abstract class Item extends ProbableElement implements IdentifiableElement{
	
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

	List<String> likesIDs;


	public List<String> getLikesIDs() {
		return likesIDs;
	}
	public void setLikesIDs(List<String> likesIDs) {
		this.likesIDs = likesIDs;
	}
	@Override
	public String toString() {
		return "Item [id=" + id + ", trgId=" + trgId + ", likesIDs=" + likesIDs
				+ "]";
	}


} 
