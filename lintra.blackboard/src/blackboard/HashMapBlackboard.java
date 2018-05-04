package blackboard;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class HashMapBlackboard implements IBlackboard {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	volatile List<IArea> areas;
	
	public HashMapBlackboard(){
		areas = new LinkedList<IArea>();
	}
	
	@Override
	public synchronized IArea createArea(String name, Policy p) {
		IArea hsA = new HashMapArea(name, p);
		areas.add(hsA);
		return hsA;
	}
	
	@Override
	public synchronized boolean clearArea(IArea area) {
		boolean ok = false;
		boolean found = false;
		int i = 0;
		while (!found) {
			if (areas.get(i).equals(area)) {
				ok = areas.get(i).clear();
				found = true;
			}
			i++;
		}
		return ok;
	}

	@Override
	public synchronized boolean destroyArea(IArea area) {
		boolean ok = false;
		boolean found = false;
		int i = 0;
		while (!found  && i<areas.size()) {
			if (areas.get(i).equals(area)) {
				areas.get(i).clear();
				areas.remove(i);
				found = true;
			}
			i++;
		}
		return ok;
	}

	@Override
	public synchronized Collection<IArea> getAllAreas() {
		return areas;
	}

	@Override
	public synchronized int size() {
		int num = 0;
		for (IArea a : areas){
			num += a.size();
		}
		return num;
	}

	@Override
	public synchronized int size(IArea area) {
		return area.size();
	}

	@Override
	public synchronized boolean clear() {
		boolean ok = true;
		int i = 0;
		while (ok && i<areas.size()){
			ok = areas.get(i).clear();
			areas.remove(i);
			i++;
		}
		return ok;
	}

	@Override
	public synchronized void print() {
		System.out.println("** Blackboard **");
		for (IArea a : areas){
			a.print();
		}
	}

	@Override
	public synchronized void printArea(IArea a) {
		a.print();
	}

	@Override
	public synchronized String toString() {
		return "HashMapBlackboard [areas=" + areas + "]";
	}
}
