package transfo;


import java.io.EOFException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

import runners.MTLauncher1Input1Output;
import blackboard.BlackboardException;
import blackboard.IArea;
import blackboard.IdentifiableElement;

public class ModelLoader_Single_Inplace extends ModelLoader_Single implements Runnable {

	private  IArea trgModelArea;
	
	public ModelLoader_Single_Inplace(String modelPath, IArea srcModelArea, IArea trgModelArea) {
		super(modelPath, srcModelArea);
		this.trgModelArea = trgModelArea;
	}
	
	@Override
	public void run() {
		try {
			
			FileInputStream fis = new FileInputStream(modelPath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object o = ois.readObject();
			
			try {

				while (o != null) {
					srcModelArea.write((IdentifiableElement)o);
//					trgModelArea.write((IdentifiableElement)o);
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
