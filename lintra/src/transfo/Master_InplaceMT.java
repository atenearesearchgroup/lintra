package transfo;

import blackboard.BlackboardException;
import blackboard.IArea;

public class Master_InplaceMT implements IMaster {

	IArea workTODOArea, srcArea;
	int numThreads;
	
	public Master_InplaceMT(IArea workTODOArea, IArea srcModelArea, int numThreads) {
		this.workTODOArea = workTODOArea;
    	this.srcArea = srcModelArea;

    	this.numThreads = numThreads;
	}
	
	public synchronized void organizeWork(IArea area, double maxId) throws BlackboardException {
		/* Inizialize workTODOArea */
    	ToDo todo = new ToDo();
    	double i = 0;
    	int increase = computeOptimalIncrease(numThreads, maxId, LinTraParameters.JOB_SIZE);
    	
    	while(i<maxId){
    		todo.add(new Job(i+1, i+increase));
    		i=i+increase;
    	}
    	workTODOArea.write(todo);	
	}

	private synchronized int computeOptimalIncrease(int numThreads, double maxId, int workExcerptSize) {
		int increase;
		if (maxId / numThreads <  1){
    		increase = 1;
    	} else if (maxId / numThreads <  workExcerptSize){
    		increase = (int) (maxId / numThreads);
    	} else {
    		increase = workExcerptSize;
    	}
		return increase;
	}
}
