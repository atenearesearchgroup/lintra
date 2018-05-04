package blackboard;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import blackboard.IdentifiableElement;
import blackboard.IBlackboard.Policy;

public class HashMapArea implements IArea {
	
	/** Comments to myself:
	 * - After we exit a synchronized block, we release the monitor, which has the effect of flushing the cache to main memory,
	 * so that writes made by this thread can be visible to other threads.
	 * -----> So synchronization is needed in setter, writes/takes and methods that modify some variables.
	 * - Do I need to synchronize getters and reads too?
	 * - Volatile variables share the visibility features of synchronized, but none of the atomicity features. This means that
	 * threads will automatically see the most up-to-date value for volatile variables.
	 * - You might prefer to use volatile variables instead of locks for one of two principal reasons: simplicity or
	 * scalability. Some idioms are easier to code and read when they use volatile variables instead of locks. In addition,
	 * volatile variables (unlike locks) cannot cause a thread to block, so they are less likely to cause scalability problems.
	 * -----> So as long as the variables are volatile, I don't need to synchronize getters, reads or any other method that doesn't
	 * modify the variable. 
	 */

	private static final long serialVersionUID = 1L;
	private volatile String name;
	private volatile Policy policy;
	private volatile Semaphore semaphore;
	private volatile Map<String, IdentifiableElement> area;

	public HashMapArea(String name, Policy p) {
		this.name = name;
		this.policy = p;
		semaphore = new Semaphore(1, true);
		area = new ConcurrentHashMap<String, IdentifiableElement>();
	}

	public String getName() {
		return name;
	}

	public synchronized void setName(String name) {
		this.name = name;
	}

	public Policy getPolicy() {
		return policy;
	}

	public synchronized void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public Semaphore getSemaphore() {
		return semaphore;
	}

	public synchronized void setSemaphore(Semaphore semaphore) {
		this.semaphore = semaphore;
	}

	public Map<String, IdentifiableElement> getArea() {
		return area;
	}

	public synchronized void setArea(Map<String, IdentifiableElement> area) {
		this.area = area;
	}

