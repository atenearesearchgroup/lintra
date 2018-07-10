
package mm.plain.smarthome;

import blackboard.IdentifiableElement;

public class COHigh extends ProbableTimedElement implements IdentifiableElement{
	
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

	String homeID;	



	public String getHomeID() {
		return homeID;
	}
	public void setHomeID(String homeID) {
		this.homeID = homeID;
	}
	
	
} 
