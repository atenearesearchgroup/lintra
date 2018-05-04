package transfo;

import blackboard.IdentifiableElement;

public class ModelFlags implements IdentifiableElement {
	
	private static final long serialVersionUID = 1L;
	
	private boolean complete;
	private double maxIdStored;
	private String id = LinTraParameters.MODEL_FLAGS_ID;
	
	String trgId;
	
	@Override
	public String getTrgId() {
		return trgId;
	}

	@Override
	public void setTrgId(String trgId) {
		this.trgId = trgId;
	}
	
	public ModelFlags(boolean complete, double maxIdStored) {
		this.complete = complete;
		this.maxIdStored = maxIdStored;
	}
	@Override
	public String getId() {
		return id;
	}
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	public boolean isComplete() {
		return complete;
	}
	public void setComplete(boolean fullyLoaded) {
		this.complete = fullyLoaded;
	}
	public double getMaxIdStored() {
		return maxIdStored;
	}
	public void setMaxIdStored(double maxIdStored) {
		this.maxIdStored = maxIdStored;
	}
	@Override
	public String toString() {
		return "ModelAreaFlags [complete=" + complete + ", maxIdStored="
				+ maxIdStored + ", id=" + id + "]";
	}
	
}
