package runners;

import java.util.LinkedList;
import java.util.List;

import transfo.CurrentId;
import transfo.IMaster;
import transfo.ISlave;
import transfo.ITransformation;
import transfo.LinTraParameters;
import transfo.Master_ChainMT;
import transfo.ModelFlags;
import transfo.ModelLoader_Chain;
import transfo.Slave_ChainMT;
import transfo.ToDo;
import transfo.ToDoFlags;
import blackboard.BlackboardException;
import blackboard.HashMapBlackboard;
import blackboard.IArea;
import blackboard.IBlackboard;
import blackboard.IBlackboard.Policy;

public class MTChainLauncher {

	String modelPath;
	 IBlackboard blackboard;
	 IArea workTODOArea, workTODOArea2,
		srcModelArea, srcModelFlagsArea,
		trg1ModelArea, trg1ModelFlagsArea,
		trg2ModelArea, trg2ModelFlagsArea,
		currentIdArea, currentIdArea2,
		idCorrespondencesArea1, idCorrespondencesArea2;
	
	public IArea getSrcArea(){
		return srcModelArea;
	}
	
	public IArea getTrg1Area(){
		return trg1ModelArea;
	}
	
	public IArea getTrg2Area(){
		return trg2ModelArea;
	}
	
	public IArea getIdCorrespondences1() {
		return idCorrespondencesArea1;
	}
	
	public IArea getIdCorrespondences2() {
		return idCorrespondencesArea2;
	}

	public void setSrcArea(IArea srcArea){
		srcModelArea = srcArea;
	}
	
	public IArea getCurrentIdArea() {
		return currentIdArea;
	}

	public IArea getCurrentIdArea2() {
		return currentIdArea2;
	}
	
	public IArea getIdCorrespondencesArea1() {
		return idCorrespondencesArea1;
	}
	
	public IArea getIdCorrespondencesArea2() {
		return idCorrespondencesArea2;
	}
	
	public IArea getSrcModelFlagsArea() {
		return srcModelFlagsArea;
	}
	
	public IArea getTrg1ModelFlagsArea() {
		return trg1ModelFlagsArea;
	}
	
	public IArea getTrg2ModelFlagsArea() {
		return trg2ModelFlagsArea;
	}

	public void createBlackboard() throws BlackboardException{
		blackboard = new HashMapBlackboard();
//		blackboard = new HazelcastBlackboard();
//		blackboard = new EhcacheBlackboard();
		
		workTODOArea = blackboard.createArea("jini://*/*/processorSpace", Policy.LOCK_TO_READ); initializeFlagsInWorkTODO(workTODOArea); initializeWorkTODO(workTODOArea);
		workTODOArea2 = blackboard.createArea("jini://*/*/processorSpace2", Policy.LOCK_TO_READ); initializeFlagsInWorkTODO(workTODOArea2); initializeWorkTODO(workTODOArea2);
		srcModelArea = blackboard.createArea("jini://*/*/processorSpace_Src", Policy.NEVER_LOCK);
		srcModelFlagsArea = blackboard.createArea("jini://*/*/processorSpace_SrcModelFlags", Policy.NEVER_LOCK); initializeFlagsInSrc(srcModelFlagsArea);
		trg1ModelArea = blackboard.createArea("jini://*/*/processorSpace_Trg1", Policy.LOCK_TO_WRITE);
		trg1ModelFlagsArea = blackboard.createArea("jini://*/*/processorSpace_Trg1ModelFlags", Policy.LOCK_TO_READ); initializeFlagsInSrc(trg1ModelFlagsArea);
		trg2ModelArea = blackboard.createArea("jini://*/*/processorSpace_Trg2", Policy.LOCK_TO_WRITE);
		trg2ModelFlagsArea = blackboard.createArea("jini://*/*/processorSpace_Trg2ModelFlags", Policy.NEVER_LOCK); initializeFlagsInSrc(trg2ModelFlagsArea);
		currentIdArea = blackboard.createArea("currentId1", Policy.LOCK_TO_READ); initializeCurrentIdArea(currentIdArea);
		currentIdArea2 = blackboard.createArea("currentId2", Policy.LOCK_TO_READ);  initializeCurrentIdArea(currentIdArea2);
		idCorrespondencesArea1 = blackboard.createArea("idCorrespondences1", Policy.NEVER_LOCK);
		idCorrespondencesArea2 = blackboard.createArea("idCorrespondences2", Policy.NEVER_LOCK);
		
	}

	private void initializeCurrentIdArea(IArea currentIdArea) throws BlackboardException {
		CurrentId cid = new CurrentId(1.0);
		currentIdArea.write(cid);
	}

