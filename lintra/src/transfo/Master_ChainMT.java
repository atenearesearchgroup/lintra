package transfo;

import java.util.List;

import blackboard.BlackboardException;
import blackboard.IArea;

public class Master_ChainMT implements IMaster, Runnable {
	
	int threadId; 
	IArea workTODOArea, srcArea, srcModelFlagsArea, currentIdArea, trgArea, trgModelFlagsArea;
	List<ISlave> slaves;
	
	public Master_ChainMT(int threadId, IArea workTODOArea, IArea currentIdArea, IArea srcModelArea,
			IArea srcModelFlagsArea, IArea trgArea, IArea trgModelFlagsArea, List<ISlave> slavesT1) {
		this.threadId = threadId;
		this.workTODOArea = workTODOArea;
    	this.srcArea = srcModelArea;
    	this.srcModelFlagsArea = srcModelFlagsArea;
    	this.currentIdArea = currentIdArea;
    	this.trgArea = trgArea;
    	this.trgModelFlagsArea = trgModelFlagsArea;
    	this.slaves = slavesT1;
	}
	
	public List<ISlave> getSlaves() {
		return slaves;
	}

	public void setSlaves(List<ISlave> slaves) {
		this.slaves = slaves;
	}

	public void run() {
		try {

//			System.out.println("Master organizing work...");
			ModelFlags modelFlags = readModelFlags();
			ToDoFlags workTODOFlags = readWorkTODOFlags();
			
				while (!modelFlags.isComplete()){
				// if T1_AND_THEN_T2 the model will be completely loaded before the master 
				//	starts organizing the work thus, the body of the while won't be executed	
					
					if (workTODOFlags.getMaxId() < modelFlags.getMaxIdStored()){
						
						createJobsAndUpdateFlags(modelFlags, workTODOFlags);
						notifySlaves();
					} 
	
					else if (workTODOFlags.getMaxId() == modelFlags.getMaxIdStored()){
//						System.out.println("Master "+threadId+" waiting...");
						synchronized (this) {
							wait();
						}
//						System.out.println("Master awakes");
					}
					
					
					modelFlags = readModelFlags();
					workTODOFlags = readWorkTODOFlags();
				}
//			System.out.println("Master aware model is complete");
			/** modelFlags.isComplete() == true */
			if (workTODOFlags.getMaxId() < modelFlags.getMaxIdStored()){
				createJobsAndUpdateFlags(modelFlags, workTODOFlags);
				notifySlaves();
			}
			workTODOFlags.setWaitingForMoreJobs(false);
			updateTODOFlags(workTODOFlags);
			notifySlaves();
	
//			System.out.println("Master finished organizing work");
		} catch (BlackboardException e){
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void updateTODOFlags(ToDoFlags workTODOFlags) throws BlackboardException, InterruptedException {
		// No need to take them, the master is the only one updating them so they won't be overritten with and older value.
		workTODOArea.write(workTODOFlags);
	}

	private void createJobsAndUpdateFlags(ModelFlags modelFlags, ToDoFlags workTODOFlags) throws BlackboardException, InterruptedException {
		
		/** createNewJobs */
		double initialId = workTODOFlags.getMaxId();
		double finalId = modelFlags.getMaxIdStored();
		
//		System.out.println("Master creating new jobs from "+ initialId +" to "+finalId+"...");
		
    	double i; if (initialId<=0.0) { i = 0; } else { i = initialId; }
    	int increase = LinTraParameters.JOB_SIZE; //computeOptimalIncrease(initialId, finalId, 500);
    	
    	ToDo todo = takeTODO();
    	while(i<=finalId){
    		double fin; if (i+increase>finalId) { fin = finalId; } else { fin = i+increase; }
    		Job j = new Job(i+1, fin);
    		todo.add(j);
//    		System.out.println(threadId+" "+j);
    		i=i+increase;
    	}
//    	System.out.println(threadId+" "+todo);
    	workTODOArea.write(todo);
    	notifySlaves();
    	
    	/** update workTODOFlags */
    	workTODOFlags.setMaxId(finalId);
    	if (modelFlags.isComplete()){
    		workTODOFlags.setWaitingForMoreJobs(false);
    	}
    	updateTODOFlags(workTODOFlags);
    	notifySlaves();
	}

	private void notifySlaves() {
		for (ISlave s : slaves){
			synchronized (s) {
				s.notifyAll();
//				System.out.println("Master notified Slave");
			}
		}
	}
	
	private ToDoFlags readWorkTODOFlags() throws BlackboardException, InterruptedException {
		ToDoFlags workTODOFlags = (ToDoFlags) workTODOArea.read(LinTraParameters.TODO_FLAGS_ID);
		while (workTODOFlags==null){
			synchronized (this) {
				wait();
			}
			workTODOFlags = (ToDoFlags) workTODOArea.read(LinTraParameters.TODO_FLAGS_ID);
		}
		return workTODOFlags;
	}

	private ModelFlags readModelFlags() throws BlackboardException, InterruptedException {
		ModelFlags modelFlags = (ModelFlags)srcModelFlagsArea.read(LinTraParameters.MODEL_FLAGS_ID);
		while (modelFlags==null){ //it might be null if it isn't in the Area because the agent(s) who is(are) loading the model is/are updating it
			synchronized (this) {
				wait();
			}
			modelFlags = (ModelFlags)srcModelFlagsArea.read(LinTraParameters.MODEL_FLAGS_ID);
		}
		return modelFlags;
	}
	
	private ToDo takeTODO() throws BlackboardException, InterruptedException {
		ToDo todo = (ToDo)workTODOArea.take(LinTraParameters.TODO_ID);
		while (todo==null){ //it might be null if it isn't in the Area because the agent(s) who is(are) loading the model is/are updating it
			synchronized (this) {
				wait();
			}
			todo = (ToDo)workTODOArea.take(LinTraParameters.TODO_ID);
		}
		return todo;
	}

	private int computeOptimalIncrease(double initialId, double finalId) {
		int increase;
		if ((finalId-initialId) / slaves.size() <  1){
    		increase = 1;
    	} else if ((finalId-initialId) / slaves.size() <  LinTraParameters.JOB_SIZE){
    		increase = (int) (finalId / slaves.size());
    	} else {
    		increase = LinTraParameters.JOB_SIZE;
    	}
		return increase;
	}

}
