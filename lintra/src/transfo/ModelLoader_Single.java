package transfo;


import java.io.EOFException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

import blackboard.BlackboardException;
import blackboard.IArea;
import blackboard.IdentifiableElement;

public class ModelLoader_Single implements Runnable {

	protected String modelPath;
	protected IArea srcModelArea;
	
	public ModelLoader_Single(String modelPath, IArea modelArea) {
		this.modelPath = modelPath;
		this.srcModelArea = modelArea;
	}
	
	@Override
	public void run() {
		// ClassModelGeneration cmg = new ClassModelGeneration(area);
		// cmg.loadModel(model, area);

		try {
			
			FileInputStream fis = new FileInputStream(modelPath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object o = ois.readObject();
			
			try {

				while (o != null) {
					
					srcModelArea.write((IdentifiableElement)o);
					o = ois.readObject();
				}
				ois.close();
				fis.close();
				
			} catch (EOFException e) {
				// when this exception is thrown means that there are no more
				// objects in the file.
				ois.close();
				fis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
