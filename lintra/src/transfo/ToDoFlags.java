package transfo;

import blackboard.IdentifiableElement;

public class ToDoFlags implements IdentifiableElement {
	
	private static final long serialVersionUID = 1L;
	
	private boolean waitingForMoreJobs;
	private double maxId;
	private String id = LinTraParameters.TODO_FLAGS_ID;
	
	String trgId;
	
	@Override
	public String getTrgId() {
		return trgId;
	}

	@Override
	public void setTrgId(String trgId) {
		this.trgId = trgId;
	}
	
	public ToDoFlags(boolean waitingForMoreJobs, double maxId) {
		this.waitingForMoreJobs = waitingForMoreJobs;
		this.maxId = maxId;
	}
	@Override
	public synchronized String getId() {
		return id;
	}
	@Override
	public synchronized void setId(String id) {
		this.id = id;
	}
	public synchronized boolean isWaitingForMoreJobs() {
		return waitingForMoreJobs;
	}
	public synchronized void setWaitingForMoreJobs(boolean waitingForMoreJobs) {
		this.waitingForMoreJobs = waitingForMoreJobs;
	}
	public synchronized double getMaxId() {
		return maxId;
	}
	public synchronized void setMaxId(double maxId) {
		this.maxId = maxId;
	}
	
	@Override
	public synchronized String toString() {
		return "WorkToDoAreaFlags [waitingForMoreJobs=" + waitingForMoreJobs
				+ ", maxId=" + maxId + ", id=" + id + "]";
	}

}
