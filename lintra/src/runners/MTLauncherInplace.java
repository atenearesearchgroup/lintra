package runners;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import transfo.CurrentId;
import transfo.IMaster;
import transfo.ITransformation;
import transfo.Master_SingleMT;
import transfo.ModelLoader_Single;
import transfo.ModelLoader_Single_Inplace;
import transfo.Slave_SingleMT;
import blackboard.*;
import blackboard.IBlackboard.Policy;

public class MTLauncherInplace {
	
	IBlackboard blackboard;
	IArea workTODOArea, srcModelArea, trgModelArea, currentIdArea, idCorrespondencesArea, deletesArea;
	
	public IArea getSrcArea(){
		return srcModelArea;
	}
	
	public IArea getTrgArea(){
		return trgModelArea;
	}

	public void setSrcArea(IArea srcArea){
		srcModelArea = srcArea;
	}

	public IArea getCurrentIdArea() {
		return currentIdArea;
	}

	public void setCurrentIdArea(IArea currentIdArea) {
		this.currentIdArea = currentIdArea;
	}

	public IArea getIdCorrespondencesArea() {
		return idCorrespondencesArea;
	}

	public void setIdCorrespondencesArea(IArea idCorrespondencesArea) {
		this.idCorrespondencesArea = idCorrespondencesArea;
	}

	public IArea getDeletesArea() {
		return deletesArea;
	}

	public void setDeletesArea(IArea deletesArea) {
		this.deletesArea = deletesArea;
	}

	public void createBlackboard() throws BlackboardException{
		blackboard = new HashMapBlackboard();
//		blackboard = new HazelcastBlackboard();
//		blackboard = new EhcacheBlackboard();
//		blackboard = new GigaSpacesBlackboard();
//		blackboard = new InfinispanBlackboard();
//		blackboard = new CoherenceBlackboard();
		workTODOArea = blackboard.createArea("processorSpace", Policy.LOCK_TO_READ);
		srcModelArea = blackboard.createArea("processorSpace_Src", Policy.NEVER_LOCK);
		trgModelArea = blackboard.createArea("processorSpace_Trg", Policy.NEVER_LOCK);
		currentIdArea = blackboard.createArea("currentId1", Policy.LOCK_TO_READ); initializeCurrentIdArea(currentIdArea, 1.0);
		idCorrespondencesArea = blackboard.createArea("idCorrespondences1", Policy.NEVER_LOCK);
		deletesArea = blackboard.createArea("deletes", Policy.NEVER_LOCK);
	}
	
	private void initializeCurrentIdArea(IArea currentIdArea, double firstIdAvailable) throws BlackboardException {
		CurrentId cid = new CurrentId(firstIdAvailable);
		currentIdArea.write(cid);
	}
	
	public void loadModel(String[] modelPath) throws Exception {
		List<Thread> ts = new LinkedList<Thread>();
		for (int i=0; i<modelPath.length; i++){
			Thread t = new Thread(new ModelLoader_Single_Inplace(modelPath[i], srcModelArea, trgModelArea));
			ts.add(t);
			t.start();
		}
		for (int i=0; i<modelPath.length; i++){
			ts.get(i).join();
		}
		initializeCurrentIdArea(currentIdArea, srcModelArea.size()+1);
	}
	
	public void loadModel(String modelPath) throws Exception {
		ModelLoader_Single mls = new ModelLoader_Single_Inplace(modelPath, srcModelArea, trgModelArea);
		mls.run();
		initializeCurrentIdArea(currentIdArea, srcModelArea.size()+1);
	}
	
	public void loadModel(Collection<IdentifiableElement> elems) throws Exception {
		for (IdentifiableElement e : elems){
			srcModelArea.write(e);
		}
		initializeCurrentIdArea(currentIdArea, srcModelArea.size()+1);
	}
	
	public double launch(ITransformation transfo, ITransformation normalizeTransfo, int numThreads) throws Exception{
		
		double maxId = srcModelArea.size();
		
		IMaster master = new Master_SingleMT(workTODOArea, srcModelArea, numThreads);
		((Master_SingleMT)master).organizeWork(srcModelArea, maxId);
		
		double time0 = System.currentTimeMillis();
		
		List<Thread> ts = new LinkedList<Thread>();
		for (int j=0; j<numThreads; j++){
			Thread t = new Thread(new Slave_SingleMT(j, transfo, workTODOArea, srcModelArea));
			t.start();
			ts.add(t);
    	}
		for (int j=0; j<ts.size(); j++){
			ts.get(j).join();
		}
		double timeF = (System.currentTimeMillis() - time0) / 1000;
		
		// Second phase in order to normalize the identifiers
		if (normalizeTransfo!=null) { 
			double timeNormalizing = normalizeIds(normalizeTransfo, numThreads);
		}
		
		return timeF;
	}

	private double normalizeIds(ITransformation normalizationT, int numThreads) throws BlackboardException, InterruptedException {
		/**
		 * In-place model transformations need to execute a second phase where the output model is navigated 
		 * in order to normalize its identifiers. 
		 */
		
		double maxId = trgModelArea.size();
		
		IMaster master = new Master_SingleMT(workTODOArea, trgModelArea, numThreads);
		((Master_SingleMT)master).organizeWork(trgModelArea, maxId);
		
		double time0 = System.currentTimeMillis();
		
		List<Thread> ts = new LinkedList<Thread>();
		for (int j=0; j<numThreads; j++){
			Thread t = new Thread(new Slave_SingleMT(j, normalizationT, workTODOArea, trgModelArea));
			t.start();
			ts.add(t);
    	}
		for (int j=0; j<ts.size(); j++){
			ts.get(j).join();
		}
		double time = (System.currentTimeMillis() - time0) / 1000;
		return time;
		
	}

	public void destroy() {
		blackboard.destroyArea(srcModelArea);
		blackboard.destroyArea(trgModelArea);
		blackboard.destroyArea(workTODOArea);
	}
	
	public void clearTrgAndTodo(){
		blackboard.clearArea(trgModelArea);
		blackboard.clearArea(workTODOArea);
	}

	public void serialize(IArea trgArea, String modelPath) throws BlackboardException, IOException {
		
		FileOutputStream fis = new FileOutputStream(modelPath);
		ObjectOutputStream ois = new ObjectOutputStream(fis);
		
		Collection<IdentifiableElement> elems;
		while(trgArea.size()!=0){
			elems = trgArea.take(1000);
			for (IdentifiableElement ie : elems){
				ois.writeObject(ie);
			}
		}
		ois.close();
		fis.close();
	}
}
