package transfo;

import java.util.Collection;

import blackboard.BlackboardException;
import blackboard.IArea;
import blackboard.IdentifiableElement;
import blackboard.SearchByIdRange;

public class Slave_InplaceMT implements Runnable, ISlave {

	int threadId;
	IArea todoArea, srcArea, trgArea;
	ITransformation transfoImpl;

	public Slave_InplaceMT(int threadId, ITransformation transfoImpl,
			IArea todoArea, IArea srcArea, IArea trgModelArea) {
		this.threadId = threadId;
		this.todoArea = todoArea;
		this.srcArea = srcArea;
		this.trgArea = trgModelArea;
		this.transfoImpl = transfoImpl;
	}

	@Override
	public synchronized void run() {
		try {
			Job we = askForWork();
			while (we != null) {
				Collection<IdentifiableElement> objs = srcArea.read(
						new SearchByIdRange(we.getMinID(), we.getMaxID(), true));
				transfoImpl.transform(objs, null);
				we = askForWork();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Job askForWork() throws BlackboardException {
		ToDo todoQueue = (ToDo) todoArea.take(LinTraParameters.TODO_ID);
		while(todoQueue == null){
			// it will be null if it is been taken by other slave, therefore the slave must retry until it find it
			todoQueue = (ToDo) todoArea.take(LinTraParameters.TODO_ID);
		}
		Job we = todoQueue.poll();
		/**
		 * depending on the behavior we want when there's no element available
		 * we can choose: l.poll() -> return null l.remove() -> an exception is
		 * thrown l.take() -> waits until there's elements available (the
		 * implementation uses method notify())
		 */
		todoArea.write(todoQueue);
		return we;
	}

}