	@Override
	public synchronized IdentifiableElement read(String id) throws BlackboardException {
		try{
			IdentifiableElement e;
			if (policy.equals(Policy.ALWAYS_LOCK) || policy.equals(Policy.LOCK_TO_READ)){
				semaphore.acquire();
				e = area.get(id);
				semaphore.release();
			} else {
				e = area.get(id);
			}
			return e;
		} catch (InterruptedException e){
			throw new BlackboardException();
		} catch (NullPointerException e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public synchronized Collection<IdentifiableElement> readAll(Collection<String> ids) throws BlackboardException {		
		try{
			Collection<IdentifiableElement> elems;
			if (policy.equals(Policy.ALWAYS_LOCK) || policy.equals(Policy.LOCK_TO_READ)){
				semaphore.acquire();
				elems = readAllAux(ids);
				semaphore.release();
			} else{
				elems = readAllAux(ids);
			}
			return elems;
		} catch (InterruptedException e){
			throw new BlackboardException();
		}
	}

	private synchronized Collection<IdentifiableElement> readAllAux(Collection<String> ids) {
		Collection<IdentifiableElement> out = new LinkedList<IdentifiableElement>();
//		String idsS = "";
		for(String id : ids){
			if (area.get(id)!=null){
//				idsS += id + ", ";
//				System.out.println(id);
				out.add(area.get(id));
			}
		}
//		System.out.println(idsS);
		return out;
	}
	
	@Override
	public synchronized Collection<IdentifiableElement> read(ISearch searchMethod) throws BlackboardException {
		try {  
			Collection<IdentifiableElement> elems;
			if (policy.equals(Policy.ALWAYS_LOCK) || policy.equals(Policy.LOCK_TO_READ)){
				semaphore.acquire();
				elems = searchMethod.search(this);
				semaphore.release();
			} else {
				elems = searchMethod.search(this);
			}
			return elems;
		} catch (InterruptedException e) {
			throw new BlackboardException();
		}
	}
	
	@Override
	public synchronized Collection<IdentifiableElement> read(int n) throws BlackboardException {
		try {  
			Collection<IdentifiableElement> elems;
			if (policy.equals(Policy.ALWAYS_LOCK) || policy.equals(Policy.LOCK_TO_READ)){
				semaphore.acquire();
				elems = readAux(n);
				semaphore.release();
			} else {
				elems = readAux(n);
			}
			return elems;
		} catch (InterruptedException e) {
			throw new BlackboardException();
		}
	}

	private Collection<IdentifiableElement> readAux(int n) {
		if (area.keySet().size() < n){
			return area.values();
		} else {
			String[] keys =  toArray(area.keySet()); // I've decided to load the keys instead of the values directly because a set of String is not as heavy as a set of IdentifiableElements
			Collection<IdentifiableElement> elems = new LinkedList<IdentifiableElement>();
			for (int i=0; i<n; i++){
				elems.add(area.get(keys[i]));
			}
			return elems;
		}
	}

	@Override
	public synchronized IdentifiableElement take(String id) throws BlackboardException {
		try {
			IdentifiableElement e;
			if (policy.equals(Policy.ALWAYS_LOCK) || policy.equals(Policy.LOCK_TO_READ)){
				semaphore.acquire();
				e = area.remove(id);
				semaphore.release();
			} else {
				e = area.remove(id);
			}
			return e;
		} catch (InterruptedException e){
			throw new BlackboardException();
		}
	}

	@Override
	public synchronized Collection<IdentifiableElement> takeAll(Collection<String> ids) throws BlackboardException {
		try {
			Collection<IdentifiableElement> elems;
			if (policy.equals(Policy.ALWAYS_LOCK) || policy.equals(Policy.LOCK_TO_READ)){
				semaphore.acquire();
				elems = takeAllAux(ids);
				semaphore.release();
			} else {
				elems = takeAllAux(ids);
			}
			return elems;
		} catch (InterruptedException e){
			throw new BlackboardException();
		}
	}

	private synchronized Collection<IdentifiableElement> takeAllAux(Collection<String> ids) {
		Collection<IdentifiableElement> out = new LinkedList<IdentifiableElement>();
		for(String id : ids){
			IdentifiableElement e = area.remove(id); 
			if (e != null){
				out.add(e);
			}
		}
		return out;
	}

	@Override
	public synchronized Collection<IdentifiableElement> take(ISearch searchMethod) throws BlackboardException {
		try {
			Collection<IdentifiableElement> elems;
			if (policy.equals(Policy.ALWAYS_LOCK) || policy.equals(Policy.LOCK_TO_READ)){
				semaphore.acquire();
				elems = searchMethod.search(this);
				removeElements(elems);
				semaphore.release();
			} else {
				elems = searchMethod.search(this);
				removeElements(elems);
			}
			return elems;
		} catch (InterruptedException e){
			throw new BlackboardException();
		}
	}

	@Override
	public synchronized Collection<IdentifiableElement> take(int n) throws BlackboardException {
		try {  
			Collection<IdentifiableElement> elems;
			if (policy.equals(Policy.ALWAYS_LOCK) || policy.equals(Policy.LOCK_TO_READ)){
				semaphore.acquire();
				elems = takeAux(n);
				semaphore.release();
			} else {
				elems = takeAux(n);
			}
			return elems;
		} catch (InterruptedException e) {
			throw new BlackboardException();
		}
	}
	
	private synchronized Collection<IdentifiableElement> takeAux(int n) {
		Collection<IdentifiableElement> elems = new LinkedList<IdentifiableElement>();
		if (area.keySet().size() < n){
			elems = new LinkedList<IdentifiableElement>(area.values());
			area.clear();
		} else {
			String[] keys = toArray(area.keySet()); // I've decided to load the keys instead of the values directly because a set of String is not as heavy as a set of IdentifiableElements
			for (int i=0; i<n; i++){
				elems.add(area.remove(keys[i]));
			}
		}
		return elems;
	}

	private synchronized String[] toArray(Set<String> keySet) {
		String[] array = new String[keySet.size()];
		int i = 0;
		for (String s : keySet){
			array[i] = s;
			i++;
		}
		return array;
	}

	private synchronized void removeElements(Collection<IdentifiableElement> elems) {
		for (IdentifiableElement e : elems){
			area.remove(e.getId());
		}
	}

	@Override
	public synchronized boolean write(IdentifiableElement elem) throws BlackboardException {
		try {
			if (policy.equals(Policy.ALWAYS_LOCK) || policy.equals(Policy.LOCK_TO_WRITE)){
				semaphore.acquire();
				area.put(elem.getId(), elem);
				semaphore.release();
			} else {
				area.put(elem.getId(), elem);
			}
			return true;
		} catch (InterruptedException e){
			throw new BlackboardException();
		}
	}

	@Override
	public synchronized boolean writeAll(Collection<IdentifiableElement> elems) throws BlackboardException {
		try {
			if (elems!=null){
				if (policy.equals(Policy.ALWAYS_LOCK) || policy.equals(Policy.LOCK_TO_WRITE)){
					semaphore.acquire();
					writeAllAux(elems);
					semaphore.release();
				} else {
					writeAllAux(elems);
				}
			}
			return true;
		} catch (InterruptedException e){
			throw new BlackboardException();
		}
	}

	private synchronized void writeAllAux(Collection<IdentifiableElement> elems) {
		
		for (IdentifiableElement e : elems){
			try {
				if (area.containsKey(e.getId())){
//					System.out.println("--->"+area.get(e.getId()) + " is being overwritten by " + e);
				}
				area.put(e.getId(), e);
			} catch (NullPointerException ex){
//				System.out.println("what's going on here? "+e.toString());
			}
		}
	}

	@Override
	public synchronized int size() {
		return area.keySet().size();
	}

	@Override
	public synchronized boolean clear() {
		area.clear();
		return area.keySet().size()==0;
	}
	

	@Override
	public void destroy() {
		area.clear();
		area = null;
	}

	@Override
	public synchronized void print() {
		System.out.println("*Area: "+name+"*");
		for (String id : area.keySet()){
			System.out.println(area.get(id));
		}
	}
	
	@Override
	public synchronized boolean equals(Object o){
		return o instanceof HashMapArea && ((HashMapArea) o).getName().equals(name);		
	}

	@Override
	public synchronized String toString() {
		return "HashMapArea [name=" + name + ", policy=" + policy
				+ ", semaphore=" + semaphore + ", area=" + area + "]";
	}

	
}
