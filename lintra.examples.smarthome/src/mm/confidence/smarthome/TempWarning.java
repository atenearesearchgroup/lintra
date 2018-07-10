
package mm.confidence.smarthome;

import blackboard.IdentifiableElement;
import uncertaintypes.UBoolean;
import uncertaintypes.UReal;

public class TempWarning extends ProbableTimedElement implements IdentifiableElement{
	
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
	UReal temp;


	public String getHomeID() {
		return homeID;
	}
	public void setHomeID(String homeID) {
		this.homeID = homeID;
	}
	public UReal getTemp() {
		return temp;
	}
	public void setTemp(UReal temp) {
		this.temp = temp;
	}
	
} 
