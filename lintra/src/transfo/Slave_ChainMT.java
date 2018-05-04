package transfo;

import java.util.Collection;
import java.util.List;

import blackboard.BlackboardException;
import blackboard.IArea;
import blackboard.IdentifiableElement;
import blackboard.SearchByIdRange;

public class Slave_ChainMT implements Runnable, ISlave {

	int threadId;
	 IArea srcModelFlagsArea, todoArea, srcArea, trgArea, idCorrespondencesArea;
	 ITransformation transfo;
	 ITransformation nextTransfo;
	 IMaster master, masterNextTransfo;
	 List<ISlave> slaves;
	private boolean gapsAllowed;

	public Slave_ChainMT(int threadId, IMaster master, List<ISlave> slaves, ITransformation transfo,
			IArea srcModelArea, IArea srcModelFlagsArea, boolean gapsAllowed, IArea todoArea, IArea idCorrespondencesArea,
			IArea trgModelArea, ITransformation nextTransfo, IMaster masterNextTransfo) {
		this.threadId = threadId;
		this.master = master;
		this.todoArea = todoArea;
		this.idCorrespondencesArea = idCorrespondencesArea;
		this.srcArea = srcModelArea;
		this.trgArea = trgModelArea;
		this.transfo = transfo;
		this.nextTransfo = nextTransfo;
		this.masterNextTransfo = masterNextTransfo;
		this.srcModelFlagsArea = srcModelFlagsArea;
		this.slaves = slaves;
		this.gapsAllowed = gapsAllowed;
	}

	@Override
	public void run() {

		try {
			ToDoFlags flags = readWorkTODOFlags();
			
			/* More jobs will be received, the slave must be checking although there are no we available right now */
			while (flags.isWaitingForMoreJobs()){
				Job we = askForWork();
				if (we == null){
					synchronized (this) {
						wait();
					}
				} else {
					Collection<IdentifiableElement> objs = srcArea.read(
							new SearchByIdRange(we.getMinID(), we.getMaxID(), gapsAllowed));
				
					transfo.transform(objs, masterNextTransfo);						
				}
				flags = readWorkTODOFlags();
			}
			
			/* No more we will be received, the slave only has to transform the existing ones */
			if (!flags.isWaitingForMoreJobs()){
				Job we = askForWork();
				while (we != null) {
					Collection<IdentifiableElement> objs = srcArea.read(
							new SearchByIdRange(we.getMinID(), we.getMaxID(), gapsAllowed));
					transfo.transform(objs, masterNextTransfo);
					we = askForWork();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private ToDoFlags readWorkTODOFlags() throws BlackboardException, InterruptedException {
		ToDoFlags flags = (ToDoFlags) todoArea.read(LinTraParameters.TODO_FLAGS_ID);
		while (flags==null){
			synchronized (this) {
				wait();
			}
			flags = (ToDoFlags) todoArea.read(LinTraParameters.TODO_FLAGS_ID);
		}
		return flags;
	}

	private Job askForWork() throws BlackboardException, InterruptedException {
		ToDo todoQueue = (ToDo) todoArea.take(LinTraParameters.TODO_ID);
		while(todoQueue == null){ // it will be null if it is been taken by other slave, therefore the slave must retry until it find it
			synchronized (this) {
				wait();
			}
			todoQueue = (ToDo) todoArea.take(LinTraParameters.TODO_ID);
		}
		Job we = todoQueue.poll();
		/**
		 * depending on the behaviour we want when there's no element available
		 * we can choose: l.poll() -> return null l.remove() -> an exception is
		 * thrown l.take() -> waits until there's elements available (the
		 * implementation uses method notify())
		 */
		todoArea.write(todoQueue);
		notifyMaster();
		notifySlaves();
		return we;
	}

	
	private void notifySlaves() {
		for (ISlave s : slaves){
			synchronized (s) {
				s.notifyAll();
//				System.out.println("Master notified Slave");
			}
		}
	}

	private void notifyMaster() {
		synchronized (master) {
			master.notify();
		}
	}

}
