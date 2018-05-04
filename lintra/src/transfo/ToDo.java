package transfo;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import blackboard.IdentifiableElement;

public class ToDo implements Serializable, IdentifiableElement {

	
	private static final long serialVersionUID = 1L;
	
	String id;
	BlockingQueue<Job> queue;
	
	String trgId;
	
	@Override
	public String getTrgId() {
		return trgId;
	}

	@Override
	public void setTrgId(String trgId) {
		this.trgId = trgId;
	}
	
	public ToDo(){
		id = LinTraParameters.TODO_ID;
		queue = new  LinkedBlockingQueue<Job>();
	}
	
	
	@Override
	public synchronized String getId() {
		return id;
	}
	
	@Override
	public synchronized void setId(String id) {
		this.id = id;
	}

	public synchronized void add(Job we){
		queue.add(we);
	}
	
	public synchronized Job poll(){
		return queue.poll();
	}
	
	public synchronized Job remove(){
		return queue.remove();
	}
	
	public synchronized Job take() throws InterruptedException{
		return queue.take();
	}
	
	public synchronized String toString(){
		return queue.toString();
	}

}
