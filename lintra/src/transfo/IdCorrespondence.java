package transfo;

import blackboard.IdentifiableElement;

public class IdCorrespondence implements IdentifiableElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String targetId, newId;
	
	String trgId;
	
	@Override
	public String getTrgId() {
		return trgId;
	}

	@Override
	public void setTrgId(String trgId) {
		this.trgId = trgId;
	}
	
	public IdCorrespondence(String targetId, String newId) {
		this.targetId = targetId;
		this.newId = newId;
	}
	
	@Override
	public String getId() {
		return targetId;
	}

	@Override
	public void setId(String targetId) {
		this.targetId = targetId;
	}

	public synchronized String getNewId() {
		return newId;
	}

	public synchronized void setNewId(String newId) {
		this.newId = newId;
	}

	@Override
	public String toString() {
		return "IdCorrespondence [targetId=" + targetId + ", newId=" + newId
				+ "]";
	}
	
}
