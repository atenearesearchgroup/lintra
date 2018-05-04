package transfo;

public class Range {
	double minID;
	double maxID;
	double current;
	
	public Range(double minId, double maxId) {
		this.minID = minId;
		this.maxID = maxId;
		this.current = minId;
	}
	
	public synchronized double getMinID() {
		return minID;
	}
	public synchronized void setMinID(double minID) {
		this.minID = minID;
	}
	public synchronized double getMaxID() {
		return maxID;
	}
	public synchronized void setMaxID(double maxID) {
		this.maxID = maxID;
	}

	public synchronized double getCurrent() {
		return current;
	}

	public synchronized void setCurrent(double current) {
		this.current = current;
	}
	
	public synchronized double next(){
		if (current<=maxID){
			current = current+1;
			return current;
		} else {
			return -1;
		}
	}

	@Override
	public synchronized String toString() {
		return "Range [minID=" + minID + ", maxID=" + maxID + ", current="
				+ current + "]";
	}
	
}
