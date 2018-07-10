
package mm.confidence.smarthome;

import uncertaintypes.UReal;
import blackboard.IdentifiableElement;


public class ProbableTimedElement {
	
	double confidence;

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	
	protected UReal ts;

	public UReal getTs() {
		return ts;
	}

	public void setTs(UReal ts) {
		this.ts = ts;
	}
	
} 