package transformations.confidence.smarthome;

import java.util.LinkedList;
import java.util.List;

import blackboard.BlackboardException;
import blackboard.IdentifiableElement;
import mm.confidence.smarthome.Home;
import mm.confidence.smarthome.Location;
import mm.confidence.smarthome.Person;
import mm.confidence.smarthome.TempIncr;
import runners.MTLauncherInplace;
import transfo.ITransformation;
import transfo.LinTraParameters;
import uncertaintypes.UBoolean;
import uncertaintypes.UReal;

public class ConfidenceSmartHomeRunner {
	
	public static void main(String[] args) throws Exception {
		
		run(10, 996);
		run(10, 996*2);
		run(10, 996*3);
		run(10, 996*4);
		
	}

	private static void run(int numHomes, int numPersons) throws BlackboardException, Exception {
		MTLauncherInplace mtli = new MTLauncherInplace();
		mtli.createBlackboard();
		List<IdentifiableElement> model = createModel(numHomes, numPersons);
		mtli.loadModel(model);
	
		System.out.println("Num elems in initial model: "+mtli.getSrcArea().size());
		ITransformation copy = new CopyToTrgSpace(mtli.getTrgArea());
		double time1 = mtli.launch(copy, null, LinTraParameters.NUMBER_OF_THREADS_T1);
		
		ITransformation t = new SmartHome(mtli.getSrcArea(), mtli.getTrgArea(), mtli.getCurrentIdArea(), mtli.getIdCorrespondencesArea(), mtli.getDeletesArea());
		double time2 = mtli.launch(t, null, LinTraParameters.NUMBER_OF_THREADS_T1);
		System.out.println("Num elems in model after transformation: "+mtli.getTrgArea().size());
		System.out.println("Transformation Time: "+(time1+time2)+"\n");
//		mtli.getTrgArea().print();
//		System.out.println(mtli.getTrgArea().size());
		
		mtli.destroy();
	}

	private static List<IdentifiableElement> createModel(int numHomes, int numPersons) throws BlackboardException {
		List<IdentifiableElement> model = new LinkedList<IdentifiableElement>();
		
		double idCounter = 1;
		
		for (int i = 0; i < numHomes; i++) {
			Location l = new Location(Double.toString(idCounter), new UReal(1000*idCounter, 10),
					new UReal(1000*idCounter, 10)); idCounter++; model.add(l);
		}
		
		for (int i = 0; i < numPersons; i++) {
			Home h = new Home(Double.toString(idCounter), Double.toString(idCounter%numHomes), Double.toString(idCounter%numHomes), 
					new UReal(30+idCounter%2, 0.5), new UReal(4920+idCounter%100, 0.5),
					new UBoolean(true, 0.8), 80, new UReal(idCounter, 1)); idCounter++; model.add(h);
			Person p = new Person(Double.toString(idCounter), Double.toString(idCounter%numPersons), new UReal(idCounter, 1)); idCounter++; model.add(p);
		}
		
		for (int i = 0; i < 1*2; i++) {
			TempIncr ti1 = new TempIncr(Double.toString(idCounter), "0.0", new UReal(40, 0.5), new UReal(3, 0.5), new UReal(0, 1)); idCounter++; model.add(ti1);
			TempIncr ti2 = new TempIncr(Double.toString(idCounter), "0.0", new UReal(44, 0.5), new UReal(4, 0.5), new UReal(60, 1)); idCounter++; model.add(ti2);
			TempIncr ti3 = new TempIncr(Double.toString(idCounter), "0.0", new UReal(48, 0.5), new UReal(3, 0.5), new UReal(120, 1)); idCounter++; model.add(ti3);
			TempIncr ti4 = new TempIncr(Double.toString(idCounter), "0.0", new UReal(52, 0.5), new UReal(4, 0.5), new UReal(180, 1)); idCounter++; model.add(ti4);
			TempIncr ti5 = new TempIncr(Double.toString(idCounter), "0.0", new UReal(56, 0.5), new UReal(4, 0.5), new UReal(240, 1)); idCounter++; model.add(ti5);
		}
		
		return model;
		
	}
}
