package mm.plain.surveillance;

import blackboard.IdentifiableElement;

public class Clock implements IdentifiableElement{
	
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
	
	public int now;
	public int getNow() {
		return now;
	}
	public void setNow(int now) {
		this.now = now;
	}
	@Override
	public String toString() {
		return "Clock [id=" + id + ", trgId=" + trgId + ", now=" + now + "]";
	}
	
}
