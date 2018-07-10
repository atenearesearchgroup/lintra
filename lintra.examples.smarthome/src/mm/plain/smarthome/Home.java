package mm.plain.smarthome;

import java.util.List;

import blackboard.IdentifiableElement;

public class Home extends ProbableTimedElement implements IdentifiableElement {

	String id, trgId;

	public Home(String id, String hIdentifier, String locationID, double temp,
			double co, boolean dopen, double sqre, double ts) {
		this.id = id;
		this.hIdentifier = hIdentifier;
		this.locationID = locationID;
		this.temp = temp;
		this.co = co;
		this.dopen = dopen;
		this.sqre = sqre;
		this.ts = ts;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTrgId() {
		return trgId;
	}

	public void setTrgId(String trgId) {
		this.trgId = trgId;
	}

	String hIdentifier;
	String locationID;
	double temp;
	double co;
	boolean dopen;
	double sqre;
	List<String> tempIncrIDs, coHighIDs, nobodyHIDs, tempWarningIDs;

	public String gethIdentifier() {
		return hIdentifier;
	}

	public void sethIdentifier(String hIdentifier) {
		this.hIdentifier = hIdentifier;
	}

	public String getLocationID() {
		return locationID;
	}

	public void setLocationID(String locationID) {
		this.locationID = locationID;
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	public double getCo() {
		return co;
	}

	public void setCo(double co) {
		this.co = co;
	}

	public boolean getDopen() {
		return dopen;
	}

	public void setDopen(boolean dopen) {
		this.dopen = dopen;
	}

	public double getSqre() {
		return sqre;
	}

	public void setSqre(double sqre) {
		this.sqre = sqre;
	}

	public List<String> getTempIncrIDs() {
		return tempIncrIDs;
	}

	public void setTempIncrIDs(List<String> tempIncrIDs) {
		this.tempIncrIDs = tempIncrIDs;
	}

	public List<String> getCoHighIDs() {
		return coHighIDs;
	}

	public void setCoHighIDs(List<String> coHighIDs) {
		this.coHighIDs = coHighIDs;
	}

	public List<String> getNobodyHIDs() {
		return nobodyHIDs;
	}

	public void setNobodyHIDs(List<String> nobodyHIDs) {
		this.nobodyHIDs = nobodyHIDs;
	}

	public List<String> getTempWarningIDs() {
		return tempWarningIDs;
	}

	public void setTempWarningIDs(List<String> tempWarningIDs) {
		this.tempWarningIDs = tempWarningIDs;
	}

}
