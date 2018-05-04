package blackboard;

import java.io.Serializable;
import java.util.Collection;

import blackboard.IBlackboard.Policy;

public interface IArea extends Serializable {
	
	public String getName();
	
	public Policy getPolicy();
	
	/**
	 * Read the element with identifier id
	 * 
	 * @param id
	 * @return the matching element or null if there is none
	 * @throws BlackboardException 
	 */
	public IdentifiableElement read(String id) throws BlackboardException;
	
	/**
	 * Read all the elements with the given identifiers
	 * 
	 * @param ids
	 * @return all the marching elements or null if there is none
	 * @throws BlackboardException 
	 */
	public Collection<IdentifiableElement> readAll(Collection<String> ids) throws BlackboardException;
	
	/**
	 * Read the elements that fulfill the criteria specified by seachMethod
	 * @param id
	 * @param maxElems
	 * @return
	 * @throws BlackboardException
	 */
	public Collection<IdentifiableElement> read(ISearch searchMethod) throws BlackboardException;
	
	/**
	 * Read randomly n elements from the area.
	 * Use this method carefully, it might happen that the number of elements does not fit in a single machine memory
	 * @return all the element stored in the area
	 * @throws BlackboardException
	 */
	public Collection<IdentifiableElement> read(int n) throws BlackboardException;
	
	/**
	 * Read and remove the element with identifier id
	 * 
	 * @param pattern
	 * @return the matching element or null if there is none
	 * @throws BlackboardException 
	 */
	public IdentifiableElement take(String id) throws BlackboardException;
	
	/**
	 * Read and remove the elements with the given identifiers
	 * 
	 * @param ids
	 * @return
	 * @throws BlackboardException 
	 */
	public Collection<IdentifiableElement> takeAll(Collection<String> ids) throws BlackboardException;
	
	/**
	 * Read and remove the elements that fulfill the criteria specified by seachMethod
	 * @param id
	 * @param maxElems
	 * @return
	 * @throws BlackboardException
	 */
	public Collection<IdentifiableElement> take(ISearch searchMethod) throws BlackboardException;
	
	/**
	 * Read and remove randomly n elements from the area.
	 * Use this method carefully, it might happen that the number of elements does not fit in a single machine memory
	 * @return all the element stored in the area
	 * @throws BlackboardException
	 */
	public Collection<IdentifiableElement> take(int n) throws BlackboardException;
	
	/**
	 * Write elem in the area
	 * 
	 * @param elem
	 * @return true if the element was successfully written or false otherwise
	 * @throws BlackboardException 
	 */
	public boolean write(IdentifiableElement elem) throws BlackboardException;
	
	/**
	 * Write all the elements elems in the area
	 * @param elems
	 * @return true if the elements were successfully written or false otherwise
	 * @throws BlackboardException 
	 */
	public boolean writeAll(Collection<IdentifiableElement> elems) throws BlackboardException;
	
	/**
	 * @return the number of elements stored in the area
	 */
	public int size();
	
	/**
	 * Remove all the elements
	 * @return true if the elements were successfully removed or false otherwise
	 */
	public boolean clear();
	
	/**
	 * Remove all the elements and destroy the area
	 * @return true if the elements were successfully removed or false otherwise
	 */
	public void destroy();
	
	/**
	 * Print all the elements
	 */
	public void print();

}
