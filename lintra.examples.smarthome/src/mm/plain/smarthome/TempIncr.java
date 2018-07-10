
package mm.plain.smarthome;

import blackboard.IdentifiableElement;

public class TempIncr extends ProbableTimedElement implements IdentifiableElement{
	
	String id, trgId;
	
	public TempIncr() {
	}
	
	public TempIncr(String id, String homeID, double temp, double incr, double ts) {
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
	double temp;
	double incr;


	public String getHomeID() {
		return homeID;
	}
	public void setHomeID(String homeID) {
		this.homeID = homeID;
	}
	public double getTemp() {
		return temp;
	}
	public void setTemp(double temp) {
		this.temp = temp;
	}
	public double getIncr() {
		return incr;
	}
	public void setIncr(double incr) {
		this.incr = incr;
	}

	@Override
	public String toString() {
		return "TempIncr [id=" + id + ", homeID=" + homeID + ", temp=" + temp
				+ ", incr=" + incr + "]";
	}
	
} 