	private void initializeWorkTODO(IArea workTODOArea) throws BlackboardException {
		ToDo todo = new ToDo();
		workTODOArea.write(todo);
	}

	private void initializeFlagsInSrc(IArea srcModelArea) throws BlackboardException {
		ModelFlags f = new ModelFlags(false, -1);
		srcModelArea.write(f);
	}
	
	private void initializeFlagsInWorkTODO(IArea workTODOArea) throws BlackboardException {
		ToDoFlags f = new ToDoFlags(true, -1);
		workTODOArea.write(f);
	}
	
	public void loadModel(String[] modelPath, ITransformation nextTransfo, IMaster master) throws Exception {
		List<Thread> ts = new LinkedList<Thread>();
		for (int i=0; i<modelPath.length; i++){
			Thread t = new Thread(new ModelLoader_Chain(modelPath[i], srcModelArea, srcModelFlagsArea, master, nextTransfo));
			ts.add(t);
			t.start();
//			t.join();
		}
		for (int i=0; i<modelPath.length; i++){
			ts.get(i).join();
		}
		setComplete(srcModelFlagsArea);
		notifyMaster(master);

	}
	
	public double launch_T1_T2_in_Parallel(String[] modelPath,
			ITransformation transfo1, ITransformation transfo2) throws Exception{
		
		IMaster master1 = new Master_ChainMT(1, workTODOArea, currentIdArea, srcModelArea, srcModelFlagsArea, trg1ModelArea, trg1ModelFlagsArea, null);
		IMaster master2 = new Master_ChainMT(2, workTODOArea2, currentIdArea2, trg1ModelArea, trg1ModelFlagsArea, trg2ModelArea, trg2ModelFlagsArea, null);
		
		loadModel(modelPath, transfo1, master1);
		
		double time0 = System.currentTimeMillis();
		
		List<Thread> threadsT1 = new LinkedList<Thread>();
		List<ISlave> slavesT1 = new LinkedList<ISlave>();
		for (int j=0; j<LinTraParameters.NUMBER_OF_THREADS_T1; j++){
			ISlave s = new Slave_ChainMT(j, master1, slavesT1, transfo1, srcModelArea, srcModelFlagsArea, true, workTODOArea, idCorrespondencesArea1, trg1ModelArea, transfo1, master2);
			Thread t = new Thread((Runnable) s);
			threadsT1.add(t);
			slavesT1.add(s);
    	}
		List<Thread> threadsT2 = new LinkedList<Thread>();
		List<ISlave> slavesT2 = new LinkedList<ISlave>();
		for (int j=0; j<LinTraParameters.NUMBER_OF_THREADS_T2; j++){
			ISlave s = new Slave_ChainMT(j+LinTraParameters.NUMBER_OF_THREADS_T1, master2, slavesT2, transfo2, trg1ModelArea, trg1ModelFlagsArea, false, workTODOArea2, idCorrespondencesArea2, trg2ModelArea, null, null);
			Thread t = new Thread((Runnable) s);
			threadsT2.add(t);
			slavesT2.add(s);
    	}
		
		/** Launch masters for T1 and T2 */
		((Master_ChainMT) master1).setSlaves(slavesT1);
		Thread masterThread = new Thread((Runnable) master1);
		masterThread.setPriority(Thread.MAX_PRIORITY);
		((Master_ChainMT) master2).setSlaves(slavesT2);
		Thread master2Thread = new Thread((Runnable) master2);
		master2Thread.setPriority(Thread.MAX_PRIORITY);
		
		masterThread.start();
		master2Thread.start();
				
		/** Launch slaves for T1 and T2 */
		for (int j=0; j<LinTraParameters.NUMBER_OF_THREADS_T1; j++){
			threadsT1.get(j).start();				
    	}
		for (int j=0; j<LinTraParameters.NUMBER_OF_THREADS_T2; j++){
			threadsT2.get(j).start();
    	}
		
		/** Start loading model */
//		loadModel(modelPath, transfo1, master1);
		
		/** Wait for the T1 master and the slaves to finish */
		for (Thread t : threadsT1){
			t.join();
		}
		masterThread.join();
		
		/** Set trg1 model as complete */
		setComplete(trg1ModelFlagsArea);
//		System.out.println("Model in trg1 complete");
		notifyMaster(master2);
		
		/** Wait for the T2 master and slaves to finish */
		for (Thread t : threadsT2){
			t.join();
		}
		master2Thread.join();
		
		
		double timeF = (System.currentTimeMillis() - time0) / 1000;
		return timeF;
	}

