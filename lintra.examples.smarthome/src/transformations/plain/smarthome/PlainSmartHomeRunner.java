package transformations.plain.smarthome;

import java.util.LinkedList;
import java.util.List;

import blackboard.BlackboardException;
import blackboard.IdentifiableElement;
import mm.plain.smarthome.Home;
import mm.plain.smarthome.Location;
import mm.plain.smarthome.Person;
import mm.plain.smarthome.TempIncr;
import runners.MTLauncherInplace;
import transfo.ITransformation;
import transfo.LinTraParameters;

public class PlainSmartHomeRunner {
	
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
		System.out.println("Transformation Time: "+(time1+time2));
//		mtli.getTrgArea().print();
//		System.out.println(mtli.getTrgArea().size());
		
		mtli.destroy();
	}

	private static List<IdentifiableElement> createModel(int numHomes, int numPersons) throws BlackboardException {
		List<IdentifiableElement> model = new LinkedList<IdentifiableElement>();
		
		double idCounter = 1;
		
		for (int i = 0; i < numHomes; i++) {
			Location l = new Location(Double.toString(idCounter), 1000*idCounter, 1000*idCounter); idCounter++; model.add(l);
		}
		
		for (int i = 0; i < numPersons; i++) {
			Home h = new Home(Double.toString(idCounter), Double.toString(idCounter%numHomes), Double.toString(idCounter%numHomes), 
					30+idCounter%2, 4920+idCounter%100,
					true, 80, idCounter); idCounter++; model.add(h);
			Person p = new Person(Double.toString(idCounter), Double.toString(idCounter%numPersons), idCounter); idCounter++; model.add(p);
		}
		
		for (int i = 0; i < 1*2; i++) {
			TempIncr ti1 = new TempIncr(Double.toString(idCounter), "0.0", 40, 3, 0); idCounter++; model.add(ti1);
			TempIncr ti2 = new TempIncr(Double.toString(idCounter), "0.0", 44, 4, 60); idCounter++; model.add(ti2);
			TempIncr ti3 = new TempIncr(Double.toString(idCounter), "0.0", 48, 3, 120); idCounter++; model.add(ti3);
			TempIncr ti4 = new TempIncr(Double.toString(idCounter), "0.0", 52, 4, 180); idCounter++; model.add(ti4);
			TempIncr ti5 = new TempIncr(Double.toString(idCounter), "0.0", 56, 4, 240); idCounter++; model.add(ti5);
		}
		
		return model;
		
	}
}
