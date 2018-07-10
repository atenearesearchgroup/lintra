package transformations.plain.surveillance;

import java.util.LinkedList;
import java.util.List;

import blackboard.BlackboardException;
import blackboard.IdentifiableElement;
import mm.plain.surveillance.Clock;
import mm.plain.surveillance.Coordinate;
import mm.plain.surveillance.Drone;
import mm.plain.surveillance.UnidentifiedObject;
import runners.MTLauncherInplace;
import transfo.ITransformation;
import transfo.LinTraParameters;

public class PlainSurveillanceRunner {
	
	public static void main(String[] args) throws Exception {
		
		run(100);
		run(1000);
		run(10000);
		run(100000);
	}

	private static void run(int initialModelSize) throws BlackboardException, Exception {
		MTLauncherInplace mtli = new MTLauncherInplace();
		mtli.createBlackboard();
		List<IdentifiableElement> model = createModel(initialModelSize/10);
		mtli.loadModel(model);
	
		System.out.println("Num elems in initial model: "+mtli.getSrcArea().size());
		ITransformation copy = new CopyToTrgSpace(mtli.getTrgArea());
		double time1 = mtli.launch(copy, null, LinTraParameters.NUMBER_OF_THREADS_T1);

		ITransformation t = new PlainSurveillance(mtli.getSrcArea(), mtli.getTrgArea(), mtli.getCurrentIdArea(), mtli.getIdCorrespondencesArea(), mtli.getDeletesArea());
		double time2 = mtli.launch(t, null, LinTraParameters.NUMBER_OF_THREADS_T1);
		System.out.println("Num elems in model after transformation: "+mtli.getTrgArea().size());
		System.out.println("Transformation Time: "+(time1+time2)+"\n");
//		mtli.getTrgArea().print();
//		System.out.println(mtli.getTrgArea().size());
		mtli.destroy();
	}

	private static List<IdentifiableElement> createModel(int n) throws BlackboardException {
		List<IdentifiableElement> model = new LinkedList<IdentifiableElement>();
		
		double idCounter = 1;
		
		Clock clock = new Clock(); clock.setId(idCounter+""); clock.setNow(0);
		model.add(clock); idCounter++;
		
		for (int i = 0; i < n; i++) {
			
			Coordinate c1 = new Coordinate(idCounter+"", 0+1001*i, 0+1001*i); idCounter++;
			Drone d1 = new Drone(idCounter+"", 5.0, 0.78, 20, c1.getId()); idCounter++;
			model.add(c1); model.add(d1);
			
			Coordinate c2 = new Coordinate(idCounter+"", 500+1001*i, 700+1001*i); idCounter++;
			Drone d2 = new Drone(idCounter+"", 9.0, 1.5, 20, c2.getId()); idCounter++;
			model.add(c2); model.add(d2);
			
			Coordinate c3 = new Coordinate(idCounter+"", 700+1001*i, 700+1001*i); idCounter++;
			UnidentifiedObject o1 = new UnidentifiedObject(idCounter+"", 75.0, 3.92, 50, c3.getId(), 0.98); idCounter++;
			model.add(c3);
			model.add(o1);
			
			Coordinate c4 = new Coordinate(idCounter+"", 1000+1001*i, 900+1001*i); idCounter++;
			UnidentifiedObject o2 = new UnidentifiedObject(idCounter+"", 90, 3.14, 60, c4.getId(), 0.98); idCounter++;
			model.add(c4);
			model.add(o2);
			
			Coordinate c5 = new Coordinate(idCounter+"", 1000+1001*i, 900+1001*i); idCounter++;
			UnidentifiedObject o3 = new UnidentifiedObject(idCounter+"", 90, 3.14, 60, c5.getId(), 0.98); idCounter++;
			model.add(c5);
			model.add(o3);
			
		}
		return model;
		
	}
}
