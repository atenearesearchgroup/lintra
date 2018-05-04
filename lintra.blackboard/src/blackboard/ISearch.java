package blackboard;

import java.util.Collection;

public interface ISearch {
	
	public Collection<IdentifiableElement> search(IArea area) throws BlackboardException;
	
}
