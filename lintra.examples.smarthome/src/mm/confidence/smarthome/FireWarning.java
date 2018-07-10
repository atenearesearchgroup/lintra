
package mm.confidence.smarthome;

import blackboard.IdentifiableElement;
import uncertaintypes.UBoolean;
import uncertaintypes.UReal;

public class FireWarning extends ProbableTimedElement implements IdentifiableElement{
	
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

	String callID, twID;


	public String getCallID() {
		return callID;
	}
	public void setCallID(String callID) {
		this.callID = callID;
	}
	public String getTwID() {
		return twID;
	}
	public void setTwID(String twID) {
		this.twID = twID;
	}	

	
} 
