package blackboard;

import java.io.Serializable;
import java.util.Collection;

public interface IBlackboard extends Serializable {
	
	public enum Policy {NEVER_LOCK, LOCK_TO_READ, LOCK_TO_WRITE, ALWAYS_LOCK};
	
	/**
	 * Create a new Area
	 * 
	 * @param name
	 * @param p
	 * @return the created area or null if an area with the same name already exists
	 */
	public IArea createArea(String name, Policy p);
	
	/**
	 * Remove all the elements in area
	 *  
	 * @param area
	 * @return true if the elements were successfully removed or false otherwise
	 */
	public boolean clearArea(IArea area);
	
	/**
	 * Remove all the elements from area and destroy the area
	 * 
	 * @param area
	 * @return true if the area was successfully destroyed or false otherwise
	 */
	public boolean destroyArea(IArea area);
	
	/**
	 * Get all the areas in the backboard
	 * 
	 * @return the collection of areas in the backboard
	 */
	public Collection<IArea> getAllAreas();
	
	/**
	 * @return the number of elements stored in the backboard
	 */
	public int size();
	
	/**
	 * @param area
	 * @return the number of elements stored in area
	 */
	public int size(IArea area);
	
	/**
	 * Remove all the areas from the backboard and all the elements from those areas
	 *  
	 * @return true if the elements were successfully removed or false otherwise
	 */
	public boolean clear();

	/**
	 * Print all the areas stored in the backboard and their elements
	 */
	public void print();
	
	/**
	 * Print all the elements stored in Area a
	 */
	public void printArea(IArea a);


}
