package transfo;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

import blackboard.BlackboardException;
import blackboard.IArea;
import blackboard.IdentifiableElement;

public class ModelLoader_Chain implements Runnable {

	private  String modelPath;
	private  IArea modelArea, modelFlagsArea;
	private  IMaster master;
	private ITransformation nextTransfo;
	
	public ModelLoader_Chain(String modelPath, IArea modelArea, IArea modelFlagsArea, IMaster master, ITransformation nextTransfo) {
		this.modelPath = modelPath;
		this.modelArea = modelArea;
		this.modelFlagsArea = modelFlagsArea;
		this.master = master;
		this.nextTransfo = nextTransfo;
	}
	
	@Override
	public void run() {
		// ClassModelGeneration cmg = new ClassModelGeneration(area);
		// cmg.loadModel(model, area);

		try {

			double maxIdStored = 0;
			int k = LinTraParameters.MODEL_CHUNK_SIZE; // the objects will be loaded in blocks of k
			List<IdentifiableElement> l = new LinkedList<IdentifiableElement>();
			FileInputStream fis = new FileInputStream(modelPath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object o = ois.readObject();
			
			try {

				while (o != null) {
					if (l.size() == k) {
						modelArea.writeAll(l);
						l = new LinkedList<IdentifiableElement>();
						ModelFlags f = new ModelFlags(false, maxIdStored);
						updateModelFlags(f);
						
						notifyMaster();
						notifyNextTransfo();
						
					}
					l.add((IdentifiableElement) o);
					double id = Double.parseDouble(((IdentifiableElement) o)
							.getId());
					if (maxIdStored < id) {
						maxIdStored = id;
					}
					o = ois.readObject();
				}
				ois.close();
				fis.close();
				
			} catch (EOFException e) {
				// when this exception is thrown means that there are no more
				// objects in the file.
				if (l.size() > 0) {
					modelArea.writeAll(l);
					ModelFlags f = new ModelFlags(false, maxIdStored);
					updateModelFlags(f);
					
					notifyMaster();
					notifyNextTransfo();
				}
				ois.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void notifyNextTransfo() {
		synchronized (nextTransfo) {
			nextTransfo.notifyAll();
		}	
	}

	private void updateModelFlags(ModelFlags f) throws BlackboardException, InterruptedException {
		/** If the model flags are not in the area means that they have been taken by another agent that might overwrite them later. If we
		 * just write the model flags, they can be overwritten by that agent with and older version. By doing this, we make sure
		 * that when we take the flags, we are the only agent modifying them so we can overwrite them and the changes won't be undone.
		 */
		ModelFlags modelFlags = (ModelFlags)modelFlagsArea.take(LinTraParameters.MODEL_FLAGS_ID);
		while (modelFlags==null){
			modelFlags = (ModelFlags)modelFlagsArea.take(LinTraParameters.MODEL_FLAGS_ID);
		}
		modelFlagsArea.write(f);
		notifyMaster();
//		System.out.println("ModelFlags updated by ModelLoader: " + modelFlags);
	}

	private void notifyMaster() {
		synchronized (master) {
//			 System.out.println("ModelLoader notifies master");
			 master.notifyAll();
		 }
	}

}
