package runners;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import transfo.IMaster;
import transfo.ITransformation;
import transfo.Master_SingleMT;
import transfo.ModelLoader_Single;
import transfo.Slave_SingleMT;
//import MavenPrj.MavenPrj.InfinispanBlackboard;
import blackboard.*;
import blackboard.IBlackboard.Policy;

public class MTLauncher1Input1Output {
	
	IBlackboard blackboard;
	IArea workTODOArea, srcModelArea, trgModelArea;
	
	public IArea getSrcArea(){
		return srcModelArea;
	}
	
	public IArea getTrgArea(){
		return trgModelArea;
	}

	public void setSrcArea(IArea srcArea){
		srcModelArea = srcArea;
	}

	public void createBlackboard(){
		blackboard = new HashMapBlackboard();
//		blackboard = new HazelcastBlackboard();
//		blackboard = new EhcacheBlackboard();
//		blackboard = new GigaSpacesBlackboard();
//		blackboard = new InfinispanBlackboard();
//		blackboard = new CoherenceBlackboard();
		workTODOArea = blackboard.createArea("processorSpace", Policy.LOCK_TO_READ);
		srcModelArea = blackboard.createArea("processorSpace_Src", Policy.NEVER_LOCK);
		trgModelArea = blackboard.createArea("processorSpace_Trg", Policy.NEVER_LOCK);
	}
	
	public void loadModel(String[] modelPath) throws Exception {
		List<Thread> ts = new LinkedList<Thread>();
		for (int i=0; i<modelPath.length; i++){
			Thread t = new Thread(new ModelLoader_Single(modelPath[i], srcModelArea));
			ts.add(t);
			t.start();
		}
		for (int i=0; i<modelPath.length; i++){
			ts.get(i).join();
		}
	}
	
	public void loadModel(String modelPath) throws Exception {
		ModelLoader_Single mls = new ModelLoader_Single(modelPath, srcModelArea);
		mls.run();
	}
	
	public double launch(ITransformation transfo, int numThreads) throws Exception{
		
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
		
		return timeF;
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
