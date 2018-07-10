
package mm.plain.smarthome;

import blackboard.IdentifiableElement;


public class ProbableTimedElement {
	
	double confidence;

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	
	protected double ts;

	public double getTs() {
		return ts;
	}

	public void setTs(double ts) {
		this.ts = ts;
	}
	
} 