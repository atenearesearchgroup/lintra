package blackboard;

import java.io.Serializable;

public interface IdentifiableElement extends Serializable {
	
	public String getId();
	
	public void setId(String id);
	
	public String getTrgId();
	
	public void setTrgId(String trgId);
	
}
