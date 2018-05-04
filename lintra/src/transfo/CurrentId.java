package transfo;

import blackboard.IdentifiableElement;

public class CurrentId implements IdentifiableElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	double currentId;
	String id;
	
	String trgId;
	
	@Override
	public String getTrgId() {
		return trgId;
	}

	@Override
	public void setTrgId(String trgId) {
		this.trgId = trgId;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	public synchronized double getCurrentId() {
		return currentId;
	}

	public synchronized void setCurrentId(double currentId) {
		this.currentId = currentId;
	}

	public CurrentId(double currentId) {
		this.id = LinTraParameters.CURRENT_AREA_ID;
		this.currentId = currentId;
	}
	
	public void increase(int range){
		/** returns the range of ids going from currentId to currentId+range ([currentId..currentId+range]) */
		currentId = currentId + range;
	}

	@Override
	public String toString() {
		return "CurrentId [currentId=" + currentId + ", id=" + id + "]";
	}

}
