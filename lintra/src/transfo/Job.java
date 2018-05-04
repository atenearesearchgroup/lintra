package transfo;

import java.io.Serializable;

import blackboard.IArea;

public class Job implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	double minID;
	double maxID;
	
	/**
	 * Elements in area whose identifiers go from minID to maxID 
	 * @param area
	 * @param minID
	 * @param maxID
	 */
	public Job(double minID, double maxID){
		this.minID = minID;
		this.maxID = maxID;
	}

	public double getMinID() {
		return minID;
	}

	public void setMinID(int minID) {
		this.minID = minID;
	}

	public double getMaxID() {
		return maxID;
	}

	public void setMaxID(int maxID) {
		this.maxID = maxID;
	}
	
	public String toString(){
		return "[" + minID + ".." + maxID + "]";
	}
	
}
