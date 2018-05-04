
package smarthome;

import blackboard.IdentifiableElement;
import uncertaintypes.UBoolean;
import uncertaintypes.UReal;

public class TempIncr extends ProbableTimedElement implements IdentifiableElement{
	
	String id, trgId;
	
	public TempIncr() {
	}
	
	public TempIncr(String id, String homeID, UReal temp, UReal incr, UReal ts) {
		super();
		this.id = id;
		this.homeID = homeID;
		this.temp = temp;
		this.incr = incr;
		this.ts = ts;
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

	String homeID;	
	UReal temp;
	UReal incr;


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
	public UReal getIncr() {
		return incr;
	}
	public void setIncr(UReal incr) {
		this.incr = incr;
	}

	@Override
	public String toString() {
		return "TempIncr [id=" + id + ", homeID=" + homeID + ", temp=" + temp
				+ ", incr=" + incr + "]";
	}
	
} 
