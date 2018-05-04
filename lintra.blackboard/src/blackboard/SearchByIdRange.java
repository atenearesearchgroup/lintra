package blackboard;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SearchByIdRange implements ISearch {
	
	double minId, maxId;
	boolean gapsAllowed;
	
	public SearchByIdRange(double d, double e, boolean gapsAllowed){
		this.minId = d;
		this.maxId = e;
		this.gapsAllowed = gapsAllowed;
	}

	@Override
	public Collection<IdentifiableElement> search(IArea area) throws BlackboardException {
		
		if (gapsAllowed){
			return searchForAvailables(area);
		} else {
			return searchForAll(area);
		}
		
	}

	private Collection<IdentifiableElement> searchForAvailables(IArea area) throws BlackboardException {
		Collection<IdentifiableElement> ies = new LinkedList<IdentifiableElement>();
		for (double i = minId; i <= maxId; i++) {
			IdentifiableElement ie = area.read(Double.toString(i));
			if (ie != null){
				ies.add(ie);
			}
		}
		return ies;
	}

	private Collection<IdentifiableElement> searchForAll(IArea area) throws BlackboardException {
		Collection<IdentifiableElement> ies = new LinkedList<IdentifiableElement>();
		for (double i = minId; i <= maxId; i++) {
			IdentifiableElement ie = area.read(Double.toString(i));
			while (ie==null){
				ie = area.read(Double.toString(i));
			}
			ies.add(ie);
		}
		return ies;
	}
	
}