	public double launch_T1_then_T2(String[] modelPath,
			ITransformation transfo1, ITransformation transfo2) throws Exception{
		
		IMaster master1 = new Master_ChainMT(1, workTODOArea, currentIdArea, srcModelArea, srcModelFlagsArea, trg1ModelArea, trg1ModelFlagsArea, null);
		IMaster master2 = new Master_ChainMT(2, workTODOArea2, currentIdArea2, trg1ModelArea, trg1ModelFlagsArea, trg2ModelArea, trg2ModelFlagsArea, null);
		
		loadModel(modelPath, transfo1, master1);
		
		double time0 = System.currentTimeMillis();
		
		/** T1  */
		
		List<Thread> threadsT1 = new LinkedList<Thread>();
		List<ISlave> slavesT1 = new LinkedList<ISlave>();
		for (int j=0; j<LinTraParameters.NUMBER_OF_THREADS_T1; j++){
			ISlave s = new Slave_ChainMT(j, master1, slavesT1, transfo1, srcModelArea, srcModelFlagsArea, true, workTODOArea, idCorrespondencesArea1, trg1ModelArea, transfo1, master2);
			Thread t = new Thread((Runnable) s);
			t.setPriority(Thread.MAX_PRIORITY);
			threadsT1.add(t);
			slavesT1.add(s);
    	}
		
		((Master_ChainMT) master1).setSlaves(slavesT1);
		Thread masterThread = new Thread((Runnable) master1);
		masterThread.start();
		
		for (int j=0; j<LinTraParameters.NUMBER_OF_THREADS_T1; j++){
			threadsT1.get(j).start();				
    	}

		for (Thread t : threadsT1){
			t.join();
		}
		masterThread.join();
		
		setComplete(trg1ModelFlagsArea);
//		System.out.println("Model in trg1 complete");
		
		/** T2  */
		
		List<Thread> threadsT2 = new LinkedList<Thread>();
		List<ISlave> slavesT2 = new LinkedList<ISlave>();
		for (int j=0; j<LinTraParameters.NUMBER_OF_THREADS_T2; j++){
			ISlave s = new Slave_ChainMT(j+LinTraParameters.NUMBER_OF_THREADS_T1, master2, slavesT2, transfo2, trg1ModelArea, trg1ModelFlagsArea, false, workTODOArea2, idCorrespondencesArea2, trg2ModelArea, null, null);
			Thread t = new Thread((Runnable) s);
			t.setPriority(Thread.MAX_PRIORITY);
			threadsT2.add(t);
			slavesT2.add(s);
    	}
	
		((Master_ChainMT) master2).setSlaves(slavesT2);
		Thread master2Thread = new Thread((Runnable) master2);
		master2Thread.start();
		
		for (int j=0; j<LinTraParameters.NUMBER_OF_THREADS_T2; j++){
			threadsT2.get(j).start();
    	}

		for (Thread t : threadsT2){
			t.join();
		}
		master2Thread.join();
		
		
		double timeF = (System.currentTimeMillis() - time0) / 1000;
		return timeF;
		
	}
	
	private void setComplete(IArea area) throws BlackboardException {
		ModelFlags modelFlags = takeModelFlags(area);
		modelFlags.setComplete(true);
		area.write(modelFlags);
	}
	
	private void notifyMaster(IMaster masterNextTransfo) {
		if (!LinTraParameters.T1_AND_THEN_T2){
			if (masterNextTransfo!=null) {
				synchronized (masterNextTransfo) {
	//				System.out.println("Launcher notifies master T2");
					masterNextTransfo.notifyAll();
				}
			}
		}
	}
	
	private ModelFlags takeModelFlags(IArea area) throws BlackboardException {
		ModelFlags modelFlags = (ModelFlags)area.read(LinTraParameters.MODEL_FLAGS_ID);
		while (modelFlags==null){ //it might be null if it isn't in the Area because the agent(s) who is(are) loading the model is/are updating it 
			modelFlags = (ModelFlags)area.take(LinTraParameters.MODEL_FLAGS_ID);
		}
		return modelFlags;
	}

	public void destroy() {
		blackboard.destroyArea(srcModelArea);
		blackboard.destroyArea(trg1ModelArea);
		blackboard.destroyArea(trg2ModelArea);
		blackboard.destroyArea(workTODOArea);
		blackboard.destroyArea(idCorrespondencesArea1);
		blackboard.destroyArea(idCorrespondencesArea2);
		blackboard.destroyArea(this.currentIdArea);
		blackboard.destroyArea(this.currentIdArea2);
		blackboard.destroyArea(this.srcModelFlagsArea);
		blackboard.destroyArea(this.trg1ModelArea);
		blackboard.destroyArea(this.trg2ModelFlagsArea);
	}
	
	public void clearTrgAndTodo(){
		blackboard.clearArea(trg1ModelArea);
		blackboard.clearArea(trg2ModelArea);
		blackboard.clearArea(workTODOArea);
	}

}
